package de.datenente.cyberente;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatEvent implements Listener {

    @EventHandler
    public void handleChatEvent(AsyncChatEvent chatEvent) {
        Player player = chatEvent.getPlayer();
        Component message = chatEvent.message();

        chatEvent.setCancelled(true);

        String plainMessage = "<" + player.getName() + "> "
                + PlainTextComponentSerializer.plainText().serialize(message);
        var finalMessage = MiniMessage.miniMessage().deserialize(plainMessage);

        Bukkit.broadcast(finalMessage);
    }
}
