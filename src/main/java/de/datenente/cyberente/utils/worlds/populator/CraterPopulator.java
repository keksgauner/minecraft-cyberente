package de.datenente.cyberente.utils.worlds.populator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CraterPopulator  extends BlockPopulator {
    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        int CRATER_CHANCE = 1; // Out of 100
        int MIN_CRATER_SIZE = 3;
        int SMALL_CRATER_SIZE = 10;
        int BIG_CRATER_SIZE = 24;
        int BIG_CRATER_CHANCE = 3; // Out of 100

        if (random.nextInt(100) <= CRATER_CHANCE) {
            int centerX = (source.getX() << 4) + random.nextInt(15);
            int centerZ = (source.getZ() << 4) + random.nextInt(15);
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            org.bukkit.util.Vector center = new BlockVector(centerX, centerY, centerZ);
            int radius;

            if (random.nextInt(100) <= BIG_CRATER_CHANCE) {
                radius = random.nextInt(BIG_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            } else {
                radius = random.nextInt(SMALL_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            }

            Material powder = Material.GRAY_CONCRETE_POWDER;

            if (random.nextBoolean())
                powder = Material.BLACK_CONCRETE_POWDER;

            if (random.nextBoolean())
                powder = Material.GRAVEL;

            final Material blockType = powder;


            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        org.bukkit.util.Vector position = center.clone().add(new org.bukkit.util.Vector(x, y, z));

                        if (center.distance(position) <= radius + 0.5) {
                            if (!world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 1, (int) position.toLocation(world).getZ()).getType().equals(Material.BEDROCK)) {
                                world.getBlockAt(position.toLocation(world)).setType(Material.AIR, false);
                            }
                        }
                    }
                    for (int z = -radius; z <= radius; z++) {
                        org.bukkit.util.Vector position = center.clone().add(new Vector(x, y, z));
                        if (center.distance(position) <= radius + 0.5) {
                            int xx = world.getBlockAt(position.toLocation(world)).getX();
                            int yy = world.getBlockAt(position.toLocation(world)).getY();
                            int zz = world.getBlockAt(position.toLocation(world)).getZ();
                            if (y < radius / 2 && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 1, (int) position.toLocation(world).getZ()).getType().equals(Material.AIR) && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 1, (int) position.toLocation(world).getZ()).getType().equals(powder) && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 1, (int) position.toLocation(world).getZ()).getType().equals(Material.AIR)
                                    && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 1, (int) position.toLocation(world).getZ()).getType().equals(Material.BEDROCK)) {

                                world.getBlockAt(xx, yy - 1, zz).setType(blockType, false);
                            }


                            if (!world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 2, (int) position.toLocation(world).getZ()).getType().equals(Material.AIR) && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 2, (int) position.toLocation(world).getZ()).getType().equals(powder) && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 2, (int) position.toLocation(world).getZ()).getType().equals(Material.AIR)
                                    && !world.getBlockAt((int) position.toLocation(world).getX(), (int) position.toLocation(world).getY() - 2, (int) position.toLocation(world).getZ()).getType().equals(Material.BEDROCK)) {
                                world.getBlockAt(xx, yy - 2, zz).setType(blockType, false);
                            }


                        }
                    }
                }
            }
        }
    }
}
