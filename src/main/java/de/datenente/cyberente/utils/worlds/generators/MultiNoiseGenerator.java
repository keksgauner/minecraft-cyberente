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
package de.datenente.cyberente.utils.worlds.generators;

import de.datenente.cyberente.utils.worlds.biome.DesertProvider;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Delete on release
public class MultiNoiseGenerator extends ChunkGenerator {

    PerlinOctaveGenerator regionNoiseGenerator;
    PerlinOctaveGenerator elevationNoiseGenerator;
    PerlinOctaveGenerator detailNoiseGenerator;
    PerlinOctaveGenerator roughNoiseGenerator;

    @Override
    public void generateSurface(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        long seed = worldInfo.getSeed();
        int worldX, worldZ;

        SimplexOctaveGenerator continentalness = new SimplexOctaveGenerator(seed, 2);
        continentalness.setScale(0.002);

        SimplexOctaveGenerator erosion = new SimplexOctaveGenerator(seed, 3);
        erosion.setScale(0.004);

        SimplexOctaveGenerator peaksValleys = new SimplexOctaveGenerator(seed, 4);
        peaksValleys.setScale(0.003);

        SimplexOctaveGenerator temperature = new SimplexOctaveGenerator(seed, 2);
        temperature.setScale(0.01);

        SimplexOctaveGenerator humidity = new SimplexOctaveGenerator(seed + 1000, 2);
        humidity.setScale(0.01);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                worldX = chunkX * 16 + x;
                worldZ = chunkZ * 16 + z;

                double cont = continentalness.noise(worldX, worldZ, 0.6, 0.6);
                double eros = erosion.noise(worldX, worldZ, 0.6, 0.6);
                double peak = peaksValleys.noise(worldX, worldZ, 0.6, 0.6);
                double temp = temperature.noise(worldX, worldZ, 0.6, 0.6);
                double hum = humidity.noise(worldX, worldZ, 0.6, 0.6);

                // Höhenberechnung – Werte von ca. 20 bis 140
                double terrainHeight = cont * 40 + peak * 35 - eros * 25 + 70;
                int height = Math.max(10, Math.min((int) terrainHeight, 150));

                // Untergrund
                for (int y = 1; y < height - 4; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }

                // Übergangsschichten
                chunkData.setBlock(x, height - 3, z, Material.DIRT);
                chunkData.setBlock(x, height - 2, z, Material.DIRT);
                chunkData.setBlock(x, height - 1, z, Material.DIRT);

                // Oberfläche abhängig von Klima
                Material top;
                if (temp < -0.3) {
                    top = Material.SNOW_BLOCK;
                } else if (temp > 0.6 && hum < 0.2) {
                    top = Material.SAND;
                } else if (hum > 0.7) {
                    top = Material.MYCELIUM;
                } else {
                    top = Material.GRASS_BLOCK;
                }

                chunkData.setBlock(x, height, z, top);
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
        if (chunkData.getMinHeight() != worldInfo.getMinHeight()) {
            return;
        }
        int minHeight = chunkData.getMinHeight();
        SimplexOctaveGenerator noise = new SimplexOctaveGenerator(worldInfo.getSeed(), 1);
        noise.setScale(0.25);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = (chunkX << 4) + x;
                int worldZ = (chunkZ << 4) + z;

                double raw = noise.noise(worldX, worldZ, 0.6, 0.6);
                int height = (int) ((raw + 1) * 2.5);
                height = Math.max(0, Math.min(5, height));

                for (int y = minHeight; y <= minHeight + height; y++) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new DesertProvider();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of();
    }
}
