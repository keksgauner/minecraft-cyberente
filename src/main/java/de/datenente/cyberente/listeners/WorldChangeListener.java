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

import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.ItemStack2Base64;
import de.datenente.cyberente.utils.Message;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void worldChangeEvent(PlayerChangedWorldEvent changedWorldEvent) {
        Player player = changedWorldEvent.getPlayer();
        String world = changedWorldEvent.getPlayer().getWorld().getName();

        StorageConfig config = StorageConfig.getInstance();

        World oldWorld = changedWorldEvent.getFrom();
        String inv =
                ItemStack2Base64.itemStackArrayToBase64(player.getInventory().getContents());
        config.getStorage()
                .getPlayerInventory()
                .put(
                        player.getUniqueId() + ":" + oldWorld.getName(),
                        new StorageObject.PlayerInventory(inv, player.getLevel()));

        player.getInventory().clear();
        player.setLevel(0);

        StorageObject.PlayerInventory invNew = config.getStorage()
                .getPlayerInventory()
                .getOrDefault(player.getUniqueId() + ":" + world, new StorageObject.PlayerInventory("", 0));
        player.getInventory().setContents(ItemStack2Base64.itemStackArrayFromBase64(invNew.getBase64()));
        player.setLevel(invNew.getXp());

        if (world.equals("world")) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
            player.setGameMode(GameMode.SURVIVAL);

            player.sendMessage(Message.text("Erfolgreich in <rainbow> world </rainbow> teleportiert!"));
        }

        if (world.equals("world_moon")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, PotionEffect.INFINITE_DURATION, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 2));
            player.setGameMode(GameMode.CREATIVE);

            player.sendMessage(Message.text("Erfolgreich in <rainbow> moon </rainbow> teleportiert!"));
        }

        if (world.equals("world_mars")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 2));
            player.setGameMode(GameMode.CREATIVE);

            player.sendMessage(Message.text("Erfolgreich in <rainbow> moon </rainbow> teleportiert!"));
        }
    }
}
