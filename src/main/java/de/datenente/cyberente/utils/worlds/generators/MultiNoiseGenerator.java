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

import de.datenente.cyberente.utils.worlds.biome.TemperatureProvider;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// This is a Test Class for a MultiNoise
// With continentals, erosion, peaks & valleys, temperature and humidity
// See: https://minecraft.wiki/w/World_generation
// https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fminecraft-noise-maps-and-how-do-they-generate-v0-8ddpi96i5lkd1.png%3Fwidth%3D890%26format%3Dpng%26auto%3Dwebp%26s%3D82c44cb3871f2f529dae90e4930314063adb062c
// https://auburn.github.io/FastNoiseLite/
// https://www.geogebra.org/graphing/yzgxvd8q
// https://www.desmos.com/calculator/otr2abiuns
// https://adrianb.io/2014/08/09/perlinnoise.html
/**
 * MultiNoiseGenerator
 *
 * <p>Generates a world with a multi-noise generator.
 *
 * <p>Uses the following noise generators:
 *
 * <ul>
 *   <li>Continentalness
 *   <li>Erosion
 *   <li>Peaks & Valleys
 *   <li>Temperature
 *   <li>Humidity
 * </ul>
 *
 * <p>Continentalness: Steuert die Form der Kontinente (z.B. wie viele Berge)
 * Erosion: Steuert die Glätte der Landschaft (z.B. wie viele Täler)
 * Peaks & Valleys: Hebt/tieft extreme Landschaftsformen (z.B. Gebirge)
 * Temperature: Steuert die Temperatur (z.B. Eis, Wüste)
 * Humidity: Steuert die Feuchtigkeit (z.B. Regenwald, Wüste)
 *
 * @author KeksGauner
 */
public class MultiNoiseGenerator extends ChunkGenerator {
    @Override
    public void generateNoise(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        SimplexOctaveGenerator continentals = new SimplexOctaveGenerator(worldInfo.getSeed(), 4);
        SimplexOctaveGenerator erosion = new SimplexOctaveGenerator(worldInfo.getSeed(), 4);
        SimplexOctaveGenerator peaksValleys = new SimplexOctaveGenerator(worldInfo.getSeed(), 4);

        continentals.setScale(0.0015); // größere Kontinente
        erosion.setScale(0.001); // tiefe Schluchten
        peaksValleys.setScale(0.02); // kleine Zacken

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = baseX + x;
                int worldZ = baseZ + z;

                double cont = continentals.noise(worldX, worldZ, 0.5, 1.0); // -1 to 1
                double eros = erosion.noise(worldX, worldZ, 0.5, 1.0); // -1 to 1
                double peaks = peaksValleys.noise(worldX, worldZ, 0.5, 1.0); // -1 to 1

                double height = cont * 40 + peaks * 25 - eros * 30 + 64;
                int maxY = Math.max(1, Math.min(255, (int) height));

                for (int y = 0; y <= maxY; y++) {
                    Material mat = Material.STONE;
                    if (y == maxY) mat = Material.GRASS_BLOCK; // Oberfläche
                    chunkData.setBlock(x, y, z, mat);
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
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = baseX + x;
                int worldZ = baseZ + z;

                // Finde die oberste Stein-Schicht
                int surfaceY = getHighestBlockY(chunkData, x, z);

                Biome biome = chunkData.getBiome(x, surfaceY, z);

                Material topBlock;
                Material underBlock = Material.DIRT;
                if (biome == Biome.DESERT) topBlock = Material.SAND;
                else if (biome == Biome.SWAMP) topBlock = Material.MUD;
                else if (biome == Biome.FOREST) topBlock = Material.GRASS_BLOCK;
                else if (biome == Biome.SNOWY_PLAINS) topBlock = Material.SNOW_BLOCK;
                else if (biome == Biome.BEACH) topBlock = Material.SAND;
                else if (biome == Biome.PLAINS) topBlock = Material.GRASS_BLOCK;
                else topBlock = Material.GRASS_BLOCK;

                // Setze Oberflächenschichten
                chunkData.setBlock(x, surfaceY, z, topBlock);
                for (int y = surfaceY - 1; y > surfaceY - 4 && y >= 0; y--) {
                    if (chunkData.getType(x, y, z) == Material.STONE) chunkData.setBlock(x, y, z, underBlock);
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
    public void generateCaves(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        SimplexOctaveGenerator caveNoise = new SimplexOctaveGenerator(worldInfo.getSeed(), 3);
        caveNoise.setScale(0.05);

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 5; y < 60; y++) {
                    int worldX = baseX + x;
                    int worldZ = baseZ + z;

                    double noise = caveNoise.noise(worldX, y, worldZ, 0.5, 0.5);

                    if (noise > 0.4) {
                        chunkData.setBlock(x, y, z, Material.AIR);
                    }
                }
            }
        }
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new TemperatureProvider();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of();
    }

    int getHighestBlockY(ChunkData chunkData, int x, int z) {
        for (int y = 255; y >= 0; y--) {
            if (chunkData.getType(x, y, z).isSolid()) return y;
        }
        return 0;
    }
}
