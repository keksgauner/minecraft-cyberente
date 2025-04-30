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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
@Setter
public class CustomItem {

    final ItemStack itemStack;
    final ItemFactory itemFactory;
    final ItemMeta itemMeta;

    public CustomItem(Material material) {
        this.itemFactory = Bukkit.getItemFactory();
        this.itemStack = new ItemStack(material);

        if (this.getItemStack().getItemMeta() == null) {
            this.itemMeta =
                    this.getItemFactory().getItemMeta(this.getItemStack().getType());
        } else {
            this.itemMeta = this.getItemStack().getItemMeta();
        }
    }

    public ItemStack asItemStack() {
        if (this.getItemStack().getType() == Material.AIR) {
            return this.getItemStack();
        }

        this.getItemStack().setItemMeta(this.getItemFactory().asMetaFor(this.getItemMeta(), this.getItemStack()));
        return this.getItemStack();
    }

    public CustomItem setName(String name) {
        this.getItemMeta().displayName(Message.text(name));
        return this;
    }

    public CustomItem setLore(String... lore) {
        List<Component> loreList = new ArrayList<>();
        Arrays.stream(lore).forEach(l -> loreList.add(Message.text(l)));
        this.getItemMeta().lore(loreList);
        return this;
    }

    public CustomItem setItemModel(String key) {
        String[] splitKey = key.split(":");
        if (splitKey.length == 2) {
            this.getItemMeta().setItemModel(new NamespacedKey(splitKey[0], splitKey[1]));
        }
        return this;
    }

    public CustomItem setOwningPlayer(OfflinePlayer player) {
        if (this.getItemStack().getType() == Material.PLAYER_HEAD) {
            ((SkullMeta) this.getItemMeta()).setOwningPlayer(player);
        }
        return this;
    }

    public CustomItem setAmount(int size) {
        this.getItemStack().setAmount(size);
        return this;
    }
}
