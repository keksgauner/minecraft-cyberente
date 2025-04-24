package de.datenente.cyberente;

import org.bukkit.plugin.java.JavaPlugin;

public final class CyberEnte extends JavaPlugin {

    public static CyberEnte instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Server wurde gestartet!");

        instance = this;

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getCommandMap().register("Ping", new PingCommand());
        getServer().getPluginManager().registerEvents(new DropChest(), this);
        getServer().getCommandMap().register("ClearChat", new ClearChatCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
