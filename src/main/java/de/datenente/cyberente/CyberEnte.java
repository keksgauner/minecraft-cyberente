package de.datenente.cyberente;

import de.datenente.cyberente.commands.*;
import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.listeners.*;
import lombok.Getter;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberEnte extends JavaPlugin {

    @Getter
    static CyberEnte instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin CyberEnte wird geladen!");

        synchronized (this) {
            instance = this;
        }

        // Register Listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new DropChestListener(), this);
        pluginManager.registerEvents(new CheatBlockListener(), this);
        pluginManager.registerEvents(new JoinLeaveListener(), this);
        pluginManager.registerEvents(new StairSittingListener(), this);
        pluginManager.registerEvents(new ChickenPlantListener(), this);

        // Register Commands
        CommandMap commandMap = getServer().getCommandMap();
        commandMap.register("cyberente", new PingCommand());
        commandMap.register("cyberente", new ClearChatCommand());

        // Load Config
        new StorageConfig(getLogger(), getDataFolder());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
