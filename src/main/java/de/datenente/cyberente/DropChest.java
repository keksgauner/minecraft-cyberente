package de.datenente.cyberente;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropChest implements Listener {

    @EventHandler
    public void handleDrop(PlayerDropItemEvent playerDropItem) {
        Item item = playerDropItem.getItemDrop();
        Player player = playerDropItem.getPlayer();

        Block block = player.getTargetBlockExact(5);

        if (block.getType() == Material.CHEST) {

            Chest chest = (Chest) block.getState();
            chest.getBlockInventory().addItem(item.getItemStack());

            item.remove();
        }
    }
}
