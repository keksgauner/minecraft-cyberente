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
package de.datenente.cyberente.commands;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.utils.Image2Chat;
import de.datenente.cyberente.utils.Message;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatImageCommand extends Command {

    public ChatImageCommand() {
        super("chatimage", "Sende im Chat ein Bild.", "/", List.of("ci"));
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {

        if (args.length == 1) {
            // InputStream stream = this.getClass().getClassLoader().getResourceAsStream("images/cyberente.png");
            // BufferedImage image = ImageIO.read(stream);

            try {
                Image image = Image2Chat.getImageFromURL(args[0]);
                if (image == null) {
                    Message.send(sender, "<red>Das Bild konnte nicht geladen werden!</red>");
                    return true;
                }

                if (sender instanceof ConsoleCommandSender consoleCommandSender)
                    Image2Chat.sendImage(consoleCommandSender, image, 50, 30);

                for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                    Image2Chat.sendImage(onlinePlayer, image, 30, 20);
                }
            } catch (IOException ex) {
                Message.send(sender, "<red>Das Bild konnte nicht geladen werden!</red>");
                CyberEnte.getInstance().getLogger().log(Level.SEVERE, "A error: ", ex);
            }

            return true;
        }

        if (args.length == 3) {
            try {
                int width = Integer.parseInt(args[1]);
                int height = Integer.parseInt(args[2]);

                Image image = Image2Chat.getImageFromURL(args[0]);
                if (image == null) {
                    Message.send(sender, "<red>Das Bild konnte nicht geladen werden!</red>");
                    return true;
                }

                if (sender instanceof ConsoleCommandSender consoleCommandSender)
                    Image2Chat.sendImage(consoleCommandSender, image, width, height);

                for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                    Image2Chat.sendImage(onlinePlayer, image, width, height);
                }
            } catch (NumberFormatException ex) {
                Message.send(sender, "<red>Die Breite und Höhe müssen Zahlen sein!</red>");
            } catch (IOException ex) {
                Message.send(sender, "<red>Das Bild konnte nicht geladen werden!</red>");
                CyberEnte.getInstance().getLogger().log(Level.SEVERE, "A error: ", ex);
            }

            return true;
        }

        Message.send(
                sender,
                "<red>Bitte gib eine URL an!</red> <green>Beispiel: /ci https://example.com/image.png breite höhe</green>");

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {

        if (args.length == 0) {
            return List.of("https://example.com/image.png");
        }

        if (args.length == 1) {
            return List.of("8", "10", "30");
        }

        if (args.length == 2) {
            return List.of("8", "10", "20");
        }

        return List.of();
    }
}
