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

import de.datenente.cyberente.CyberEnte;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Image2Chat {
    static final String pageURL = "https://minotar.net/avatar/";
    // 'https://minotar.net/helm/{Player}/8.png'
    // 'https://cravatar.eu/helmavatar/{Player}/8.png'
    // 'https://mc-heads.net/avatar/{Player}/8'

    /**
     * Send the head image to the player
     * @param player the player
     */
    public static void sendHeadImage(Player player) {
        sendURL(player, pageURL + player.getName() + "/8", 8, 7);
    }

    public static void sendDuck(Player player) {
        sendURL(player, pageURL + player.getName() + "/8", 8, 7);
    }

    public static void sendURL(Player player, String imageUrl, int width, int height) {
        try {
            URL url = URI.create(imageUrl).toURL();
            Image image = ImageIO.read(url);

            List<Component> colorMap = getImageAsComponentList(image, width);
            for (int i = 0; i < height; i++) {
                if (i >= colorMap.size()) break;
                Component component = colorMap.get(i);
                if (component == null) continue;
                player.sendMessage(component);
            }

        } catch (IOException ex) {
            CyberEnte.getInstance().getLogger().severe(ex.getMessage());
        }
    }

    public static List<Component> getImageAsComponentList(Image image, int width) {
        int height = image.getHeight(null);

        List<Component> colorMap = new ArrayList<>();
        int y = 0;
        int x = 0;

        StringBuilder line = new StringBuilder();
        for (int pixel = 0; pixel < height * width; pixel++) {
            if (x == width) {
                y++;
                x = 0;
                colorMap.add(Message.get(line.toString()));
                line = new StringBuilder();
            } else {
                Color color = getSpecificColor(image, x, y);
                line.append("<color:").append(getHexColor(color)).append(">\u2588</color>");
                x++;
            }
        }
        return colorMap;
    }

    public static String getHexColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    /**
     * Get the specific color of the image
     * @return Color
     */
    public static Color getSpecificColor(Image image, int x, int y) {
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
