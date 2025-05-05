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
package de.datenente.cyberente.listeners;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.ItemStack2Base64;
import de.datenente.cyberente.utils.Message;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void onPerWorldInventory(PlayerChangedWorldEvent changedWorldEvent) {
        Player player = changedWorldEvent.getPlayer();
        StorageConfig storageConfig = StorageConfig.getInstance();

        String world = player.getWorld().getName();
        String fromWorld = changedWorldEvent.getFrom().getName();

        Inventory inventory = player.getInventory();

        // Inventory speichern
        String base64 = ItemStack2Base64.itemStackArrayToBase64(inventory.getContents());
        storageConfig.setPlayerInventory(player.getUniqueId(), fromWorld, base64, player.getLevel(), player.getExp());

        // Inventory leeren
        inventory.clear();
        player.setLevel(0);
        player.setExp(0);

        // Inventory laden
        StorageObject.PlayerInventory inventoryNew = storageConfig.getPlayerInventory(player.getUniqueId(), world);
        if (inventoryNew == null) {
            CyberEnte.getInstance()
                    .getLogger()
                    .warning("No inventory found for player " + player.getName() + " in world " + world);
            return;
        }

        ItemStack[] contents = ItemStack2Base64.itemStackArrayFromBase64(inventoryNew.getBase64());
        inventory.setContents(contents);
        player.setLevel(inventoryNew.getLevel());
        player.setExp(inventoryNew.getXp());
    }

    @EventHandler
    public void onPerWorldMode(PlayerChangedWorldEvent changedWorldEvent) {
        Player player = changedWorldEvent.getPlayer();
        String world = player.getWorld().getName();

        if (world.endsWith("world")) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
            player.setGameMode(GameMode.SURVIVAL);

            Message.send(player, "Erfolgreich in <rainbow> world </rainbow> teleportiert!");
        }

        if (world.endsWith("_moon")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, PotionEffect.INFINITE_DURATION, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 2));
            player.setGameMode(GameMode.CREATIVE);

            Message.send(player, "Erfolgreich in <rainbow> moon </rainbow> teleportiert!");
        }

        if (world.endsWith("_mars")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 1));
            player.setGameMode(GameMode.CREATIVE);

            Message.send(player, "Erfolgreich in <rainbow> moon </rainbow> teleportiert!");
        }
    }
}
