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
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

@Getter
@Setter
public class CustomItem {

    ItemStack itemStack;
    ItemFactory itemFactory;
    ItemMeta itemMeta;

    public static CustomItem of(Material material) {
        return new CustomItem(material);
    }

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

    public CustomItem displayName(Component component) {
        this.getItemMeta().displayName(component);
        return this;
    }

    public CustomItem displayName(String name) {
        this.displayName(Message.text(name));
        return this;
    }

    public CustomItem lore(Component... lore) {
        List<Component> loreList = new ArrayList<>();
        Arrays.stream(lore).forEach(l -> loreList.add(l));
        this.getItemMeta().lore(loreList);
        return this;
    }

    public CustomItem lore(String... lore) {
        List<Component> loreList = new ArrayList<>();
        Arrays.stream(lore).forEach(l -> loreList.add(Message.text(l)));
        this.getItemMeta().lore(loreList);
        return this;
    }

    public CustomItem itemModel(String key) {
        String[] splitKey = key.split(":");
        if (splitKey.length == 2) {
            this.getItemMeta().setItemModel(new NamespacedKey(splitKey[0], splitKey[1]));
            return this;
        }
        throw new IllegalStateException("Invalid item model key: " + key);
    }

    public CustomItem itemModel(NamespacedKey namespacedKey) {
        this.getItemMeta().setItemModel(namespacedKey);

        return this;
    }

    @Deprecated(since = "1.21.5")
    public CustomItem customModelData(int id) {
        this.getItemMeta().setCustomModelData(id);
        return this;
    }

    public CustomItem unbreakable(boolean unbreakable) {
        this.getItemMeta().setUnbreakable(unbreakable);
        return this;
    }

    public CustomItem addEnchantment(Enchantment enchantment, int level) {
        this.getItemMeta().addEnchant(enchantment, level, false);
        return this;
    }

