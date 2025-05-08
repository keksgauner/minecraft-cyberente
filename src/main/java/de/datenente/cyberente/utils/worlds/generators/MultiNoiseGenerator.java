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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Delete on release
public class MultiNoiseGenerator extends ChunkGenerator {

    int heightDifference = 40;

    PerlinOctaveGenerator regionNoiseGenerator;
    PerlinOctaveGenerator elevationNoiseGenerator;
    PerlinOctaveGenerator detailNoiseGenerator;
    PerlinOctaveGenerator roughNoiseGenerator;

    PerlinOctaveGenerator bedrockNoiseGenerator;

    @Override
    public void generateNoise(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {

        this.regionNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 2);
        this.regionNoiseGenerator.setScale(1 / 500.0);

        this.elevationNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 4);
        this.elevationNoiseGenerator.setScale(1 / 200.0);

        this.detailNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 4);
        this.detailNoiseGenerator.setScale(1 / 30.0);

        this.roughNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 1);
        this.roughNoiseGenerator.setScale(1 / 100.0);

        this.bedrockNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 1);
        this.bedrockNoiseGenerator.setScale(1 / 100.0);
    }

    @Override
    public void generateSurface(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        int maxHeight = chunkData.getMaxHeight();
        int minHeight = chunkData.getMinHeight();
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = worldX + x;
                int realZ = worldZ + z;

                double regionTerrainNoise = this.regionNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                double elevationTerrainNoise = this.elevationNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                double detailTerrainNoise = this.detailNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                double roughTerrainNoise = this.roughNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);

                int blockHeight = (int) Math.round(
                        (elevationTerrainNoise + detailTerrainNoise * roughTerrainNoise) * this.heightDifference);

                // Berge (regionNoise > 0.6) und Klüfte (regionNoise < -0.6)
                if (regionTerrainNoise > 0.6) {
                    blockHeight += 20; // Berge erhöhen
                } else if (regionTerrainNoise < -0.6) {
                    blockHeight -= 20; // Klüfte absenken
                }

                if (blockHeight > maxHeight) {
                    blockHeight = maxHeight;
                } else if (blockHeight < minHeight) {
                    blockHeight = minHeight;
                }

                for (int y = minHeight; y < blockHeight; y++) {
                    chunkData.setBlock(x, y, z, Material.RED_SANDSTONE);
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

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = chunkX * 16 + x;
                int realZ = chunkZ * 16 + z;
                double bedrockNoise = this.bedrockNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                int blockHeight = (int) Math.round(bedrockNoise * this.heightDifference);
                if (blockHeight > chunkData.getMaxHeight()) {
                    blockHeight = chunkData.getMaxHeight();
                }
                for (int y = minHeight; y < blockHeight; y++) {
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

    /*
    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
       Location spawnLocation = new Location(world, 0.5D, 64, 0.5D);
        Location blockLocation = spawnLocation.clone().subtract(0D, 1D, 0D);
        blockLocation.getBlock().setType(Material.BEDROCK);
        return spawnLocation;
    }
     */
}
