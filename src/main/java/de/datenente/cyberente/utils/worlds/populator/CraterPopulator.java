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
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class CraterPopulator extends BlockPopulator {

    @Override
    public void populate(
            @NotNull WorldInfo worldInfo,
            @NotNull Random random,
            int chunkX,
            int chunkZ,
            @NotNull LimitedRegion limitedRegion) {
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        // Wahrscheinlichkeit für einen Krater (z. B. 5%)
        double craterProbability = 0.05;

        // Prüfen, ob ein Krater erstellt werden soll
        if (random.nextDouble() < craterProbability) {
            // Zufällige Position im Chunk
            int x = worldX + random.nextInt(16);
            int z = worldZ + random.nextInt(16);
            int y = limitedRegion.getHighestBlockYAt(x, z);

            // Krater erstellen (z. B. als einfache Vertiefung)
            int craterRadius = 3 + random.nextInt(3); // Radius zwischen 3 und 5
            for (int dx = -craterRadius; dx <= craterRadius; dx++) {
                for (int dz = -craterRadius; dz <= craterRadius; dz++) {
                    if (dx * dx + dz * dz <= craterRadius * craterRadius) {
                        for (int dy = 0; dy < 3; dy++) { // Tiefe des Kraters
                            limitedRegion.setType(x + dx, y - dy, z + dz, Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
