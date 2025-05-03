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
import org.bukkit.potion.PotionType;

public class PotionRecipe {

    public void register() {
        ItemStack colaPotion = createItem("cola potion", "cyberente:cola_potion", 1);
        CustomShapedRecipe.of()
                .key("cola_potion")
                .shape(" S ", " P ", "   ")
                .ingredient('S', Material.SUGAR)
                .ingredient(
                        'P',
                        CustomItem.of(Material.POTION)
                                .potionType(PotionType.WATER)
                                .asItemStack())
                .result(colaPotion)
                .register();
    }

    private ItemStack createItem(String name, String itemModel, int customModelData) {
        CustomItem customItem = new CustomItem(Material.POTION);
        customItem.displayName("<gold>" + name);
        customItem.itemModel(itemModel);
        customItem.customModelData(customModelData);
        return customItem.asItemStack();
    }
}
