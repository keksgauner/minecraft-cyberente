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
import org.jetbrains.annotations.NotNull;

public class MoonGenerator extends ChunkGenerator {
    @Override
    public void generateNoise(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        super.generateNoise(worldInfo, random, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateSurface(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        int baseHeight = 64; // Standardhöhe der Mondoberfläche

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // Leichte Höhenunterschiede
                int height = baseHeight + random.nextInt(3) - 1; // 63–65

                double noise = Math.sin(worldX * 0.05) + Math.cos(worldZ * 0.05) + random.nextDouble();
                if (noise > 1.8) {
                    height -= 4 + random.nextInt(3); // Tieferer Krater
                }

                // Mondoberflächenblöcke
                chunkData.setBlock(x, height, z, Material.END_STONE);

                // Untere Schichten mit Stein oder ähnlichem auffüllen
                for (int y = height - 1; y > 0; y--) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
            }
        }
    }

    @Override
    public void generateBedrock(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        super.generateBedrock(worldInfo, random, chunkX, chunkZ, chunkData);
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
