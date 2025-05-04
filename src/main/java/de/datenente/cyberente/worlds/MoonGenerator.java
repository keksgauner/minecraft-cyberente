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
package de.datenente.cyberente.worlds;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

public class MoonGenerator extends ChunkGenerator {

    // Moon generator
    // Using: https://www.spigotmc.org/threads/1-17-1-world-generator-api.521870/
    // Outdated: https://bukkit.fandom.com/wiki/Developing_a_World_Generator_Plugin

    @Override
    public void generateNoise(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.008);

        int minY = chunkData.getMinHeight();
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        for (int x = minY; x < 16; x++) {
            for (int z = minY; z < 16; z++) {

                double noise = generator.noise(worldX + x, worldZ + z, 1, 1, true);
                int height = (int) (noise * 40);
                height += 84;
                if (height > chunkData.getMaxHeight()) {
                    height = chunkData.getMaxHeight();
                }
                for (int y = 0; y < height; y++) {
                    chunkData.setBlock(x, y, z, Material.END_STONE);
                }
            }
        }
    }

    @Override
    public void generateSurface(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        super.generateSurface(worldInfo, random, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateBedrock(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            int minY = chunkData.getMinHeight();
            int maxY = 5;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int depth = 1 + random.nextInt(4);

                    for (int y = 0; y < depth; y++) {
                        int yPos = minY + y;
                        if (yPos < maxY) {
                            chunkData.setBlock(x, yPos, z, Material.BEDROCK);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void generateCaves(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        super.generateCaves(worldInfo, random, chunkX, chunkZ, chunkData);
    }
}
