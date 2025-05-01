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
package de.datenente.cyberente;

import de.datenente.cyberente.commands.*;
import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.listeners.*;
import de.datenente.cyberente.recipes.BreadRecipe;
import de.datenente.cyberente.special.PlayTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CyberEnte extends JavaPlugin {

    @Getter
    static CyberEnte instance;

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(250);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin CyberEnte wird geladen!");

        synchronized (this) {
            instance = this;
        }

        // Load Config
        new StorageConfig(getLogger(), getDataFolder());

        // Register Commands & Listeners
        registerListeners();
        registerCommands();

        // Special Features
        new BreadRecipe().register();
        new PlayTime().startTimer();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin CyberEnte wird deaktiviert!");

        getServer().resetRecipes();
    }

    void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new ChickenPlantListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new DropChestListener(), this);
        pluginManager.registerEvents(new CheatBlockListener(), this);
        pluginManager.registerEvents(new JoinLeaveListener(), this);
        pluginManager.registerEvents(new ReplantListener(), this);
        pluginManager.registerEvents(new StairSittingListener(), this);
    }

    void registerCommands() {
        CommandMap commandMap = getServer().getCommandMap();

        commandMap.register("cyberente", new PingCommand());
        commandMap.register("cyberente", new ClearChatCommand());
        commandMap.register("cyberente", new ChatImageCommand());
    }
}
