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

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomItem {

    private ItemStack item;
    private ItemMeta itemMeta;
    // subID = 1.8.9 Builder -> creates the ItemStack
    public CustomItem(Material material, short subID) {
        this.item = new ItemStack(material, 1, subID);
        this.itemMeta = this.item.getItemMeta();
    }

    // For newer versions -> creates the ItemStack
    public CustomItem(Material material) {
        this(material, (short) 0);
    }

    public CustomItem setCustomModelData(Integer modelData) {
        this.itemMeta.setCustomModelData(modelData);
        return this;
    }

    // adding an Enchantment to your Item
    public CustomItem addEnchantment(Enchantment enchantment, int amount, boolean test) {
        this.itemMeta.addEnchant(enchantment, amount, test);
        return this;
    }

    // adding the player head texture to the player head -> requires PLAYER_HEAD
    public CustomItem setSkull(String displayName, Player player, String... lore) {
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(Arrays.asList(lore));
        skullMeta.setOwningPlayer(Bukkit.getPlayer(player.getUniqueId()));
        this.itemMeta = skullMeta;
        return this;
    }

    // adding the player head texture to the player head -> requires PLAYER_HEAD
    public CustomItem setSkull(String displayName, String player, String... lore) {
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(Arrays.asList(lore));
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
        this.itemMeta = skullMeta;
        return this;
    }

    public CustomItem createCustomSkull(String DisplayName, String url, String... lore) {
        /*
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        headMeta.setDisplayName(DisplayName);
        headMeta.setLore(Arrays.asList(lore));
        this.itemMeta = headMeta;
        return this;
         */
        return null;
    }

    // setting the Name of the Item
    public CustomItem setName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    // setting the Lore of the Item
    public CustomItem setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    // setting the Color of Leather Armor
    public CustomItem setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) this.itemMeta;
        meta.setColor(color);
        return this;
    }

    // adding a PotionEffect to your Potion -> requires POTION
    public CustomItem addPotionEffect(PotionEffectType effect, int duration, int amplifier, Color PotionColor) {
        PotionMeta meta = (PotionMeta) this.itemMeta;
        meta.addCustomEffect(new PotionEffect(effect, duration, amplifier), false);
        meta.setColor(PotionColor);
        return this;
    }

    // setting a PotionEffect to your Potion -> requires POTION
    public CustomItem setPotionEffect(PotionEffectType effect, int duration, int amplifier, Color PotionColor) {
        PotionMeta meta = (PotionMeta) this.itemMeta;
        meta.addCustomEffect(new PotionEffect(effect, duration, amplifier), true);
        meta.setColor(PotionColor);
        return this;
    }

    // setting the item Unbreackable
    public CustomItem setUnbreakable(boolean isUnbreakable) {
        this.itemMeta.setUnbreakable(isUnbreakable);
        return this;
    }

    // setting the Amount of your Item
    public CustomItem setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    // adding ItemFlags to your Item
    public CustomItem addItemFlag(ItemFlag itemFlag) {
        this.itemMeta.addItemFlags(itemFlag);
        return this;
    }

    // removing ItemFlags from your Item
    public CustomItem removeItemFlag(ItemFlag itemFlag) {
        this.itemMeta.removeItemFlags(itemFlag);
        return this;
    }

    // creating the Item
    public ItemStack toItemStack() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }
}
