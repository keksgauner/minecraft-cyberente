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
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

@Getter
public class OreVeinPopulator extends BlockPopulator {
    Material[] ores = {
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.DIAMOND_ORE,
        Material.REDSTONE_ORE,
        Material.LAPIS_ORE
    };
    int[] maxHeights = {128, 64, 32, 16, 16, 32};
    int[] clusterSizes = {17, 9, 9, 8, 8, 7};

    @Override
    public void populate(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull LimitedRegion limitedRegion) {

        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        for (int i = 0; i < this.getOres().length; i++) {
            Material ore = this.getOres()[i];
            int maxHeight = this.getMaxHeights()[i];
            int clusterSize = this.getClusterSizes()[i];

            int clusterCount = random.nextInt(10) + 1;

            for (int j = 0; j < clusterCount; j++) {
                int x = worldX + random.nextInt(16);
                int y = random.nextInt(maxHeight);
                int z = worldZ + random.nextInt(16);

                generateOreCluster(limitedRegion, random, x, y, z, clusterSize, ore);
            }
        }
    }

    void generateOreCluster(
            @NotNull LimitedRegion limitedRegion,
            @NotNull Random random,
            int x,
            int y,
            int z,
            int clusterSize,
            @NotNull Material ore) {

        for (int i = 0; i < clusterSize; i++) {
            int offsetX = x + random.nextInt(3) - 1;
            int offsetY = y + random.nextInt(3) - 1;
            int offsetZ = z + random.nextInt(3) - 1;

            if (limitedRegion.getType(offsetX, offsetY, offsetZ).isAir()) {
                limitedRegion.setType(offsetX, offsetY, offsetZ, ore);
            }
        }
    }
}
