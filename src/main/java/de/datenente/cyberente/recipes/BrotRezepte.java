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

import de.datenente.cyberente.utils.CustomShapedRecipe;
import de.datenente.cyberente.utils.Message;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BrotRezepte {

    public void register() {
        CustomShapedRecipe.of()
                .key("half_bread")
                .result(create("half bread"))
                .shape("   ", " B ", "   ")
                .ingredient('B', Material.BREAD)
                .register();

        CustomShapedRecipe.of()
                .key("quarter_bread")
                .result(create("quarter bread"))
                .shape("   ", " H ", "   ")
                .ingredient('H', create("half bread"))
                .register();

        CustomShapedRecipe.of()
                .key("rett")
                .result(create("rett"))
                .shape("   ", " V ", "   ")
                .ingredient('V', create("quarter bread"))
                .register();

        CustomShapedRecipe.of()
                .key("drill")
                .result(create("drill"))
                .shape(" S ", " B ", "   ")
                .ingredient('S', Material.SUGAR)
                .ingredient('B', Material.BREAD)
                .register();

        CustomShapedRecipe.of()
                .key("bire")
                .result(create("bire"))
                .shape(" A ", " B ", "   ")
                .ingredient('A', Material.APPLE)
                .ingredient('B', Material.BREAD)
                .register();
    }

    private ItemStack create(String name) {
        ItemStack item = new ItemStack(Material.BREAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Message.text("<gold>" + name));
            item.setItemMeta(meta);
        }
        return item;
    }
}
