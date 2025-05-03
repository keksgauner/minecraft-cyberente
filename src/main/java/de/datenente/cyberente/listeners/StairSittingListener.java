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
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class StairSittingListener implements Listener {

    static final FixedMetadataValue STAIRS_VALUE = new FixedMetadataValue(CyberEnte.getInstance(), Boolean.TRUE);

    public static void clean() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Pig pig && pig.hasMetadata("stair")) {
                    pig.remove();
                }
            }
        }
    }

    public void setPlayerSitting(Player player, Location location) {
        Pig seat = location.getWorld().spawn(location, Pig.class);
        seat.setInvisible(true);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.setAI(false);
        seat.setMetadata("stair", STAIRS_VALUE);
        seat.addPassenger(player);
    }

    @EventHandler
    public void onClickStair(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = playerInteractEvent.getClickedBlock();
        if (clickedBlock == null || !clickedBlock.getType().name().endsWith("STAIRS")) return;

        // Check ob ein schild an der seite ist
        List<Block> potentialSigns = List.of(
                // X
                clickedBlock.getLocation().add(1, 0, 0).getBlock(),
                clickedBlock.getLocation().add(-1, 0, 0).getBlock(),
                // Z
                clickedBlock.getLocation().add(0, 0, 1).getBlock(),
                clickedBlock.getLocation().add(0, 0, -1).getBlock());

        boolean isSign = false;
        for (Block block : potentialSigns) {
            if (block.getType().name().endsWith("SIGN")) {
                isSign = true;
            }
        }
        if (!isSign) return;

        Player player = playerInteractEvent.getPlayer();
        Location sitLocation = clickedBlock.getLocation().add(0.5D, -0.4D, 0.5D);

        Stairs stairs = (Stairs) clickedBlock.getBlockData();
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        for (BlockFace blockFace : blockFaces) {
            if (stairs.getFacing() == blockFace) {
                // Setze den Yaw basierend auf der Ausrichtung der Treppe
                // Kenne aber auch kein besseren weg
                float[] yawValues = {0, 90, 180, 270};
                for (int i = 0; i < blockFaces.length; i++) {
                    if (stairs.getFacing() == blockFaces[i]) {
                        sitLocation.setYaw(yawValues[i]);
                        break;
                    }
                }
                setPlayerSitting(player, sitLocation);
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent entityDismountEvent) {
        if (!entityDismountEvent.getDismounted().hasMetadata("stair")) return;
        entityDismountEvent.getDismounted().remove();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();

        if (vehicle instanceof Pig pig && pig.hasMetadata("stair")) {
            pig.remove();
        }
    }
}
