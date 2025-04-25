package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void handleChat(AsyncChatEvent chatEvent) {
        Player player = chatEvent.getPlayer();
        Component message = chatEvent.message();

        chatEvent.setCancelled(true);

        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);
        Component finalMessage =
                Message.get(player, "<dark_green>%player%</dark_green> <gray>></gray> " + plainMessage);

        Bukkit.broadcast(finalMessage);
    }
}
