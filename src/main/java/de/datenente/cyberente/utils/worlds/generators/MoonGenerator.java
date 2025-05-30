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
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * A custom chunk generator for a moon-like world.
 * <p>
 * This generator creates a surface using Simplex noise and populates it with end stone.
 * It also generates bedrock at the bottom of the world.
 */
public class MoonGenerator extends ChunkGenerator {

    // END_STONE -> DEEP_SLATE_STONE

    // The octaves parameter sets the number of functions used.
    // More octaves result in a more detailed surface.
    int octaves = 6;
    // The scale parameter determines at what distance to view the surface.
    // Zoom out/zoom in.
    double scale = 0.008;
    // The frequency parameter sets how much detail each octave adds to the surface.
    // A frequency of 1 results in each octave having the same impact on the resulting surface.
    // A frequency of smaller than 1 results in later octaves generating a smoother surface
    double frequency = 1;
    // The amplitude parameter sets how much each octave contributes to the overall surface.
    // An amplitude of 1 results in each octave having the same impact on the resulting surface.
    // An amplitude of smaller than 1 results in later octaves adding smaller changes to the surface.
    // Also known as Persistence
    double amplitude = 1;
    // The height parameter sets the height difference.
    // With 15 you get a difference 15 and -15.
    double heightDifference = 40;
    // The height parameter sets the height of the surface.
    int height = 84;

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

        SimplexOctaveGenerator noiseGenerator = new SimplexOctaveGenerator(worldInfo.getSeed(), this.octaves);
        noiseGenerator.setScale(this.scale);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = worldX + x;
                int realZ = worldZ + z;

                double terrainNoise = noiseGenerator.noise(realX, realZ, frequency, amplitude, true);
                int blockHeight = (int) Math.round(terrainNoise * heightDifference);
                blockHeight += this.height;

                if (blockHeight > maxHeight) {
                    blockHeight = maxHeight;
                }
                for (int y = minHeight; y < blockHeight; y++) {
                    chunkData.setBlock(x, y, z, Material.END_STONE);
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
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(new CraterPopulator());
    }
}
