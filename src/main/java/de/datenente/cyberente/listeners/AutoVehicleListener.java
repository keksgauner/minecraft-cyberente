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
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AutoVehicleListener implements Listener {
    private static final HashMap<UUID, ArmorStand> vehicles = new HashMap<>();

    // deprecated:
    public static void spawn(Player player) {
        Location loc = player.getLocation()
                .add(player.getLocation().getDirection().normalize().multiply(2));
        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setBasePlate(false);
        stand.setCustomName("Auto");
        stand.setCustomNameVisible(false);
        stand.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
        vehicles.put(player.getUniqueId(), stand);
    }

    // neu:
    public static  void spawn(Player player, Vehicle vehicle)
    {
        Location loc = player.getLocation()
                .add(player.getLocation().getDirection().normalize().multiply(2));

        Vehicle visibleVehicle = vehicle;

        vehicle = new
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof ArmorStand stand)) return;
        if (!"Auto".equals(stand.getCustomName())) return;

        Player player = e.getPlayer();
        player.setInvisible(true);
        player.setInvulnerable(true);
        player.setGravity(false);
        player.teleport(stand.getLocation().add(0, 1, 0));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !vehicles.containsKey(player.getUniqueId()) || !player.isSneaking()) return;

                ArmorStand vehicle = vehicles.get(player.getUniqueId());
                if (vehicle == null || vehicle.isDead()) {
                    cancel();
                    return;
                }

                Vector dir = player.getLocation().getDirection().normalize();
                double speed = CyberEnte.getInstance().getConfig().getDouble("speed");
                vehicle.setVelocity(dir.multiply(speed));
                player.teleport(vehicle.getLocation().add(0, 1, 0));
            }
        }.runTaskTimer(CyberEnte.getInstance(), 0L, 2L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ArmorStand v = vehicles.remove(e.getPlayer().getUniqueId());
        if (v != null) v.remove();
    }
}
