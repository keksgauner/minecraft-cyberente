/*
 * MIT License
 *
 * Copyright (c) 2025 KeksGauner, CyberEnte
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncChatEvent chatEvent) {
        Player player = chatEvent.getPlayer();
        Component message = chatEvent.message();
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        chatEvent.setCancelled(true);

        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        // # is hidden in the chat. lg KeksGauner
        if (plainMessage.startsWith("#")) return;

        Component finalMessage = Message.text(
                "<hover:show_text:'<gold>{0}</gold>'><dark_green>{1}</dark_green></hover> <gray>></gray> {2}",
                currentTime, player.getName(), plainMessage);

        finalMessage = pingMessage(finalMessage);
        finalMessage = linkMessage(finalMessage);

        Bukkit.broadcast(finalMessage);
    }

    Component pingMessage(Component message) {
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        // Regex pattern to match player names
        Pattern pattern = Pattern.compile("@[a-zA-Z0-9_.]*");
        Matcher matcher = pattern.matcher(plainMessage);

        while (matcher.find()) {
            // Highlight the mentioned player name
            message = message.replaceText(TextReplacementConfig.builder()
                    .match(matcher.group())
                    .replacement(Message.text("<i><color:#FB4EE9>{0}</color></i>", matcher.group()))
                    .build());

            // Notify the mentioned player with a sound
            String currentPlayer = matcher.group().substring(1);
            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> onlinePlayer.getName().equalsIgnoreCase(currentPlayer))
                    .forEach(onlinePlayer ->
                            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1));
        }

        return message;
    }

    Component linkMessage(Component message) {
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        // Regex pattern to match links
        Pattern pattern = Pattern.compile(
                "https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)");
        Matcher matcher = pattern.matcher(plainMessage);

        while (matcher.find()) {

            // Highlight the link
            message = message.replaceText(TextReplacementConfig.builder()
                    .match(matcher.group())
                    .replacement(Message.text(
                            "<hover:show_text:'<gold>Open URL</gold>'><click:open_url:'{0}'><green>{0}</green></click></hover>",
                            matcher.group()))
                    .build());
        }

        return message;
    }
}
