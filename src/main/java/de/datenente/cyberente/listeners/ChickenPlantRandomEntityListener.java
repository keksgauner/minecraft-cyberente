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
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChickenPlantRandomEntityListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onChickenPlant(PlayerInteractEvent interactEvent) {
        if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = interactEvent.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.FARMLAND) return;

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

        // Wachstumsanimation
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
                                    Bukkit.getScheduler().runTask(CyberEnte.getInstance(), () -> {
                                        if (!chicken.isValid()) return;

                                        chicken.teleport(
                                                clickedBlock.getLocation().add(0, 1, 0));
                                        chicken.setAdult();
                                        chicken.setAgeLock(false);
                                        chicken.setAI(true);
                                        chicken.setSilent(false);
                                        chicken.setInvulnerable(false);
                                        chicken.setGravity(true);
                                        world.playSound(chicken.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1f, 1f);

                                        // Jetzt: Struktur mit Loot generieren!
                                        spawnRandomEntityOrEffect(
                                                world,
                                                clickedBlock.getLocation().add(0, 1, 0));
                                    });
                                    futureRef.get().cancel(true);
                                    return;
                                }

                                offset += 0.01;
                                chicken.teleportAsync(location.clone().add(0, offset, 0));
                                tick++;
                            }
                        },
                        0L,
                        1L,
                        TimeUnit.SECONDS);

        futureRef.set(futureTask);
    }

    private void spawnRandomEntityOrEffect(World world, Location location) {
        int roll = random.nextInt(5); // kannst du anpassen

        switch (roll) {
            case 0 -> world.spawnEntity(location, EntityType.RABBIT);
            case 1 -> world.strikeLightningEffect(location);
            case 2 -> world.spawnParticle(Particle.EXPLOSION, location, 1);
            case 3 -> buildSpecialStructureWithLoot(world, location); // unsere Struktur
            default -> world.spawnParticle(Particle.NOTE, location, 10, 0.5, 0.5, 0.5);
        }
    }

    private void buildSpecialStructureWithLoot(World world, Location origin) {
        Location base = origin.clone().getBlock().getLocation();

        // Fundament 3x3
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.getBlockAt(base.clone().add(x, 0, z)).setType(Material.STONE_BRICKS);
            }
        }

        // Säulen
        world.getBlockAt(base.clone().add(-1, 1, -1)).setType(Material.COBBLESTONE_WALL);
        world.getBlockAt(base.clone().add(1, 1, -1)).setType(Material.COBBLESTONE_WALL);
        world.getBlockAt(base.clone().add(-1, 1, 1)).setType(Material.COBBLESTONE_WALL);
        world.getBlockAt(base.clone().add(1, 1, 1)).setType(Material.COBBLESTONE_WALL);

        // Dach
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.getBlockAt(base.clone().add(x, 2, z)).setType(Material.MOSSY_STONE_BRICKS);
            }
        }

        // Kiste mit Loot
        Block chestBlock = world.getBlockAt(base.clone().add(0, 1, 0));
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest chest) {
            chest.getInventory()
                    .addItem(
                            new ItemStack(Material.DIAMOND, 3),
                            new ItemStack(Material.GOLDEN_APPLE, 1),
                            new ItemStack(Material.EXPERIENCE_BOTTLE, 8),
                            createEpicLoot());
            chest.setCustomName("§6CyberEnte-Schatz");
            chest.update();
        }

        // Effekt
        world.spawnParticle(Particle.ENCHANT, base.clone().add(0.5, 1.5, 0.5), 30, 0.5, 0.5, 0.5);
        world.playSound(base, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }

    private ItemStack createEpicLoot() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§bCyber-Klinge");
            meta.setLore(List.of("§7Eine legendäre Klinge,", "§7geschmiedet vom Huhn der Tiefe."));
            meta.addEnchant(Enchantment.POWER, 5, true);
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            sword.setItemMeta(meta);
        }
        return sword;
    }
}
