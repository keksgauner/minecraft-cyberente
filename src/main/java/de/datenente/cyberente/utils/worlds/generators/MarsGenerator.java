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

import de.datenente.cyberente.utils.worlds.populator.CraterPopulator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MarsGenerator is a custom chunk generator for a Mars-like world.
 * It generates a surface with red sandstone and populates it with craters.
 */
public class MarsGenerator extends ChunkGenerator {

    // RED_SAND -> TERRACOTTA -> RED_SANDSTONE

    int heightDifference = 40;

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

        PerlinOctaveGenerator elevationNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 4);
        elevationNoiseGenerator.setScale(1 / 200.0);

        PerlinOctaveGenerator detailNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 4);
        detailNoiseGenerator.setScale(1 / 30.0);

        PerlinOctaveGenerator roughNoiseGenerator = new PerlinOctaveGenerator(worldInfo.getSeed(), 1);
        roughNoiseGenerator.setScale(1 / 100.0);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = worldX + x;
                int realZ = worldZ + z;

                double elevationTerrainNoise = elevationNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                double detailTerrainNoise = detailNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);
                double roughTerrainNoise = roughNoiseGenerator.noise(realX, realZ, 0.5, 0.5, true);

                int blockHeight = (int) Math.round(
                        (elevationTerrainNoise + detailTerrainNoise * roughTerrainNoise) * this.heightDifference);

                if (blockHeight > maxHeight) {
                    blockHeight = maxHeight;
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
                for (int y = minHeight; y < minHeight + random.nextInt(4) + 1; y++) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return Biome.DESERT;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(new CraterPopulator());
    }
}
