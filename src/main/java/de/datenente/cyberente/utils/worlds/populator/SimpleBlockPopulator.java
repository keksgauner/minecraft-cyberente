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

public class SimpleBlockPopulator extends BlockPopulator {

    private static final BlockFace[] OFFSETS = new BlockFace[] {
        BlockFace.NORTH,
        BlockFace.EAST,
        BlockFace.SOUTH,
        BlockFace.WEST,
        BlockFace.UP,
        BlockFace.DOWN,
        BlockFace.NORTH_EAST,
        BlockFace.NORTH_WEST,
        BlockFace.SOUTH_EAST,
        BlockFace.SOUTH_WEST,
    };

    @Override
    public void populate(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull LimitedRegion limitedRegion) {
        int startX = random.nextInt(16) + (chunkX * 16);
        int startY = random.nextInt(30) + worldInfo.getMinHeight();
        int startZ = random.nextInt(16) + (chunkZ * 16);
        Location location = new Location(null, startX, startY, startZ);

        if (!limitedRegion.isInRegion(location)) {
            return;
        }

        Material startMaterial = limitedRegion.getType(location);

        if (startMaterial != Material.STONE && startMaterial != Material.DEEPSLATE) {
            return;
        }

        for (int seize = random.nextInt(15); seize > 0; seize--) {
            if (limitedRegion.isInRegion(location)) {
                limitedRegion.setType(location, Material.ANCIENT_DEBRIS);
            }

            BlockFace blockFace = OFFSETS[random.nextInt(OFFSETS.length)];
            location.add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        }
    }
}
