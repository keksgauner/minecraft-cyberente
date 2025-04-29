package de.datenente.cyberente.commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatImageCommand extends Command {
    public ChatImageCommand() {
        super("chatimage", "Füge dem Chat ein Bild hinzu", "/", List.of("ci"));
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;

            try {
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream("images/cyberente.png");
                player.sendMessage("Hi, der Stream ist " + stream.toString());
                if (stream == null) {
                    player.sendMessage("§cBild nicht gefunden! Lege es in src/main/resources/images/cyberente.png.");
                    return true;
                }

                BufferedImage image = ImageIO.read(stream);
                if (image == null) {
                    player.sendMessage("§cBild konnte nicht geladen werden!");
                    return true;
                }

                int width = image.getWidth();
                int height = image.getHeight();
                MiniMessage mm = MiniMessage.miniMessage();
                player.sendMessage(Component.text("Hier ist die CyberEnte:"));

                for(int y = 0; y < height; ++y) {
                    StringBuilder line = new StringBuilder();

                    for(int x = 0; x < width; ++x) {
                        int pixel = image.getRGB(x, y);
                        Color color = new Color(pixel, true);
                        if (color.getAlpha() < 128) {
                            line.append("  ");
                        } else {
                            String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                            line.append("<color:").append(hex).append(">█</color>");
                        }
                    }

                    Component coloredLine = mm.deserialize(line.toString());
                    player.sendMessage(coloredLine);
                }
            } catch (Exception var16) {
                var16.printStackTrace();
                player.sendMessage(Component.text("§cFehler beim Senden des Bildes!"));
                String error = var16.getMessage() != null ? var16.getMessage() : var16.toString();
                player.sendMessage(Component.text("§7" + error));
            }

            return true;
        } else {
            sender.sendMessage("Nur Spieler können das verwenden!");
            return true;
        }
    }
}