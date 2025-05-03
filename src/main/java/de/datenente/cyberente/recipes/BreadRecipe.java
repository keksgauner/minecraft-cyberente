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

import de.datenente.cyberente.utils.CustomItem;
import de.datenente.cyberente.utils.CustomShapedRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BreadRecipe {

    public static void register() {
        ItemStack halfBread = createItem("half bread", "cyberente:half_bread", 1);
        CustomShapedRecipe.of()
                .key("half_bread")
                .shape("   ", " B ", "   ")
                .ingredient('B', Material.BREAD)
                .result(halfBread)
                .register();

        ItemStack quarterBread = createItem("quarter bread", "cyberente:quarter_bread", 2);
        CustomShapedRecipe.of()
                .key("quarter_bread")
                .shape("   ", " H ", "   ")
                .ingredient('H', halfBread)
                .result(quarterBread)
                .register();

        CustomShapedRecipe.of()
                .key("rett")
                .shape("   ", " Q ", "   ")
                .ingredient('Q', quarterBread)
                .result(createItem("rett", "cyberente:rett", 3))
                .register();

        ItemStack drill = createItem("drill", "cyberente:drill", 4);
        CustomShapedRecipe.of()
                .key("drill")
                .shape(" S ", " B ", "   ")
                .ingredient('S', Material.SUGAR)
                .ingredient('B', Material.BREAD)
                .result(drill)
                .register();

        ItemStack bire = createItem("bire", "cyberente:bire", 5);
        CustomShapedRecipe.of()
                .key("bire")
                .shape(" A ", " B ", "   ")
                .ingredient('A', Material.APPLE)
                .ingredient('B', Material.BREAD)
                .result(bire)
                .register();
    }

    static ItemStack createItem(String name, String itemModel, int customModelData) {
        CustomItem customItem = new CustomItem(Material.BREAD);
        customItem.displayName("<gold>" + name);
        customItem.itemModel(itemModel);
        customItem.customModelData(customModelData);
        return customItem.asItemStack();
    }
}
