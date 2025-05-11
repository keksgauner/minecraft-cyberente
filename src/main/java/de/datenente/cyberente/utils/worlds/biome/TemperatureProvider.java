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
package de.datenente.cyberente.utils.worlds.biome;

import java.util.List;
import lombok.Getter;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

@Getter
public class TemperatureProvider extends BiomeProvider {

    @NotNull @Override
    public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        SimplexOctaveGenerator temperature = new SimplexOctaveGenerator(worldInfo.getSeed(), 2);
        SimplexOctaveGenerator humidity = new SimplexOctaveGenerator(worldInfo.getSeed(), 2);
        temperature.setScale(0.01);
        humidity.setScale(0.01);

        double temp = temperature.noise(x, z, 0.5, 1.0);
        double hum = humidity.noise(x, z, 0.5, 1.0);

        return pickBiome(temp, hum);
    }

    @NotNull @Override
    public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return List.of(Biome.SNOWY_PLAINS, Biome.DESERT, Biome.SWAMP, Biome.PLAINS, Biome.FOREST);
    }

    Biome pickBiome(double temp, double hum) {
        if (temp < -0.3) return Biome.SNOWY_PLAINS;
        if (temp > 0.6 && hum < 0.3) return Biome.DESERT;
        if (hum > 0.7) return Biome.SWAMP;
        if (temp > 0.5) return Biome.PLAINS;
        return Biome.FOREST;
    }
}
