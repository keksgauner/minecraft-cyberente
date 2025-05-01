/*
 * MIT License
 *
 * Copyright (c) 2025 KeksGauner, CyberEnte
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.ItemStack2Base64;
import de.datenente.cyberente.utils.Message;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class DeathListener implements Listener {

    private static final FixedMetadataValue DEATH_KEY = new FixedMetadataValue(CyberEnte.getInstance(), Boolean.TRUE);

    private final HashMap<UUID, Block> openedDeathInventories = new HashMap<>();

    @EventHandler
    public void handleDeathLocation(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();

        Message.send(
                player,
                "<gray>Du bist bei <white>X: {0} Y: {1} Z: {2} <gray>gestorben.",
                deathLocation.getBlockX(),
                deathLocation.getBlockY(),
                deathLocation.getBlockZ());
    }

    @EventHandler
    public void handleDeathSkull(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();
        Block deathBlock = deathLocation.getBlock();

        List<ItemStack> drops = deathEvent.getDrops();
        ItemStack[] contents = drops.toArray(new ItemStack[0]);
        String base64 = ItemStack2Base64.itemStackArrayToBase64(contents);
        int droppedExp = deathEvent.getDroppedExp();

        drops.clear();
        deathEvent.setDroppedExp(0);

        while (deathBlock.getType() != Material.AIR) {
            deathLocation = deathLocation.add(0, 1, 0);
            deathBlock = deathLocation.getBlock();
        }

        deathBlock.setType(Material.PLAYER_HEAD);

        BlockState state = deathBlock.getState();
        if (!(state instanceof Skull skull)) return;
        skull.setOwningPlayer(player);
        skull.setMetadata("death", DEATH_KEY);
        skull.update();

        StorageConfig storageConfig = StorageConfig.getInstance();
        storageConfig.setDeathSkull(deathLocation, base64, droppedExp);
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

        Player player = interactEvent.getPlayer();
        if (openedDeathInventories.containsValue(clickedBlock)) {
            return;
        }

        interactEvent.setCancelled(true);

        StorageConfig storageConfig = StorageConfig.getInstance();
        StorageObject.DeathSkull deathSkull = storageConfig.getDeathSkull(clickedBlock.getLocation());
        if (deathSkull == null) {
            Message.send(player, "<red>Es ist kein Inventar vorhanden!");
            return;
        }
        ItemStack[] contents = ItemStack2Base64.itemStackArrayFromBase64(deathSkull.getBase64());
        if (contents == null) return;
        int xp = deathSkull.getXp();

        storageConfig.removeDeathSkull(clickedBlock.getLocation());

        Inventory deathInventory = Bukkit.createInventory(null, 9 * 6, Component.text("Letzte Items"));
        for (ItemStack item : contents) {
            if (item == null) continue;
            deathInventory.addItem(item);
        }

        player.giveExp(xp);

        player.openInventory(deathInventory);

        UUID uuid = player.getUniqueId();
        openedDeathInventories.put(uuid, clickedBlock);
    }

    @EventHandler
    public void handleInventoryClose(InventoryCloseEvent closeEvent) {
        Player player = (Player) closeEvent.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!openedDeathInventories.containsKey(uuid)) return;
        Block clickedBlock = openedDeathInventories.get(uuid);

        BlockState state = clickedBlock.getState();
        if (!(state instanceof Skull skull)) return;
        if (!skull.hasMetadata("death")) return;

        Inventory inventory = closeEvent.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            clickedBlock.getWorld().dropItemNaturally(clickedBlock.getLocation(), item);
        }

        clickedBlock.setType(Material.AIR);
        openedDeathInventories.remove(uuid);
    }
}
