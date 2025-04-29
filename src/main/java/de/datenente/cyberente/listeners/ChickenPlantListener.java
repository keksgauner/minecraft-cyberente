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
