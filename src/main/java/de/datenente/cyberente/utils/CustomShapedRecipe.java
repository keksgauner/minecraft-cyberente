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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomShapedRecipe {

    String key;
    ItemStack result;
    List<String> shape = new ArrayList<>();
    Map<Character, ItemStack> ingredients = new HashMap<>();

    /**
     * Creates a new CustomShapedRecipe instance.
     *
     * @return a new instance of CustomShapedRecipe
     */
    public static CustomShapedRecipe of() {
        return new CustomShapedRecipe();
    }

    public CustomShapedRecipe shape(String... shape) {
        this.shape = List.of(shape);
        return this;
    }

    public CustomShapedRecipe result(Material result) {
        this.result = new ItemStack(result);
        return this;
    }

    public CustomShapedRecipe result(ItemStack result) {
        this.result = result;
        return this;
    }

    public CustomShapedRecipe key(String key) {
        this.key = key;
        return this;
    }

    public CustomShapedRecipe ingredient(char character, Material material) {
        this.getIngredients().put(character, new ItemStack(material));
        return this;
    }

    public CustomShapedRecipe ingredient(char character, ItemStack itemStack) {
        this.getIngredients().put(character, itemStack);
        return this;
    }

    public void register() {
        NamespacedKey namespacedKey = new NamespacedKey(CyberEnte.getInstance(), this.getKey());
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, this.getResult());

        recipe.shape(this.getShape().toArray(new String[0]));
        ingredients.forEach(recipe::setIngredient);

        CyberEnte.getInstance().getServer().addRecipe(recipe);
    }
}
