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
import de.datenente.cyberente.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.entity.boat.OakBoat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class AutoVehicleListener implements Listener {

    static final FixedMetadataValue CAR_VALUE = new FixedMetadataValue(CyberEnte.getInstance(), Boolean.TRUE);

    public static void clean() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof OakBoat boat && boat.hasMetadata("car")) {
                    boat.remove();
                }
            }
        }
    }

    public static void spawnCar(Player player) {
        Location location = player.getLocation();
        Entity entity = location.getWorld().spawnEntity(location, EntityType.OAK_BOAT);
        if (!(entity instanceof Boat boat)) return;

        boat.customName(Message.text("<red>Auto"));
        boat.setMetadata("car", CAR_VALUE);
        boat.addPassenger(player);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent entityDismountEvent) {
        if (!(entityDismountEvent.getDismounted() instanceof OakBoat boat)) return;
        if (!boat.hasMetadata("car")) return;
        boat.remove();
    }

    @EventHandler
    public void onEntityMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        Location locationFrom = playerMoveEvent.getFrom();
        Location locationTo = playerMoveEvent.getTo();

        // Wenn der Spieler sich nicht bewegt hat, return
        if (locationFrom.getBlockX() == locationTo.getBlockX()
                && locationFrom.getBlockY() == locationTo.getBlockY()
                && locationFrom.getBlockZ() == locationTo.getBlockZ()) {
            return;
        }

        if (player.getVehicle() == null) return;
        if (!(player.getVehicle() instanceof OakBoat boat)) return;
        if (!boat.hasMetadata("car")) return;

        boat.setVelocity(boat.getVelocity().multiply(1.5));
    }
}
