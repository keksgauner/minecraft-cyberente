package de.datenente.cyberente.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropChestListener implements Listener {

    @EventHandler
    public void handleDropChest(PlayerDropItemEvent playerDropItem) {
        Item item = playerDropItem.getItemDrop();
        Player player = playerDropItem.getPlayer();

        Block block = player.getTargetBlockExact(5);
        if (block == null) {
            return;
        }

        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        if (chest.getBlockInventory().firstEmpty() != -1) {
            chest.getBlockInventory().addItem(item.getItemStack());
            item.remove();
        }
    }
}
