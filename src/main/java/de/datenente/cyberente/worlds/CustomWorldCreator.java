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
package de.datenente.cyberente.worlds;

import de.datenente.cyberente.CyberEnte;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.codehaus.plexus.util.FileUtils;

public class CustomWorldCreator {
    public static World createMoonWorld() {
        WorldCreator creator = new WorldCreator("world_moon");

        creator.environment(World.Environment.NORMAL);
        creator.generator(new MoonGenerator());

        World world = creator.createWorld();
        if (world == null) {
            CyberEnte.getInstance().getLogger().severe("Failed to create world: world_moon");
            return null;
        }
        return world;
    }

    public static World createMarsWorld() {
        WorldCreator creator = new WorldCreator("world_mars");

        creator.environment(World.Environment.NORMAL);
        creator.generator(new MarsGenerator());

        World world = creator.createWorld();
        if (world == null) {
            CyberEnte.getInstance().getLogger().severe("Failed to create world: world_mars");
            return null;
        }
        return world;
    }

    public static boolean unloadWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return false;

        world.getPlayers()
                .forEach(player -> player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation()));
        return Bukkit.unloadWorld(world, true);
    }

    public static void deleteWorld(String worldName) {
        unloadWorld(worldName);
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        try {
            FileUtils.deleteDirectory(worldFolder);
        } catch (IOException ex) {
            CyberEnte.getInstance().getLogger().log(Level.SEVERE, "Failed to delete world folder", ex);
        }
    }
}
