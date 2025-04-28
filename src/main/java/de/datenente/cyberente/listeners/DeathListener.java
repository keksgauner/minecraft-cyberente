package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.utils.Base64;
import de.datenente.cyberente.utils.Message;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DeathListener implements Listener {

    private static final FixedMetadataValue DEATH_KEY = new FixedMetadataValue(CyberEnte.getInstance(), Boolean.TRUE);
    private static final NamespacedKey ITEMS_KEY = new NamespacedKey(CyberEnte.getInstance(), "items");
    private static final NamespacedKey XP_KEY = new NamespacedKey(CyberEnte.getInstance(), "xp");

    private final HashMap<UUID, Block> openedDeathInventories = new HashMap<>();

    @EventHandler
    public void handleDeathLocation(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();

        player.sendMessage(Message.get("<gray>Du bist bei <white>X: " + deathLocation.getBlockX() + " Y: "
                + deathLocation.getBlockY() + " Z: " + deathLocation.getBlockZ() + " <gray>gestorben."));
    }

    @EventHandler
    public void handleDeathSkull(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();
        Block block = deathLocation.getBlock();
        List<ItemStack> drops = deathEvent.getDrops();

        ItemStack[] contents = drops.toArray(new ItemStack[0]);
        String base64 = Base64.itemStackArrayToBase64(contents);
        int droppedExp = deathEvent.getDroppedExp();

        drops.clear();
        deathEvent.setDroppedExp(0);

        Bukkit.getScheduler()
                .runTaskLater(
                        CyberEnte.getInstance(),
                        () -> {
                            block.setType(Material.PLAYER_HEAD);

                            BlockState state = block.getState();
                            if (!(state instanceof Skull skull)) return;
                            skull.setOwningPlayer(player);
                            skull.setMetadata("death", DEATH_KEY);

                            PersistentDataContainer container = skull.getPersistentDataContainer();
                            container.set(ITEMS_KEY, PersistentDataType.STRING, base64);
                            container.set(XP_KEY, PersistentDataType.INTEGER, droppedExp);

                            skull.update();
                        },
                        1L);
    }

    @EventHandler
    public void handleSkullClick(PlayerInteractEvent interactEvent) {
        if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = interactEvent.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.PLAYER_HEAD) return;

        BlockState state = clickedBlock.getState();
        if (!(state instanceof Skull skull)) return;
        if (!skull.hasMetadata("death")) return;

        interactEvent.setCancelled(true);

        PersistentDataContainer container = skull.getPersistentDataContainer();
        if (!container.has(ITEMS_KEY, PersistentDataType.STRING) && !container.has(XP_KEY, PersistentDataType.INTEGER))
            return;
        String base64 = container.get(ITEMS_KEY, PersistentDataType.STRING);
        ItemStack[] contents = Base64.itemStackArrayFromBase64(base64);
        if (contents == null) return;

        Inventory deathInventory = Bukkit.createInventory(null, 9 * 4, Component.text("Letzte Items"));
        for (ItemStack item : contents) {
            if (item == null) continue;
            deathInventory.addItem(item);
        }

        Player player = interactEvent.getPlayer();
        player.openInventory(deathInventory);

        openedDeathInventories.put(player.getUniqueId(), clickedBlock);
    }

    @EventHandler
    public void handleInventoryClose(InventoryCloseEvent closeEvent) {
        Player player = (Player) closeEvent.getPlayer();
        UUID uuid = player.getUniqueId();

        if (openedDeathInventories.containsKey(uuid)) {
            Block clickedBlock = openedDeathInventories.get(uuid);

            // Beim Schließen XP geben
            BlockState state = clickedBlock.getState();
            if (state instanceof Skull skull) {
                PersistentDataContainer container = skull.getPersistentDataContainer();

                if (container.has(XP_KEY, PersistentDataType.INTEGER)) {
                    int xp = container.get(XP_KEY, PersistentDataType.INTEGER);
                    player.giveExp(xp);
                }
            }

            // Block löschen
            clickedBlock.setType(Material.AIR);


            // Entfernen aus Tracking
            openedDeathInventories.remove(uuid);
        }
    }
}
