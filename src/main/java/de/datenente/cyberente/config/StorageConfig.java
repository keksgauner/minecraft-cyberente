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

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.config.JsonDocument;
import de.datenente.cyberente.utils.worlds.CustomGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.World;

@Getter
public class StorageConfig extends JsonDocument<StorageObject> {

    static StorageConfig instance;

    public static StorageConfig getInstance() {
        if (instance == null) {
            CyberEnte cyberEnte = CyberEnte.getInstance();
            Logger logger = cyberEnte.getLogger();
            File dataFolder = cyberEnte.getDataFolder();
            instance = new StorageConfig(logger, dataFolder);
        }
        return instance;
    }

    public StorageConfig(Logger pluginLogger, File dataFolder) {
        super(pluginLogger, dataFolder, StorageObject.class, "storage.json");
    }

    @Override
    public void loadContent() {
        this.save();
    }

    /**
     * Worlds
     */
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

    /**
     * World Groups
     * Group = Element in den Gruppen
     * Groups = Die Gruppen
     */
    public void addWorldGroup(String groupName, String world) {
        List<String> worlds = this.getStorage().getWorldGroups().getOrDefault(groupName, new ArrayList<>());
        worlds.add(world);
        this.getStorage().getWorldGroups().put(groupName, worlds);
        this.save();
    }

    public void removeWorldGroup(String groupName, String world) {
        List<String> worlds = this.getStorage().getWorldGroups().getOrDefault(groupName, new ArrayList<>());
        worlds.remove(world);
        this.getStorage().getWorldGroups().put(groupName, worlds);
        this.save();
    }

    public List<String> getWorldGroup(String groupName) {
        return this.getStorage().getWorldGroups().getOrDefault(groupName, null);
    }

    public HashMap<String, List<String>> getWorldGroups() {
        return this.getStorage().getWorldGroups();
    }

    public void addWorldGroups(String groupName) {
        this.getStorage().getWorldGroups().put(groupName, new ArrayList<>());
        this.save();
    }

    public void removeWorldGroups(String groupName) {
        HashMap<String, List<String>> worldGroups = this.getStorage().getWorldGroups();
        worldGroups.remove(groupName);
        this.save();
    }

    public boolean hasWorldGroups(String groupName) {
        HashMap<String, List<String>> worldGroups = this.getStorage().getWorldGroups();
        return worldGroups.containsKey(groupName);
    }

    public String getWorldGroups(String world) {
        for (String group : this.getStorage().getWorldGroups().keySet()) {
            if (this.getStorage().getWorldGroups().get(group).contains(world)) {
                return group;
            }
        }
        return null;
    }
}
