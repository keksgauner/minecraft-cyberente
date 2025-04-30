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
package de.datenente.cyberente.recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class BrotRezepte {

    private final Plugin plugin;

    public BrotRezepte(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Bukkit.addRecipe(rezept("halb_brot", create("Halbes Brot"), "   ", " B ", "  ", 'B', Material.BREAD));
        Bukkit.addRecipe(rezept("viertel_brot", create("Viertel Brot"), "   ", " H ", "  ", 'H', Material.BREAD));
        Bukkit.addRecipe(rezept("rett", create("Rett"), "   ", " V ", "  ", 'V', Material.BREAD));
        Bukkit.addRecipe(rezept("dreh", create("Dreh"), " S ", " B ", "  ", 'S', Material.SUGAR, 'B', Material.BREAD));
        Bukkit.addRecipe(rezept("bire", create("Bire"), " A ", " B ", "  ", 'A', Material.APPLE, 'B', Material.BREAD));
    }

    private ItemStack create(String name) {
        ItemStack item = new ItemStack(Material.BREAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ShapedRecipe rezept(
            String keyName, ItemStack result, String row1, String row2, String row3, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, keyName);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(row1, row2, row3);

        for (int i = 0; i < ingredients.length; i += 2) {
            char symbol = (char) ingredients[i];
            Material material = (Material) ingredients[i + 1];
            recipe.setIngredient(symbol, material);
        }

        return recipe;
    }
}
