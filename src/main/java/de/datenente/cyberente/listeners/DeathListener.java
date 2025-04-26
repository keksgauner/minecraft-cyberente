package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener {

    @EventHandler
    public void handleDeath(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        Location deathLocation = player.getLocation();

        player.sendMessage(Message.get("<gray>Du bist bei <white>X: " + deathLocation.getX() + " Y: "
                + deathLocation.getY() + " Z: " + deathLocation.getZ() + " <gray>gestorben."));

        Block block = deathLocation.getBlock();
        block.setType(Material.CHEST);

        Bukkit.getScheduler()
                .runTaskLater(
                        CyberEnte.getInstance(),
                        () -> {
                            if (block.getState() instanceof Chest chest) {
                                ItemStack[] contents = deathEvent.getDrops().toArray(new ItemStack[0]);
                                for (ItemStack item : contents) {
                                    if (item != null) {
                                        chest.getBlockInventory().addItem(item);
                                    }
                                }

                                // Drops aus dem Event entfernen, damit sie nicht zusätzlich gedroppt werden
                                deathEvent.getDrops().clear();

                                // Hologramm (ArmorStand) über der Truhe
                                Location holoLocation =
                                        deathLocation.clone().add(0.5, 1.5, 0.5); // etwas über der Truhe
                                ArmorStand hologram = (ArmorStand)
                                        player.getWorld().spawnEntity(holoLocation, EntityType.ARMOR_STAND);
                                hologram.setCustomName("§f☠ " + player.getName() + " ☠");
                                hologram.setCustomNameVisible(true);
                                hologram.setInvisible(true);
                                hologram.setGravity(false);
                                hologram.setMarker(true); // kleiner Hitbox
                            }
                        },
                        1L);
    }
}
