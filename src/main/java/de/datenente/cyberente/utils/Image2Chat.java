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
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Image2Chat {

    /**
     * Send the head image to the player
     * @param player the player
     */
    public static void sendHeadImage(Player player) throws IOException {
        final String pageURL = "https://minotar.net/avatar/";
        // 'https://minotar.net/helm/{Player}/8.png'
        // 'https://cravatar.eu/helmavatar/{Player}/8.png'
        // 'https://mc-heads.net/avatar/{Player}/8'

        sendImage(player, pageURL + player.getName() + "/8", 8, 8);
    }

    /**
     * Send the image to the player
     * @param sender the sender
     * @param url the image URL
     * @param width the width
     * @param height the height
     */
    public static void sendImage(CommandSender sender, String url, int width, int height) throws IOException {
        List<Component> colorMap = componentList(url, width, height);
        for (Component component : colorMap) {
            sender.sendMessage(component);
        }
    }

    /**
     * Send the image to the player
     * @param sender the sender
     * @param image the image
     * @param width the width
     * @param height the height
     */
    public static void sendImage(CommandSender sender, Image image, int width, int height) {
        List<Component> colorMap = componentList(image, width, height);
        for (Component component : colorMap) {
            sender.sendMessage(component);
        }
    }

    /**
     * Resize the image
     * @param image the image
     * @param width the width
     * @param height the height
     * @return the resized image
     */
    public static Image resizedImage(Image image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Get the image from the URL
     * @param imageUrl the image URL
     * @return the image
     */
    public static Image imageFromURL(String imageUrl) throws IOException {
        URI uri = URI.create(imageUrl);
        if (uri.getScheme() == null) {
            return null;
        }
        URL url = uri.toURL();
        return ImageIO.read(url);
    }

    /**
     * Get the image from the input stream
     * @param inputStream the input stream
     * @return the image
     * @throws IOException if the image could not be read
     */
    public static Image imageFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        return ImageIO.read(inputStream);
    }

    /**
     * Get the image as a list of components
     * @param image the image
     * @param width the width per line
     * @return the list of components
     */
    public static List<Component> componentList(Image image, int width) {
        return componentList(image, '\u2588', width);
    }

    /**
     * Get the image as a list of components
     * @param url the image URL
     * @param width the width per line
     * @param height the height per line
     * @return the list of components
     * @throws IOException if the image could not be read
     */
    public static List<Component> componentList(String url, int width, int height) throws IOException {
        return componentList(resizedImage(imageFromURL(url), width, height), width);
    }

    public static List<Component> componentList(Image image, int width, int height) {
        return componentList(resizedImage(image, width, height), width);
    }

    /**
     * Get the image as a list of components
     * @param image the image
     * @param filler the filler character
     * @param width the width per line
     * @return the list of components
     */
    public static List<Component> componentList(Image image, char filler, int width) {
        int height = image.getHeight(null);

        List<Component> colorMap = new ArrayList<>();
        int y = 0;
        int x = 0;

        StringBuilder line = new StringBuilder();
        for (int pixel = 0; pixel < height * width; pixel++) {
            if (x == width) {
                y++;
                x = 0;
                colorMap.add(Message.text(line.toString()));
                line = new StringBuilder();
            } else {
                Color color = getSpecificColor(image, x, y);
                line.append("<color:")
                        .append(getHexColor(color))
                        .append(">")
                        .append(filler)
                        .append("</color>");
                x++;
            }
        }
        return colorMap;
    }

    /**
     * Get the hex color of the image
     * @param color the color
     * @return the hex color format #RRGGBB
     */
    public static String getHexColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    /**
     * Get the specific color of the image
     * @return Color
     */
    public static Color getSpecificColor(Image image, int x, int y) {
        if (image instanceof BufferedImage bufferedImage) return new Color((bufferedImage).getRGB(x, y));
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int[] pixels = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "A error: ", ex);
            return null;
        }
        int c = pixels[x * width + y];
        int red = (c & 0xFF0000) >> 16;
        int green = (c & 0xFF00) >> 8;
        int blue = c & 0xFF;
        return new Color(red, green, blue);
    }
}
