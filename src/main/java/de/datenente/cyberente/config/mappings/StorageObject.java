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
package de.datenente.cyberente.config.mappings;

import de.datenente.cyberente.utils.worlds.CustomGenerator;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
@Setter
public class StorageObject {
    HashMap<String, Worlds> worlds = new HashMap<>(); // World Name
    HashMap<String, List<String>> worldGroups = new HashMap<>();

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LocationObject {
        String world;
        double x;
        double y;
        double z;
        float yaw;
        float pitch;

        public LocationObject(Location location) {
            this.world = location.getWorld().getName();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }

        public Location getLocation() {
            World realWorld = Bukkit.getWorld(world);
            return new Location(realWorld, x, y, z, yaw, pitch);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Worlds {
        CustomGenerator generator;
        World.Environment environment;
    }
}
