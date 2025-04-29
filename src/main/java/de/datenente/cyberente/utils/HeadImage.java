package de.datenente.cyberente.utils;

import de.datenente.cyberente.CyberEnte;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.bukkit.entity.Player;

/**
 * Meine Veraltete Klasse für die Kopf Bilder
 * Es ist hier nur zum Testen.
 * Es ist aus dem Jahr 2022 und aus dem Plugin CoreSystem-Waterfall
 * @author KeksGauner
 * Jul 22, 2022
 */
@Deprecated
public class HeadImage {
    static final String pageURL = "https://minotar.net/avatar/";
    // 'https://minotar.net/helm/{Player}/8.png'
    // 'https://cravatar.eu/helmavatar/{Player}/8.png'
    // 'https://mc-heads.net/avatar/{Player}/8'

    /**
     * Send the player the head of the skin
     * @param player the player
     */
    public static void sendHeadImage(Player player) {
        try {
            URL playerURL = new URL(pageURL + player.getName() + "/8");
            Image image = ImageIO.read(playerURL);
            int y = 0;
            int x = 0;
            StringBuilder line = new StringBuilder();
            for (int pixel = 0; pixel < 72; pixel++) {
                if (x == 8) {
                    y++;
                    x = 0;
                    player.sendMessage(Message.get(line.toString()));
                    line = new StringBuilder();
                } else {
                    Color color = getSpecificColor(image, x, y);
                    String hex =
                            "<color:#" + Integer.toHexString(color.getRGB()).substring(2) + ">";
                    line.append(hex + "\u2588");
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
        if (image instanceof BufferedImage) return new Color(((BufferedImage) image).getRGB(x, y));
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int[] pixels = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException ex) {
            CyberEnte.getInstance().getLogger().severe(ex.getMessage());
        }
        int c = pixels[x * width + y];
        int red = (c & 0xFF0000) >> 16;
        int green = (c & 0xFF00) >> 8;
        int blue = c & 0xFF;
        return new Color(red, green, blue);
    }
}
