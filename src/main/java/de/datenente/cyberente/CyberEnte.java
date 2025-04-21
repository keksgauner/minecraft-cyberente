package de.datenente.cyberente;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberEnte extends JavaPlugin {

    public static CyberEnte instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Server wurde gestartet!");

        instance = this;

        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveEvent(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
