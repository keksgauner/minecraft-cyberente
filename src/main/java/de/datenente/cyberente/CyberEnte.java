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
import de.datenente.cyberente.hibernate.Databases;
import de.datenente.cyberente.listeners.*;
import de.datenente.cyberente.recipes.BreadRecipe;
import de.datenente.cyberente.recipes.PotionRecipe;
import de.datenente.cyberente.special.AFKDetector;
import de.datenente.cyberente.special.DayNight;
import de.datenente.cyberente.special.PlayTime;
import de.datenente.cyberente.special.WorldLoader;
import de.datenente.cyberente.utils.worlds.CustomGenerator;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

@Getter
public final class CyberEnte extends JavaPlugin {

    @Getter
    static CyberEnte instance;

    Instant startTime;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(250);

    @Override
    public void onLoad() {
        getLogger().info("Plugin CyberEnte wird geladen!");
        synchronized (this) {
            instance = this;
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin CyberEnte wird aktiviert!");
        this.startTime = Instant.now();

        // Hibernate
        Databases.getInstance().openDatabaseConnection();
        Databases.getInstance().startDatabaseConnectTask();

        registerListeners();
        registerCommands();

        // Special Features
        BreadRecipe.register();
        PotionRecipe.register();
        PlayTime.startPlayTimeTask();
        AFKDetector.getInstance().startAFKCheckTask();
        DayNight.getInstance().startDayNightCycleTask();

        Bukkit.getScheduler().runTask(this, WorldLoader::load);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin CyberEnte wird deaktiviert!");

        Databases.getInstance().getHibernateConnection().close();

        getServer().resetRecipes();

        StairSittingListener.clean();
        VehicleListener.clean();

        this.getScheduledExecutorService().shutdown();

        AFKDetector.getInstance().removeAll();
        DayNight.getInstance().removeAll();
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        if (id == null) return null;
        CustomGenerator generator = CustomGenerator.valueOf(id.toUpperCase());
        return generator.getChunkGenerator();
    }

    void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ChatListener(), this);
        /// pluginManager.registerEvents(new ChickenPlantListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new DropChestListener(), this);
        // pluginManager.registerEvents(new CheatBlockListener(), this);
        pluginManager.registerEvents(new JoinLeaveListener(), this);
        pluginManager.registerEvents(new MoveListener(), this);
        pluginManager.registerEvents(new ReplantListener(), this);
        pluginManager.registerEvents(new StairSittingListener(), this);
        pluginManager.registerEvents(new VehicleListener(), this);
        pluginManager.registerEvents(new WorldChangeListener(), this);
        pluginManager.registerEvents(new ChickenPlantRandomEntityListener(), this);
    }

    void registerCommands() {
        CommandMap commandMap = getServer().getCommandMap();

        commandMap.register("cyberente", new PingCommand());
        commandMap.register("cyberente", new ClearChatCommand());
        commandMap.register("cyberente", new ChatImageCommand());
        commandMap.register("cyberente", new VehicleCommand());
        commandMap.register("cyberente", new WorldsCommand());
        commandMap.register("cyberente", new TrashCommand());
        commandMap.register("cyberente", new AFKCommand());
    }
}
