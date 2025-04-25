package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.Message;
import io.papermc.paper.event.world.WorldGameRuleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheatBlockListener implements Listener {

    @EventHandler
    public void handleGameModeChange(PlayerGameModeChangeEvent gameModeChangeEvent) {
        gameModeChangeEvent.setCancelled(true);
        Player player = gameModeChangeEvent.getPlayer();
        Bukkit.broadcast(Message.get(
                player,
                "<red>%player% wollte in den "
                        + gameModeChangeEvent.getNewGameMode().name()
                        + "-Modus wechseln – wurde erfolgreich blockiert.</red>"));
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void handleGameRuleChange(WorldGameRuleChangeEvent gameRuleChangeEvent) {
        gameRuleChangeEvent.setCancelled(true);
        Bukkit.broadcast(Message.get("<red>Die Gamerule darf nicht verändert werden.</red>"));
    }

    @EventHandler
    public void handleTeleport(PlayerTeleportEvent teleportEvent) {
        if (teleportEvent.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
            teleportEvent.setCancelled(true);
            Bukkit.broadcast(Message.get(
                    "<red>" + teleportEvent.getPlayer().getName() + " Es darf sich nicht Teleportiert werden!</red>"));
        }
    }
}
