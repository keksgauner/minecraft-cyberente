package de.datenente.cyberente.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class StairSittingListener implements Listener {

    @EventHandler
    public void handleClickStair(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = playerInteractEvent.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        if (!clickedBlock.getType().name().endsWith("STAIRS")) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        Location sitLocation = clickedBlock.getLocation().add(0.5D, 0.0D, 0.5D);

        setPlayerSitting(playerInteractEvent.getPlayer(), sitLocation);
    }

    public void setPlayerSitting(Player player, Location location) {
        ArmorStand seat = location.getWorld().spawn(location, ArmorStand.class);
        seat.setInvisible(true);
        seat.setMarker(true);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.setSmall(true);
        seat.addPassenger(player);
    }
}
