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
package de.datenente.cyberente.utils;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Message {
    /**
     * Get a message from the MiniMessage
     * @param message the message
     * @return the message
     */
    public static Component text(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * Get a message from the MiniMessage with replacements
     * {0} = {0}, {1} = {1}, ...
     * @param message the message
     * @param replacements the replacements
     * @return the message
     */
    public static Component text(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }
        return text(message);
    }

    /**
     * Send a message to the sender
     * @param sender the sender
     * @param message the message
     */
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(text(message));
    }

    /**
     * Send a message to the sender with replacements
     * {0} = {0}, {1} = {1}, ...
     * @param sender the sender
     * @param message the message
     * @param replacements the replacements
     */
    public static void send(CommandSender sender, String message, Object... replacements) {
        sender.sendMessage(text(message, replacements));
    }

    /**
     * Send a message to all players
     * @param message the message
     */
    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(text(message)));
    }

    /**
     * Send a message to all players with replacements
     * {0} = {0}, {1} = {1}, ...
     * @param message the message
     * @param replacements the replacements
     */
    public static void broadcast(String message, Object... replacements) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(text(message, replacements)));
    }

    public static List<String> filter(List<String> list, String filter) {
        if (filter == null || filter.isEmpty()) {
            return list;
        }
        return list.stream()
                .filter(s -> s.toLowerCase().contains(filter.toLowerCase()))
                .toList();
    }
}
