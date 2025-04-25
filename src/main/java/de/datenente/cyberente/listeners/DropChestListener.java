package de.datenente.cyberente.listeners;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DropChestListener implements Listener {

    @EventHandler
    public void handleDropChest(PlayerDropItemEvent playerDropItemEvent) {
        Item item = playerDropItemEvent.getItemDrop();
        Player player = playerDropItemEvent.getPlayer();

        Block block = player.getTargetBlockExact(10);
        if (block == null || block.getType() != Material.CHEST) return;

        Chest chest = (Chest) block.getState();
        Inventory inventory = chest.getBlockInventory();

        HashMap<Integer, ItemStack> addedItem = inventory.addItem(item.getItemStack());
        if (addedItem.isEmpty()) {
            item.remove();
        }
    }
}