    public CustomItem addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.getItemMeta().addEnchant(enchantment, level, true);
        return this;
    }

    public CustomItem removeEnchantment(Enchantment enchantment) {
        this.getItemMeta().removeEnchant(enchantment);
        return this;
    }

    public boolean isPlayerHead() {
        ItemStack itemStack = this.getItemStack();
        return itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD;
    }

    public CustomItem owningPlayer(OfflinePlayer player) {
        if (this.getItemMeta() instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
            return this;
        }
        throw new IllegalStateException("Item is not a player head");
    }

    public CustomItem amount(int size) {
        this.getItemStack().setAmount(size);
        return this;
    }

    public CustomItem durability(int durability) {
        if (this.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage((short) durability);
            return this;
        }
        throw new IllegalStateException("Item is not damageable");
    }

    public CustomItem damage(int damage) {
        if (this.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damage);
            return this;
        }
        throw new IllegalStateException("Item is not damageable");
    }

    public CustomItem addDamage(int damage) {
        if (this.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damageable.getDamage() + damage);
            return this;
        }
        throw new IllegalStateException("Item is not damageable");
    }

    public CustomItem removeDamage(int damage) {
        if (this.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damageable.getDamage() - damage);
            return this;
        }
        throw new IllegalStateException("Item is not damageable");
    }

    public CustomItem potionType(PotionType potionType) {
        if (this.getItemMeta() instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(potionType);
            return this;
        }
        throw new IllegalStateException("Item is not a potion");
    }

    public CustomItem addCustomPotionEffect(PotionEffect potionEffect, boolean overwrite) {
        if (this.getItemMeta() instanceof PotionMeta potionMeta) {
            potionMeta.addCustomEffect(potionEffect, overwrite);
            return this;
        }
        throw new IllegalStateException("Item is not a potion");
    }

    public CustomItem removeCustomPotionEffect(PotionEffectType potionEffectType) {
        if (this.getItemMeta() instanceof PotionMeta potionMeta) {
            potionMeta.removeCustomEffect(potionEffectType);
            return this;
        }
        throw new IllegalStateException("Item is not a potion");
    }

    public CustomItem clearCustomPotionEffects() {
        if (this.getItemMeta() instanceof PotionMeta potionMeta) {
            potionMeta.clearCustomEffects();
            return this;
        }
        throw new IllegalStateException("Item is not a potion");
    }

    public CustomItem addItemFlags(ItemFlag... itemFlags) {
        this.getItemMeta().addItemFlags(itemFlags);
        return this;
    }

    public CustomItem hasItemFlag(ItemFlag itemFlag) {
        this.getItemMeta().hasItemFlag(itemFlag);
        return this;
    }

    public boolean isLeatherArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.LEATHER_HELMET
                || material == Material.LEATHER_CHESTPLATE
                || material == Material.LEATHER_LEGGINGS
                || material == Material.LEATHER_BOOTS);
    }

    public boolean isIronArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.IRON_HELMET
                || material == Material.IRON_CHESTPLATE
                || material == Material.IRON_LEGGINGS
                || material == Material.IRON_BOOTS);
    }

    public boolean isGoldenArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.GOLDEN_HELMET
                || material == Material.GOLDEN_CHESTPLATE
                || material == Material.GOLDEN_LEGGINGS
                || material == Material.GOLDEN_BOOTS);
    }

    public boolean isChainmailArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.CHAINMAIL_HELMET
                || material == Material.CHAINMAIL_CHESTPLATE
                || material == Material.CHAINMAIL_LEGGINGS
                || material == Material.CHAINMAIL_BOOTS);
    }

    public boolean isDiamondArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.DIAMOND_HELMET
                || material == Material.DIAMOND_CHESTPLATE
                || material == Material.DIAMOND_LEGGINGS
                || material == Material.DIAMOND_BOOTS);
    }

    public boolean isNetheriteArmor() {
        Material material = this.getItemStack().getType();
        return (material == Material.NETHERITE_HELMET
                || material == Material.NETHERITE_CHESTPLATE
                || material == Material.NETHERITE_LEGGINGS
                || material == Material.NETHERITE_BOOTS);
    }

    public CustomItem armorColor(Color color) {
        if (this.getItemMeta() instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
            return this;
        }
        throw new IllegalStateException("Item is not leather armor");
    }

    public CustomItem resetArmorColor() {
        if (this.getItemMeta() instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(this.getItemFactory().getDefaultLeatherColor());
            return this;
        }
        throw new IllegalStateException("Item is not leather armor");
    }

    public CustomItem fireworkPower(int power) {
        if (this.getItemMeta() instanceof FireworkMeta fireworkMeta) {
            fireworkMeta.setPower(power);
            return this;
        }
        throw new IllegalStateException("Item is not a firework");
    }

    public CustomItem removeFireworkEffect(int index) {
        if (this.getItemMeta() instanceof FireworkMeta fireworkMeta) {
            fireworkMeta.removeEffect(index);
            return this;
        }
        throw new IllegalStateException("Item is not a firework");
    }

    public CustomItem addFireworkEffects(FireworkEffect... effect) {
        if (this.getItemMeta() instanceof FireworkMeta fireworkMeta) {
            fireworkMeta.addEffects(effect);
            return this;
        }
        throw new IllegalStateException("Item is not a firework");
    }

    public CustomItem chargeEffect(FireworkEffect effect) {
        if (this.getItemMeta() instanceof FireworkEffectMeta fireworkEffectMeta) {
            fireworkEffectMeta.setEffect(effect);
            return this;
        }
        throw new IllegalStateException("Item is not a firework effect");
    }

    public CustomItem setBannerPattern(int i, Pattern pattern) {
        if (this.getItemMeta() instanceof BannerMeta bannerMeta) {
            bannerMeta.setPattern(i, pattern);
            return this;
        }
        throw new IllegalStateException("Item is not a banner");
    }

    public CustomItem setBannerPatterns(List<Pattern> patterns) {
        if (this.getItemMeta() instanceof BannerMeta bannerMeta) {
            bannerMeta.setPatterns(patterns);
            return this;
        }
        throw new IllegalStateException("Item is not a banner");
    }

    public CustomItem removeBannerPattern(int i) {
        if (this.getItemMeta() instanceof BannerMeta bannerMeta) {
            bannerMeta.removePattern(i);
            return this;
        }
        throw new IllegalStateException("Item is not a banner");
    }

    public CustomItem addBannerPattern(Pattern pattern) {
        if (this.getItemMeta() instanceof BannerMeta bannerMeta) {
            bannerMeta.addPattern(pattern);
            return this;
        }
        throw new IllegalStateException("Item is not a banner");
    }

    public CustomItem addBannerPatterns(Pattern... patterns) {
        if (this.getItemMeta() instanceof BannerMeta bannerMeta) {
            for (Pattern pattern : patterns) {
                bannerMeta.addPattern(pattern);
            }
            return this;
        }
        throw new IllegalStateException("Item is not a banner");
    }

    public CustomItem addBookPage(String... pages) {
        if (this.getItemMeta() instanceof WritableBookMeta writableBookMeta) {
            writableBookMeta.addPage(pages);
            return this;
        }
        throw new IllegalStateException("Item is not a book");
    }

    public CustomItem bookAuthor(String author) {
        if (this.getItemMeta() instanceof BookMeta bookMeta) {
            bookMeta.setAuthor(author);
            return this;
        }
        throw new IllegalStateException("Item is not a book");
    }

    public CustomItem setBookPage(int page, String data) {
        if (this.getItemMeta() instanceof WritableBookMeta writableBookMeta) {
            writableBookMeta.setPage(page, data);
            return this;
        }
        throw new IllegalStateException("Item is not a book");
    }

    public CustomItem bookTitle(String title) {
        if (this.getItemMeta() instanceof BookMeta bookMeta) {
            bookMeta.setTitle(title);
            return this;
        }
        throw new IllegalStateException("Item is not a book");
    }

    public CustomItem setBookPages(String... pages) {
        if (this.getItemMeta() instanceof WritableBookMeta writableBookMeta) {
            writableBookMeta.setPages(pages);
            return this;
        }
        throw new IllegalStateException("Item is not a book");
    }
}
