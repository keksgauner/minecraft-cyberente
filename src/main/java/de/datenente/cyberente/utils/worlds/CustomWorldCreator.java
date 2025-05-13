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
package de.datenente.cyberente.utils.worlds;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.codehaus.plexus.util.FileUtils;

public class CustomWorldCreator {
    public static World createWorld(String worldName, World.Environment environment, CustomGenerator generator) {
        WorldCreator creator = new WorldCreator(worldName);

        creator.environment(environment);
        if (generator != null) {
            creator.generator(generator.getChunkGenerator());
        }

        return creator.createWorld();
    }

    public static boolean unloadWorld(String worldName, boolean save) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return false;

        world.getPlayers()
                .forEach(player -> player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation()));
        return Bukkit.unloadWorld(world, save);
    }

    public static void deleteWorld(String worldName) {
        unloadWorld(worldName, false);
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        try {
            FileUtils.deleteDirectory(worldFolder);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to delete world folder: ", ex);
        }
    }
}
