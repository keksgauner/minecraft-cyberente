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
import de.datenente.cyberente.utils.Message;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChickenPlantListener implements Listener {

    @EventHandler
    public void onChickenPlant(PlayerInteractEvent interactEvent) {
        if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = interactEvent.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.FARMLAND) return;

        ItemStack item = interactEvent.getItem();
        if (item == null) return;
        Material material = item.getType();
        if (material != Material.CHICKEN && material != Material.COOKED_CHICKEN) return;

        interactEvent.setCancelled(true);

        Location location = clickedBlock.getLocation().add(0.5, 1.0, 0.5);
        World world = location.getWorld();
        Player player = interactEvent.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }

        world.spawnParticle(Particle.HAPPY_VILLAGER, location, 10, 0.3, 0.5, 0.3);

        Chicken chicken = world.spawn(location.clone().add(0, -0.3, 0), Chicken.class, c -> {
            c.setBaby();
            c.setAgeLock(true);
            c.setAI(false);
            c.setSilent(true);
            c.setInvulnerable(true);
            c.setGravity(false);
        });

        // Wachsen simulieren
        AtomicReference<ScheduledFuture<?>> futureRef = new AtomicReference<>();

        ScheduledFuture<?> futureTask = CyberEnte.getInstance()
                .getScheduledExecutorService()
                .scheduleWithFixedDelay(
                        new Runnable() {
                            int tick = 0;
                            double offset = -0.3;

                            @Override
                            public void run() {
                                if (!chicken.isValid()) {
                                    futureRef.get().cancel(true);
                                    return;
                                }
                                if (tick >= 13) {
                                    // Task syncron laufen lassen
                                    CyberEnte.getInstance()
                                            .getScheduledExecutorService()
                                            .schedule(
                                                    () -> {
                                                        if (!chicken.isValid()) return;
                                                        try {
                                                            chicken.teleportAsync(clickedBlock
                                                                            .getLocation()
                                                                            .add(0, 1, 0))
                                                                    .get();
                                                        } catch (final InterruptedException | ExecutionException ex) {
                                                            Message.send(
                                                                    player,
                                                                    "<red>An error occurred while trying to teleport!</red>");
                                                        }
                                                        chicken.setAdult();
                                                        chicken.setAgeLock(false);
                                                        chicken.setAI(true);
                                                        chicken.setSilent(false);
                                                        chicken.setInvulnerable(false);
                                                        chicken.setGravity(true);
                                                        world.playSound(
                                                                chicken.getLocation(),
                                                                Sound.ENTITY_CHICKEN_AMBIENT,
                                                                1f,
                                                                1f);
                                                    },
                                                    100,
                                                    TimeUnit.MILLISECONDS);
                                    futureRef.get().cancel(true);
                                    return;
                                }

                                offset += 0.01;
                                try {
                                    chicken.teleportAsync(location.clone().add(0, offset, 0))
                                            .get();
                                } catch (final InterruptedException | ExecutionException ex) {
                                    Message.send(player, "<red>An error occurred while trying to teleport!</red>");
                                }
                                tick++;
                            }
                        },
                        0L,
                        1L,
                        TimeUnit.SECONDS);

        futureRef.set(futureTask);
    }
}
