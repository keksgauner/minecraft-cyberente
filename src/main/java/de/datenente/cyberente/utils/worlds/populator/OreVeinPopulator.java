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
package de.datenente.cyberente.utils.worlds.populator;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class OreVeinPopulator extends BlockPopulator {

    private int randomIntBetween(int min, int max) {
        if (min >= max) {
            Random r = new Random();
            return r.nextInt((min - max) + 1) + max;
        } else {
            Random r = new Random();
            return r.nextInt((max - min) + 1) + min;
        }
    }

    @Override
    public void populate(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull LimitedRegion limitedRegion) {
        int amount = randomIntBetween(20, 60);
        for (int i = 1; i < amount; i++) {
            int maxY = 60;
            int X = random.nextInt(15);
            int Z = random.nextInt(15);

            for (int j = world.getMaxHeight() - 1; chunk.getBlock(X, j, Z).getType() == Material.AIR; j--) maxY = j;

            int maxVeinY = maxY - 4;
            int Y;
            Material ore;
            int propagation;
            int orePicker = random.nextInt(99) + 1;
            if (orePicker > 90) {
                ore = Material.DIAMOND_ORE;
                propagation = randomIntBetween(0, 3);
                Y = randomIntBetween(1, 30);
            } else if (orePicker > 65) {
                ore = Material.GOLD_ORE;
                propagation = randomIntBetween(1, 4);
                Y = randomIntBetween(3, (int) (maxVeinY * 0.6));
            } else if (orePicker > 50) {
                ore = Material.REDSTONE_ORE;
                propagation = randomIntBetween(2, 5);
                Y = randomIntBetween(1, (int) (maxVeinY * 0.4));
            } else if (orePicker > 35) {
                ore = Material.IRON_ORE;
                propagation = randomIntBetween(5, 10);
                Y = randomIntBetween(3, (int) (maxVeinY * 0.9));
            } else if (orePicker > 25) {
                ore = Material.LAPIS_ORE;
                propagation = randomIntBetween(5, 10);
                Y = randomIntBetween(3, (int) (maxVeinY * 0.5));
            } else {
                ore = Material.COAL_ORE;
                propagation = randomIntBetween(5, 10);
                Y = randomIntBetween(1, maxVeinY);
            }

            if (orePicker == 6) {
                ore = Material.EMERALD_ORE;
                propagation = 1;
                Y = randomIntBetween(1, (int) (maxVeinY * 0.2));
            }

            Block b = world.getBlockAt((chunk.getX() << 4) + X, Y, (chunk.getZ() << 4) + Z);

            if (b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR))
                b.setType(ore, false);

            int propX = (chunk.getX() << 4) + X;
            int propZ = (chunk.getZ() << 4) + Z;
            for (int prop = 0; prop < propagation + 1; prop++) {
                try {
                    b = world.getBlockAt(propX, Y, propZ);

                    if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR))
                        b.setType(ore, false);

                    orePicker = random.nextInt(2);
                    if (orePicker == 0) {
                        propX = randomIntBetween(propX - 1, propX + 1);
                    } else if (orePicker == 1) {
                        Y = randomIntBetween(Y - 1, Y + 1);
                        if (Y < 1) Y = 1;
                    } else if (orePicker == 2) {
                        propZ = randomIntBetween(propZ - 1, propZ + 1);
                    }
                } catch (Exception e) {
                }

            }
        }
    }
}
