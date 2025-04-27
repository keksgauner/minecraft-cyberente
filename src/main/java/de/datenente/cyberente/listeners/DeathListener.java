package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.utils.Base64;
import de.datenente.cyberente.utils.Message;
import java.util.HashMap;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DeathListener implements Listener {

    private static final NamespacedKey ITEMS_KEY = new NamespacedKey(CyberEnte.getInstance(), "items");
    private static final NamespacedKey XP_KEY = new NamespacedKey(CyberEnte.getInstance(), "xp");

    // Merken, welche Inventare "Todesinventare" sind
    private final HashMap<UUID, Block> openedDeathInventories = new HashMap<>();

    @EventHandler
    public void handleDeathLocation(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();

        player.sendMessage(Message.get("<gray>Du bist bei <white>X: " + deathLocation.getBlockX() + " Y: "
                + deathLocation.getBlockY() + " Z: " + deathLocation.getBlockZ() + " <gray>gestorben."));
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();

        Bukkit.getScheduler()
                .runTaskLater(
                        CyberEnte.getInstance(),
                        () -> {
                            Block block = deathLocation.getBlock();
                            block.setType(Material.PLAYER_HEAD);

                            BlockState state = block.getState();
                            if (!(state instanceof Skull skull)) return;
                            skull.setOwningPlayer(player);

                            ItemStack[] contents = player.getInventory().getContents();
                            int droppedExp = deathEvent.getDroppedExp();

                            PersistentDataContainer container = skull.getPersistentDataContainer();

                            container.set(
                                    ITEMS_KEY, PersistentDataType.STRING, Base64.itemStackArrayToBase64(contents));
                            container.set(XP_KEY, PersistentDataType.INTEGER, droppedExp);

                            skull.update();
                        },
                        1L);
    }

    @EventHandler
    public void handleRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.PLAYER_HEAD) return;

        Block clickedBlock = event.getClickedBlock();
        BlockState state = clickedBlock.getState();
        if (!(state instanceof Skull skull)) return;

        PersistentDataContainer container = skull.getPersistentDataContainer();

        if (container.has(ITEMS_KEY, PersistentDataType.STRING)) {
            Player player = event.getPlayer();
            String base64 = container.get(ITEMS_KEY, PersistentDataType.STRING);
            ItemStack[] contents = Base64.itemStackArrayFromBase64(base64);

            // Ein eigenes Inventar öffnen
            Inventory deathInventory = Bukkit.createInventory(null, 9 * 4, Component.text("Letzte Items"));

            for (ItemStack item : contents) {
                if (item != null) {
                    deathInventory.addItem(item);
                }
            }

            player.openInventory(deathInventory);

            // Merken, welcher Spieler welches Block-Inventar geöffnet hat
            openedDeathInventories.put(player.getUniqueId(), clickedBlock);

            event.setCancelled(true); // Kein Standard-Interagieren mehr
        }
    }

    @EventHandler
    public void handleInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
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

            player.sendMessage(Component.text("Du hast deine Items und XP eingesammelt."));

            // Entfernen aus Tracking
            openedDeathInventories.remove(uuid);
        }
    }
}
