package de.datenente.cyberente;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {
    @EventHandler
    public void handleJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        var finalMessage = MiniMessage.miniMessage().deserialize("<gradient:#ADF3FD:#ADF3FD>" + player.getName() + " hat das Spiel betreten </gradient>");

        joinEvent.joinMessage(finalMessage);
    }

    @EventHandler
    public void handleLeve(PlayerQuitEvent quitEvent)
    {
        Player player = quitEvent.getPlayer();
        var finalMessage = MiniMessage.miniMessage().deserialize("<gradient:#ADF3FD:#ADF3FD>" + player.getName() + " hat das Spiel verlassen </gradient>");

        quitEvent.quitMessage(finalMessage);
    }
}
