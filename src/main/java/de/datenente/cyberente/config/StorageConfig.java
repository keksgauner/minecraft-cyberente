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
package de.datenente.cyberente.config;

import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.config.JsonDocument;
import de.datenente.cyberente.utils.worlds.CustomGenerator;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
public class StorageConfig extends JsonDocument<StorageObject> {

    @Getter
    static StorageConfig instance;

    public StorageConfig(Logger pluginLogger, File dataFolder) {
        super(pluginLogger, dataFolder, StorageObject.class, "storage.json");

        synchronized (this) {
            instance = this;
        }
    }

    @Override
    public void loadContent() {
        this.reload();
    }

    String serializeLocation(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX()
                + ":" + location.getBlockY()
                + ":" + location.getBlockZ();
    }

    public void setDeathSkull(Location location, String base64Inventory, Integer level, Float xp) {
        HashMap<String, StorageObject.PlayerInventory> deathSkulls =
                this.getStorage().getDeathSkulls();

        deathSkulls.put(serializeLocation(location), new StorageObject.PlayerInventory(base64Inventory, level, xp));
        this.save();
    }

    public StorageObject.PlayerInventory getDeathSkull(Location location) {
        HashMap<String, StorageObject.PlayerInventory> deathSkulls =
                this.getStorage().getDeathSkulls();

        return deathSkulls.getOrDefault(serializeLocation(location), null);
    }

    public void removeDeathSkull(Location location) {
        HashMap<String, StorageObject.PlayerInventory> deathSkulls =
                this.getStorage().getDeathSkulls();

        deathSkulls.remove(serializeLocation(location));
        this.save();
    }

    public void setPlayerInventory(UUID uuid, String world, String base64Inventory, Integer level, Float xp) {
        HashMap<String, StorageObject.PlayerInventory> playerInventory =
                this.getStorage().getPlayerInventory();

        playerInventory.put(uuid + ":" + world, new StorageObject.PlayerInventory(base64Inventory, level, xp));
        this.save();
    }

    public StorageObject.PlayerInventory getPlayerInventory(UUID uuid, String world) {
        HashMap<String, StorageObject.PlayerInventory> playerInventory =
                this.getStorage().getPlayerInventory();

        return playerInventory.getOrDefault(uuid + ":" + world, null);
    }

    public void setWorld(String world, World.Environment environment, CustomGenerator generator) {
        HashMap<String, StorageObject.Worlds> worldsMap = this.getStorage().getWorlds();

        worldsMap.put(world, new StorageObject.Worlds(generator, environment));
        this.save();
    }

    public StorageObject.Worlds getWorld(String world) {
        HashMap<String, StorageObject.Worlds> worldsMap = this.getStorage().getWorlds();

        return worldsMap.getOrDefault(world, null);
    }

    public HashMap<String, StorageObject.Worlds> getWorlds() {
        return this.getStorage().getWorlds();
    }

    public void removeWorld(String world) {
        HashMap<String, StorageObject.Worlds> worldsMap = this.getStorage().getWorlds();

        worldsMap.remove(world);
        this.save();
    }

    public void setWorldGroup(String groupName, List<String> worlds) {
        this.getStorage().getWorldGroups().put(groupName, worlds);
        this.save();
    }

    public List<String> getWorldGroup(String groupName) {
        return this.getStorage().getWorldGroups().getOrDefault(groupName, null);
    }
}
