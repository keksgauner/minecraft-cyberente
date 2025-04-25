package de.datenente.cyberente.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class Message {
    public static Component get(Player player, String message) {
        return MiniMessage.miniMessage().deserialize(message.replace("%player%", player.getName()));
    }

    public static Component get(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
