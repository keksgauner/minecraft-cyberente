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
package de.datenente.cyberente.configuration;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.configuration.mappings.PlayerInventoryObject;
import de.datenente.cyberente.utils.configuration.JsonDocument;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class FileConfig<T> extends JsonDocument<T> {

    public FileConfig(Logger pluginLogger, Path dataDirectory, Class<T> clazz, String file) {
        super(pluginLogger, dataDirectory, clazz, file);
    }

    @Override
    public void loadContent() {
        // Nothing to do here, the default values are already set in the MySQLObject class
    }

    static String serializeLocation(Location location) {
        return location.getWorld().getName() + "_" + location.getBlockX()
                + "_" + location.getBlockY()
                + "_" + location.getBlockZ();
    }

    /**
     * Death Skull
     */
    public static FileConfig<PlayerInventoryObject> getDeathSkullFile(Location location) {
        CyberEnte cyberEnte = CyberEnte.getInstance();
        Logger logger = cyberEnte.getLogger();
        Path dataDirectory = cyberEnte.getDataFolder().toPath();
        return new FileConfig<>(
                logger,
                dataDirectory,
                PlayerInventoryObject.class,
                "deathskulls/" + serializeLocation(location) + ".json");
    }

    public static void setDeathSkull(Location location, String base64Inventory, Integer level, Float xp) {
        FileConfig<PlayerInventoryObject> deathSkull = getDeathSkullFile(location);
        deathSkull.getStorage().setBase64(base64Inventory);
        deathSkull.getStorage().setLevel(level);
        deathSkull.getStorage().setXp(xp);
        deathSkull.save();
    }

    public static boolean hasDeathSkull(Location location) {
        FileConfig<PlayerInventoryObject> deathSkull = getDeathSkullFile(location);
        return deathSkull.getFile().exists();
    }

    public static PlayerInventoryObject getDeathSkull(Location location) {
        FileConfig<PlayerInventoryObject> deathSkull = getDeathSkullFile(location);
        if (deathSkull.getFile().exists()) {
            return deathSkull.getStorage();
        }
        return null;
    }

    public static boolean deleteDeathSkull(Location location) {
        FileConfig<PlayerInventoryObject> deathSkull = getDeathSkullFile(location);
        return deathSkull.delete();
    }

    /**
     * Player Inventory
     * */
    public static FileConfig<PlayerInventoryObject> getWorldInventoryFile(UUID uuid, String world) {
        CyberEnte cyberEnte = CyberEnte.getInstance();
        Logger logger = cyberEnte.getLogger();
        Path dataDirectory = cyberEnte.getDataFolder().toPath();
        return new FileConfig<>(
                logger, dataDirectory, PlayerInventoryObject.class, "inventory/" + world + "_" + uuid + ".json");
    }

    public static void setWorldInventory(UUID uuid, String world, String base64Inventory, Integer level, Float xp) {
        FileConfig<PlayerInventoryObject> worldInventory = getWorldInventoryFile(uuid, world);
        worldInventory.getStorage().setBase64(base64Inventory);
        worldInventory.getStorage().setLevel(level);
        worldInventory.getStorage().setXp(xp);
        worldInventory.save();
    }

    public static PlayerInventoryObject getWorldInventory(UUID uuid, String world) {
        FileConfig<PlayerInventoryObject> worldInventory = getWorldInventoryFile(uuid, world);
        if (worldInventory.getFile().exists()) {
            return worldInventory.getStorage();
        }
        return null;
    }
}
