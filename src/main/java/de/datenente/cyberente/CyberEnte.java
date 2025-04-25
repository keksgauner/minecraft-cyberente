package de.datenente.cyberente;

import de.datenente.cyberente.commands.ClearChatCommand;
import de.datenente.cyberente.commands.PingCommand;
import de.datenente.cyberente.listeners.ChatListener;
import de.datenente.cyberente.listeners.DropChestListener;
import de.datenente.cyberente.listeners.JoinLeaveListener;
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
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new DropChestListener(), this);
        pm.registerEvents(new JoinLeaveListener(), this);
        // pm.registerEvents(new StairSittingListener(), this);

        // Register Commands
        CommandMap cm = getServer().getCommandMap();
        cm.register("cyberente", new PingCommand());
        cm.register("cyberente", new ClearChatCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
