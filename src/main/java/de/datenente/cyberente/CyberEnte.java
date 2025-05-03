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
import de.datenente.cyberente.config.MySQLConfig;
import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.hibernate.Databases;
import de.datenente.cyberente.listeners.*;
import de.datenente.cyberente.recipes.BreadRecipe;
import de.datenente.cyberente.recipes.PotionRecipe;
import de.datenente.cyberente.special.PlayTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
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

        registerConfigs();

        // Hibernate
        new Databases(getLogger());
        Databases.getInstance().openDatabaseConnection();
        Databases.getInstance().startDatabaseConnectTask();

        registerListeners();
        registerCommands();

        // Special Features
        new BreadRecipe().register();
        new PotionRecipe().register();
        new PlayTime().startTimer();

        // Automobility Recipes
        registerAutomobilityRecipes();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin CyberEnte wird deaktiviert!");

        Databases.getInstance().getHibernateConnection().close();

        getServer().resetRecipes();
        StairSittingListener.clean();
    }

    void registerConfigs() {
        new MySQLConfig(getLogger(), getDataFolder());
        new StorageConfig(getLogger(), getDataFolder());
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
        pluginManager.registerEvents(new AutoVehicleListener(), this);
    }

    void registerCommands() {
        getServer().getCommandMap().register("cyberente", new PingCommand());
        getServer().getCommandMap().register("cyberente", new ClearChatCommand());
        getServer().getCommandMap().register("cyberente", new ChatImageCommand());
        getServer().getCommandMap().register("cyberente", new AutoVehicleCommand());
    }

    private void registerAutomobilityRecipes() {
        // Auto Mechanic Table
        ItemStack mechanicTable = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta mechanicMeta = mechanicTable.getItemMeta();
        mechanicMeta.setDisplayName("Auto Mechanic Table");
        mechanicTable.setItemMeta(mechanicMeta);

        ShapedRecipe mechanicRecipe = new ShapedRecipe(new NamespacedKey(this, "auto_mechanic_table"), mechanicTable);
        mechanicRecipe.shape("AA", "BB");
        mechanicRecipe.setIngredient('A', Material.COPPER_INGOT);
        mechanicRecipe.setIngredient('B', Material.STONE);
        Bukkit.addRecipe(mechanicRecipe);

        // Automobile Assembler
        ItemStack assembler = new ItemStack(Material.SMITHING_TABLE);
        ItemMeta assemblerMeta = assembler.getItemMeta();
        assemblerMeta.setDisplayName("Automobile Assembler");
        assembler.setItemMeta(assemblerMeta);

        ShapedRecipe assemblerRecipe = new ShapedRecipe(new NamespacedKey(this, "automobile_assembler"), assembler);
        assemblerRecipe.shape("AAA", " B ", "BBB");
        assemblerRecipe.setIngredient('A', Material.COPPER_INGOT);
        assemblerRecipe.setIngredient('B', Material.STONE);
        Bukkit.addRecipe(assemblerRecipe);

        // Autopilot Sign (x6)
        ItemStack autopilotSign = new ItemStack(Material.OAK_SIGN, 6);
        ItemMeta signMeta = autopilotSign.getItemMeta();
        signMeta.setDisplayName("Autopilot Sign");
        autopilotSign.setItemMeta(signMeta);

        ShapedRecipe autopilotRecipe = new ShapedRecipe(new NamespacedKey(this, "autopilot_sign"), autopilotSign);
        autopilotRecipe.shape("ACA", "ABA", " D ");
        autopilotRecipe.setIngredient('A', Material.IRON_INGOT);
        autopilotRecipe.setIngredient('B', Material.REDSTONE);
        autopilotRecipe.setIngredient('C', Material.EMERALD);
        autopilotRecipe.setIngredient('D', Material.IRON_BARS);
        Bukkit.addRecipe(autopilotRecipe);
    }
}
