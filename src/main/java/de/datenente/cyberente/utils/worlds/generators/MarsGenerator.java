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

import de.datenente.cyberente.utils.worlds.biome.SimpleBiomeProvider;
import de.datenente.cyberente.utils.worlds.populator.CraterPopulator;
import de.datenente.cyberente.utils.worlds.populator.FloraPopulator;
import de.datenente.cyberente.utils.worlds.populator.OreVeinPopulator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarsGenerator extends ChunkGenerator {

    // RED_SAND -> TERRACOTTA -> RED_SANDSTONE

    public float lerp(float min, float max, float norm){
        return (max - min) * norm + min;
    }

    SimplexOctaveGenerator generator;

    @Override
    public void generateNoise(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        this.generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), this.octaves);
        this.generator.setScale(this.scale);
    }

    @Override
    public void generateSurface(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull ChunkData chunkData) {
        int currentHeight;

        ChunkGenerator.ChunkData chunk = createChunkData(world);

        SimplexOctaveGenerator terrainGen = new SimplexOctaveGenerator(new Random(world.getSeed()), 2);

        terrainGen.setScale(0.0056D);

        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {

                float terrainNoise = (float) (terrainGen.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.05D, 0.8D, true));

                currentHeight = (int) ( lerp(0f, 1f, terrainNoise) * 15D + 60D);

                if (currentHeight > 100) currentHeight = 100;
                if(currentHeight < 16) currentHeight = 16;


                chunk.setBlock(X, currentHeight, Z, Material.DEAD_BRAIN_CORAL_BLOCK);
                if (random.nextInt(100) < 90) {
                    chunk.setBlock(X, currentHeight - 1, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);
                } else {
                    chunk.setBlock(X, currentHeight - 1, Z, Material.DEAD_HORN_CORAL_BLOCK);
                }
                chunk.setBlock(X, currentHeight - 2, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);
                chunk.setBlock(X, currentHeight - 3, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);

                for (int i = 0; i < currentHeight - 3; i++) {
                    if (i > currentHeight * 0.8) {
                        chunk.setBlock(X, i, Z, Material.DEAD_FIRE_CORAL_BLOCK);
                    } else if (i > currentHeight * 0.6) {
                        chunk.setBlock(X, i, Z, Material.DEAD_TUBE_CORAL_BLOCK);
                    } else if (i > currentHeight * 0.4) {
                        chunk.setBlock(X, i, Z, Material.COBBLESTONE);
                    } else if (i > currentHeight * 0.1) {
                        chunk.setBlock(X, i, Z, Material.PACKED_ICE);
                    } else {
                        chunk.setBlock(X, i, Z, Material.BLUE_ICE);
                    }
                    if (i <= 2){
                        if(random.nextBoolean() && random.nextBoolean() && random.nextBoolean())
                            chunk.setBlock(X, i, Z, Material.BEDROCK);
                    }

                }
                chunk.setBlock(X, 0, Z, Material.BEDROCK);

                biome.setBiome(X, Z, Biome.DESERT);
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
                int depth = 1 + random.nextInt(2);

                for (int i = 0; i < depth; i++) {
                    int y = minHeight + i;
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new SimpleBiomeProvider();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(new OreVeinPopulator(), new CraterPopulator(), new FloraPopulator());
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
