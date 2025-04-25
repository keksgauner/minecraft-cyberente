package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class StairSittingListener implements Listener {

    @EventHandler
    public void handleClickStair(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

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

        boolean isSing = false;
        for (Block block : potentialSigns) {
            if (block.getType().name().endsWith("SIGN")) {
                isSing = true;
            }
        }
        if (!isSing) return;

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
    public void onDismount(EntityDismountEvent entityDismountEvent) {
        if (entityDismountEvent.getDismounted().hasMetadata("stair"))
            Bukkit.getScheduler()
                    .runTaskLater(
                            CyberEnte.getInstance(),
                            () -> entityDismountEvent.getDismounted().remove(),
                            1L);
    }

    public void setPlayerSitting(Player player, Location location) {
        Pig seat = location.getWorld().spawn(location, Pig.class);
        seat.setInvisible(true);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.setAI(false);
        seat.setMetadata("stair", new FixedMetadataValue(CyberEnte.getInstance(), Boolean.TRUE));
        seat.addPassenger(player);
    }
}
