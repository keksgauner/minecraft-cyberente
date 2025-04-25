package de.datenente.cyberente.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        Component finalMessage = MiniMessage.miniMessage()
                .deserialize("<gradient:#ADF3FD:#ADF3FD>" + player.getName() + " hat das Spiel betreten </gradient>");

        joinEvent.joinMessage(finalMessage);
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        Component finalMessage = MiniMessage.miniMessage()
                .deserialize("<gradient:#ADF3FD:#ADF3FD>" + player.getName() + " hat das Spiel verlassen </gradient>");

        quitEvent.quitMessage(finalMessage);
    }
}
