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
import java.io.*;
import java.util.logging.Level;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStack2Base64 {
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "A error: ", ex);
        }
        return null;
    }

    public static String itemStackArrayToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "A error: ", ex);
        }
        return null;
    }
    /*
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            int length = in.readInt();
            ItemStack[] items = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) in.readObject();
            }
            return items;
        } catch (IOException | ClassNotFoundException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "Failed to deserialize item array", ex);
            return null;
        }
    }

    public static String itemStackArrayToBase64(ItemStack[] items) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeInt(items.length);
            for (ItemStack item : items) {
                out.writeObject(item);
            }
            return Base64Coder.encodeLines(byteOut.toByteArray());
        } catch (IOException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "Failed to serialize item array", ex);
            return null;
        }
    }*/
}
