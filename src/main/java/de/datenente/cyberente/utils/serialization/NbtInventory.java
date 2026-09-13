/*
 * MIT License
 *
 * Copyright (c) 2025 - 2026 KeksGauner, CyberEnte
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
package de.datenente.cyberente.utils.serialization;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Serializes {@link ItemStack}s and {@link Inventory} contents through the Item-NBT-API.
 *
 * <p>This replaces {@code Base64Inventory}, which relied on {@code BukkitObjectOutputStream}
 * (Java serialization) and therefore broke whenever the server changed its internal item
 * representation. The NBT-API instead writes the item's own serialized NBT form
 * ({@code {components:{...},count:2,id:"minecraft:stone"}}), which the server itself is able to
 * up- and downgrade between versions.
 *
 * <p>Two wire formats are offered for the same data:
 * <ul>
 *   <li><b>Base64</b> - the binary NBT compound, Base64 encoded. Compact and safe to drop into
 *       YAML files or database columns. Use this as the default.</li>
 *   <li><b>SNBT</b> - the human readable NBT string as documented in the API wiki. Handy for
 *       debugging or when the stored value should stay readable.</li>
 * </ul>
 *
 * @see <a href="https://github.com/tr7zw/Item-NBT-API/wiki/Using-the-NBT-API">Using the NBT API</a>
 */
public final class NbtInventory {

    private NbtInventory() {}

    /**
     * Serializes an item array into a Base64 encoded NBT compound. Empty slots are kept.
     *
     * @param items the items to serialize, {@code null} entries are allowed
     * @return the Base64 encoded NBT compound
     * @throws IllegalStateException if the items could not be serialized
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        return encode(NBT.itemStackArrayToNBT(items));
    }

    /**
     * Reads an item array back from a Base64 encoded NBT compound.
     *
     * @param data the Base64 encoded NBT compound
     * @return the items, empty slots are {@code null}
     * @throws IOException if the data is not a valid item array
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        return toItemStackArray(decode(data));
    }

    /**
     * Serializes a single item into a Base64 encoded NBT compound.
     *
     * @param item the item to serialize
     * @return the Base64 encoded NBT compound
     * @throws IllegalStateException if the item could not be serialized
     */
    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        return encode(NBT.itemStackToNBT(item));
    }

    /**
     * Reads a single item back from a Base64 encoded NBT compound.
     *
     * @param data the Base64 encoded NBT compound
     * @return the item, or {@code null} if the compound held no item
     * @throws IOException if the data is not a valid item
     */
    public static ItemStack itemStackFromBase64(String data) throws IOException {
        return NBT.itemStackFromNBT(decode(data));
    }

    /**
     * Serializes the contents of an inventory into a Base64 encoded NBT compound. The inventory
     * size is stored alongside the items, so {@link #fromBase64(String)} can restore it.
     *
     * @param inventory the inventory to serialize
     * @return the Base64 encoded NBT compound
     * @throws IllegalStateException if the inventory could not be serialized
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        return itemStackArrayToBase64(inventory.getContents());
    }

    /**
     * Recreates an inventory from a Base64 encoded NBT compound.
     *
     * @param data the Base64 encoded NBT compound
     * @return a new inventory holding the stored items
     * @throws IOException if the data is not a valid item array
     */
    public static Inventory fromBase64(String data) throws IOException {
        ItemStack[] items = toItemStackArray(decode(data));

        Inventory inventory = Bukkit.getServer().createInventory(null, items.length);
        for (int i = 0; i < items.length; i++) {
            inventory.setItem(i, items[i]);
        }

        return inventory;
    }

    /**
     * Serializes an item array into a SNBT string. Empty slots are kept.
     *
     * @param items the items to serialize, {@code null} entries are allowed
     * @return the SNBT representation of the items
     */
    public static String itemStackArrayToSnbt(ItemStack[] items) {
        return NBT.itemStackArrayToNBT(items).toString();
    }

    /**
     * Reads an item array back from a SNBT string.
     *
     * @param snbt the SNBT representation of the items
     * @return the items, empty slots are {@code null}
     * @throws IOException if the string is not a valid item array
     */
    public static ItemStack[] itemStackArrayFromSnbt(String snbt) throws IOException {
        return toItemStackArray(parse(snbt));
    }

    private static String encode(ReadableNBT nbt) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            nbt.writeCompound(outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException | RuntimeException ex) {
            throw new IllegalStateException("Unable to serialize item stacks.", ex);
        }
    }

    private static ReadWriteNBT decode(String data) throws IOException {
        if (data == null || data.isBlank()) {
            throw new IOException("No NBT data to decode.");
        }

        byte[] compound;
        try {
            // Accepts the line wrapped output of the old Base64Inventory as well
            compound = Base64.getMimeDecoder().decode(data);
        } catch (IllegalArgumentException ex) {
            throw new IOException("Unable to decode Base64 data.", ex);
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(compound)) {
            return NBT.readNBT(inputStream);
        } catch (RuntimeException ex) {
            throw new IOException("Unable to read NBT data.", ex);
        }
    }

    private static ReadWriteNBT parse(String snbt) throws IOException {
        if (snbt == null || snbt.isBlank()) {
            throw new IOException("No NBT data to parse.");
        }

        try {
            return NBT.parseNBT(snbt);
        } catch (RuntimeException ex) {
            throw new IOException("Unable to parse SNBT data.", ex);
        }
    }

    private static ItemStack[] toItemStackArray(ReadableNBT nbt) throws IOException {
        ItemStack[] items = NBT.itemStackArrayFromNBT(nbt);
        if (items == null) {
            throw new IOException("NBT data does not contain an item array.");
        }

        // The API fills empty slots with AIR stacks, callers expect null like with Base64Inventory
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType().isAir()) {
                items[i] = null;
            }
        }

        return items;
    }
}
