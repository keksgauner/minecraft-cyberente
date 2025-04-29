package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.ImageConverter;
import de.datenente.cyberente.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        Component finalMessage = Message.get(
                player,
                "<dark_gray>[</dark_gray><green>+</green><dark_gray>]</dark_gray> <gradient:#ADF3FD:#ADF3FD>%player%</gradient>");

        joinEvent.joinMessage(finalMessage);

        // Delete by production
        ImageConverter.sendHeadImage(player);
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        Component finalMessage = Message.get(
                player,
                "<dark_gray>[</dark_gray><dark_red>-</dark_red><dark_gray>]</dark_gray> <gradient:#ADF3FD:#ADF3FD>%player%</gradient>");

        quitEvent.quitMessage(finalMessage);
    }
}
