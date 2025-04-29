package de.datenente.cyberente.utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Meine Veraltete Klasse für die Kopf Bilder
 * Es ist hier nur zum Testen.
 * Es ist aus dem Jahr 2022 und aus dem Plugin CoreSystem-Waterfall
 * @author KeksGauner
 * Jul 22, 2022
 */
@Deprecated
public class HeadImage {
    public static final String pageURL = "https://minotar.net/avatar/";
    // 'https://minotar.net/helm/{Player}/8.png'
    // 'https://cravatar.eu/helmavatar/{Player}/8.png'
    // 'https://mc-heads.net/avatar/{Player}/8'

    /**
     * Send the player the head of the skin
     * @param player the player
     * @param msg the List to be written right of the head
     */
    public static void sendHeadImage(Player player, List<?> msg) {
        try {
            URL playerURL = new URL(pageURL + player.getName() + "/8");
            Image image = ImageIO.read(playerURL);
            int y = 0;
            int x = 0;
            StringBuilder line = new StringBuilder();
            player.sendMessage(Component.text(""));
            for (int pixel = 0; pixel < 72; pixel++) {
                if (x == 8) {
                    y++;
                    x = 0;
                    for (int list = 0; list < msg.size(); list++) {
                        if (y == list) {
                            line.append(ChatColor.translateAlternateColorCodes('&', (String) msg.get(list)));
                        }
                    }
                    player.sendMessage(Component.text(line.toString()));
                    line = new StringBuilder();
                } else {
                    Color color = getSpecificColor(image, x, y);
                    String hex = "#" + Integer.toHexString(color.getRGB()).substring(2);
                    line.append(replaceColors(hex)).append("\u2588");
                    x++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get of a image a color
     * @return Color
     */
    private static Color getSpecificColor(Image image, int x, int y) {
        if (image instanceof BufferedImage)
            return new Color(((BufferedImage)image).getRGB(x, y));
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int[] pixels = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int c = pixels[x * width + y];
        int red = (c & 0xFF0000) >> 16;
        int green = (c & 0xFF00) >> 8;
        int blue = c & 0xFF;
        return new Color(red, green, blue);
    }

    /**
     * replace hexColor to ChatColor
     * @return ChatColor as a String
     */
    public static String replaceColors(String toReplace) {
        Pattern pattern = Pattern.compile("(#[a-fA-F\\d]{6})");
        Matcher matcher = pattern.matcher(toReplace);
        while (matcher.find()) {
            String color = toReplace.substring(matcher.start(), matcher.end());
            toReplace = toReplace.replace(color, "" + ChatColor.of(color));
            matcher = pattern.matcher(toReplace);
        }
        return toReplace;

    }
}