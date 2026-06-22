/*
 * MIT License
 *
 * Copyright (c) 2025 - 2026 KeksGauner, CyberEnte
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
package de.datenente.cyberente.commands;

import de.datenente.cyberente.hibernate.Databases;
import de.datenente.cyberente.hibernate.database.HomeDatabase;
import de.datenente.cyberente.hibernate.mappings.SQLHome;
import de.datenente.cyberente.utils.Message;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand extends Command {
    static final int MAX_HOMES = 3;

    public HomeCommand() {
        super("home", "Teleportiert dich zu deinen Homes", "/home <name|set|delete|list>", List.of());
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            Message.send(sender, "<red>Dieser Befehl kann nur von einem Spieler ausgeführt werden.</red>");
            return true;
        }

        HomeDatabase homeDatabase = Databases.getInstance().getHomeDatabase();

        if (args.length == 1) {
            String type = args[0].toLowerCase();

            if (type.equals("list")) {
                List<SQLHome> homes = homeDatabase.getHomes(player.getUniqueId());
                if (homes.isEmpty()) {
                    Message.send(player, "<yellow>Du hast noch keine Homes gesetzt.</yellow>");
                    return true;
                }

                Message.send(
                        player,
                        "<gray>Homes ({0}/{1}):</gray> <green>{2}</green>",
                        homes.size(),
                        MAX_HOMES,
                        String.join(
                                ", ", homes.stream().map(SQLHome::getHomeName).toList()));
                return true;
            }

            SQLHome sqlHome = homeDatabase.getHome(player.getUniqueId(), type);
            if (sqlHome == null) {
                Message.send(player, "<red>Home <yellow>{0}</yellow> wurde nicht gefunden.</red>", type);
                return true;
            }

            World world = Bukkit.getWorld(sqlHome.getWorldName());
            if (world == null) {
                Message.send(player, "<red>Die Welt für Home <yellow>{0}</yellow> ist nicht geladen.</red>", type);
                return true;
            }

            Location location = new Location(
                    world, sqlHome.getX(), sqlHome.getY(), sqlHome.getZ(), sqlHome.getYaw(), sqlHome.getPitch());
            player.teleportAsync(location);
            Message.send(
                    player, "<green>Teleportiere dich zu Home <yellow>{0}</yellow>.</green>", sqlHome.getHomeName());
            return true;
        }

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            String homeName = HomeDatabase.normalizeHomeName(args[1]);

            if (homeName.isEmpty()) {
                Message.send(player, "<red>Bitte gib einen gültigen Home-Namen an.</red>");
                return true;
            }

            if (type.equals("set")) {
                SQLHome sqlHome = homeDatabase.getHome(player.getUniqueId(), homeName);
                List<SQLHome> homes = homeDatabase.getHomes(player.getUniqueId());

                if (sqlHome == null && homes.size() >= MAX_HOMES) {
                    Message.send(player, "<red>Du kannst maximal <yellow>{0}</yellow> Homes haben.</red>", MAX_HOMES);
                    return true;
                }

                Location location = player.getLocation();
                homeDatabase.createOrUpdate(
                        player.getUniqueId(),
                        homeName,
                        location.getWorld().getName(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        location.getYaw(),
                        location.getPitch());
                Message.send(player, "<green>Home <yellow>{0}</yellow> gespeichert.</green>", homeName);
                return true;
            }

            if (type.equals("delete")) {
                if (!homeDatabase.deleteHome(player.getUniqueId(), homeName)) {
                    Message.send(player, "<red>Home <yellow>{0}</yellow> wurde nicht gefunden.</red>", homeName);
                    return true;
                }

                Message.send(player, "<green>Home <yellow>{0}</yellow> gelöscht.</green>", homeName);
                return true;
            }
        }

        Message.send(player, "<red>Nutzung:</red> <green>/home <name></green>");
        Message.send(player, "<red>Nutzung:</red> <green>/home set <name></green>");
        Message.send(player, "<red>Nutzung:</red> <green>/home delete <name></green>");
        Message.send(player, "<red>Nutzung:</red> <green>/home list</green>");
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        HomeDatabase homeDatabase = Databases.getInstance().getHomeDatabase();
        List<String> homeNames = homeDatabase.getHomes(player.getUniqueId()).stream()
                .map(SQLHome::getHomeName)
                .toList();

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>(homeNames);
            suggestions.addAll(List.of("set", "delete", "list"));
            return Message.filter(suggestions, args[0]);
        }

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            if (type.equals("set") || type.equals("delete")) {
                return Message.filter(homeNames, args[1]);
            }
        }

        return List.of();
    }
}
