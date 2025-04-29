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
import org.bukkit.*;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChickenPlantListener implements Listener {

    @EventHandler
    public void onChickenPlant(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;

        Material mat = item.getType();
        if (mat != Material.CHICKEN && mat != Material.COOKED_CHICKEN) return;

        event.setCancelled(true);

        Location loc = event.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);
        World world = loc.getWorld();

        for (int i = 0; i < 2; i++) {
            Chicken chicken = world.spawn(loc.clone().add(0, -0.3, 0), Chicken.class, c -> {
                c.setBaby();
                c.setAgeLock(true);
                c.setAI(false);
                c.setSilent(true);
                c.setInvulnerable(true);
                c.setGravity(false);
            });

            // Wachsen simulieren
            Bukkit.getScheduler()
                    .runTaskTimer(
                            CyberEnte.getInstance(),
                            new Runnable() {
                                int tick = 0;
                                double offset = -0.3;

                                @Override
                                public void run() {
                                    if (!chicken.isValid()) return;

                                    if (tick >= 40) {
                                        chicken.setAdult();
                                        chicken.setAgeLock(false);
                                        chicken.setAI(true);
                                        chicken.setSilent(false);
                                        chicken.setInvulnerable(false);
                                        chicken.setGravity(true);
                                        chicken.getWorld()
                                                .playSound(chicken.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1f, 1f);
                                        return;
                                    }

                                    offset += 0.01;
                                    chicken.teleportAsync(loc.clone().add(0, offset, 0));
                                    tick++;
                                }
                            },
                            0L,
                            1L);
        }

        // Verbrauche das Item
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }

        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 10, 0.3, 0.5, 0.3);
    }
}
