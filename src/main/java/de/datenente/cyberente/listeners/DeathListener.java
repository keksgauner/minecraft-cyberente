package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void handleDeath(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getPlayer();
        Location deathLocation = deathEvent.getPlayer().getLastDeathLocation();
        if (deathLocation == null) return;

        player.sendMessage(Message.get("<gray>Du bist bei <white>X: " + deathLocation.getX() + " Y: "
                + deathLocation.getY() + " Z: "
                + deathLocation.getZ() + " <gray>gestorben."));
    }
}
