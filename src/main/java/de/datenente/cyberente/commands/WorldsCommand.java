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
package de.datenente.cyberente.commands;

import de.datenente.cyberente.config.StorageConfig;
import de.datenente.cyberente.utils.Message;
import de.datenente.cyberente.utils.worlds.CustomGenerator;
import de.datenente.cyberente.utils.worlds.CustomWorldCreator;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldsCommand extends Command {

    public WorldsCommand() {
        super("worlds", "Manage worlds", "/worlds", List.of("w"));
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {

        if (!sender.isOp()) {
            Message.send(sender, "You do not have permission to use this command!");
            return true;
        }

        if (args.length == 1) {
            String type = args[0].toLowerCase();

            if (type.equals("list")) {
                StringBuilder worlds = new StringBuilder();
                for (World world : Bukkit.getWorlds()) {
                    worlds.append(world.getName()).append(", ");
                }
                Message.send(sender, "Worlds: {0}", worlds.toString());
                return true;
            }

            if (type.equals("groups")) {
                StringBuilder worlds = new StringBuilder();
                StorageConfig.getInstance().getWorldGroups().forEach((key, values) -> {
                    worlds.append(key)
                            .append(": ")
                            .append(String.join(", ", values))
                            .append(" | ");
                });
                Message.send(sender, "Groups: {0}", worlds.toString());
                return true;
            }
        }

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            String world = args[1].toLowerCase();

            switch (type) {
                case "tp" -> {
                    if (!(sender instanceof Player player)) {
                        Message.send(sender, "You must be a player to use this command!");
                        return true;
                    }

                    World realWorld = Bukkit.getWorld(world);
                    if (realWorld == null) {
                        Message.send(sender, "<red>World not found!</red>");
                        return true;
                    }

                    player.teleport(realWorld.getSpawnLocation());
                    return true;
                }
                case "remove" -> {
                    Message.send(sender, "Remove {0} World...", world);
                    CustomWorldCreator.unloadWorld(world, true);

                    StorageConfig storageConfig = StorageConfig.getInstance();
                    storageConfig.removeWorld(world);
                    Message.send(sender, "{0} World removed!", world);
                    return true;
                }
                case "delete" -> {
                    Message.send(sender, "Delete {0} World...", world);
                    CustomWorldCreator.deleteWorld(world);

                    StorageConfig storageConfig = StorageConfig.getInstance();
                    storageConfig.removeWorld(world);
                    Message.send(sender, "{0} World deleted!", world);
                    return true;
                }
            }
        }

        if (args.length == 3) {
            String type = args[0].toLowerCase();

            if (type.equals("groups")) {
                String action = args[1].toLowerCase();
                String group = args[2].toLowerCase();
                StorageConfig storageConfig = StorageConfig.getInstance();

                if (action.equals("add")) {
                    if (storageConfig.hasWorldGroups(group)) {
                        Message.send(sender, "Die welten Gruppe {0} existiert bereits!", group);
                        return true;
                    }
                    storageConfig.addWorldGroups(group);
                    Message.send(sender, "Die welten Gruppe {0} wurde hinzugefügt!", group);
                    return true;
                }
                if (action.equals("remove")) {
                    if (!storageConfig.hasWorldGroups(group)) {
                        Message.send(sender, "Die welten Gruppe {0} existiert nicht!", group);
                        return true;
                    }
                    storageConfig.removeWorldGroups(group);
                    Message.send(sender, "Die welten Gruppe {0} wurde gelöscht!", group);
                    return true;
                }
            }
        }

        if (args.length == 4) {
            String type = args[0].toLowerCase();

            if (type.equals("generate")) {
                String world = args[1].toLowerCase();
                String environment = args[2].toUpperCase();
                String generator = args[3].toUpperCase();

                World.Environment realEnvironment;
                try {
                    realEnvironment = World.Environment.valueOf(environment.toUpperCase());
                } catch (IllegalArgumentException e) {
                    Message.send(sender, "<red>Environment not found!</red>");
                    return true;
                }

                CustomGenerator realGenerator;
                try {
                    realGenerator = CustomGenerator.valueOf(generator.toUpperCase());
                } catch (IllegalArgumentException e) {
                    Message.send(sender, "<red>Generator not found!</red>");
                    return true;
                }
                /* Generate is also used for importing worlds
                if (Bukkit.getWorld(world) != null) {
                    Message.send(sender, "<red>World already exists!</red>");
                    return true;
                }
                 */

                Message.send(sender, "Generating {0} World...", world);
                CustomWorldCreator.createWorld(world, realEnvironment, realGenerator);

                StorageConfig storageConfig = StorageConfig.getInstance();
                storageConfig.setWorld(world, realEnvironment, realGenerator);
                Message.send(sender, "{0} World generated!", world);
                return true;
            }
            if (type.equals("group")) {
                String group = args[1].toLowerCase();
                String action = args[2].toLowerCase();
                String world = args[3].toLowerCase();
                StorageConfig storageConfig = StorageConfig.getInstance();

                if (action.equals("add")) {
                    if (!storageConfig.hasWorldGroups(group)) {
                        Message.send(sender, "Die welten Gruppe {0} existiert nicht!", group);
                        return true;
                    }
                    storageConfig.addWorldGroup(group, world);
                    Message.send(sender, "Die Welt {0} wurde der gruppe {1} hinzugefügt!", world, group);
                    return true;
                }
                if (action.equals("remove")) {
                    if (!storageConfig.hasWorldGroups(group)) {
                        Message.send(sender, "Die welten Gruppe {0} existiert nicht!", group);
                        return true;
                    }
                    storageConfig.removeWorldGroup(group, world);
                    Message.send(sender, "Die Welt {0} wurde von der gruppe {1} gelöscht!", world, group);
                    return true;
                }
            }
        }

        Message.send(sender, "Usage: /world <list/groups>");
        Message.send(sender, "Usage: /world <tp/remove/delete> <world>");
        Message.send(sender, "Usage: /world generate <world> <environment> <generator>");
        Message.send(sender, "Usage: /world groups <add/remove> <group>");
        Message.send(sender, "Usage: /world group <group> <add/remove> <world>");
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        if (!sender.isOp()) {
            return List.of();
        }

        if (args.length == 1) {
            return Message.filter(List.of("tp", "generate", "remove", "delete", "list", "groups", "group"), args[0]);
        }

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            if (type.equals("tp") || type.equals("generate") || type.equals("remove") || type.equals("delete")) {
                return Message.filter(
                        Bukkit.getWorlds().stream().map(World::getName).toList(), args[1]);
            }
            if (type.equals("groups")) {
                return Message.filter(List.of("add", "remove"), args[1]);
            }
            if (type.equals("group")) {
                StorageConfig storageConfig = StorageConfig.getInstance();
                return Message.filter(
                        storageConfig.getWorldGroups().keySet().stream().toList(), args[1]);
            }
        }

        if (args.length == 3) {
            String type = args[0].toLowerCase();
            String action = args[1].toLowerCase();
            if (type.equals("generate")) {
                return Message.filter(
                        Arrays.stream(World.Environment.values())
                                .map(World.Environment::name)
                                .toList(),
                        args[2]);
            }
            if (type.equals("groups") && action.equals("remove")) {
                StorageConfig storageConfig = StorageConfig.getInstance();
                return Message.filter(
                        storageConfig.getWorldGroups().keySet().stream().toList(), args[2]);
            }
            if (type.equals("group")) {
                return Message.filter(List.of("add", "remove"), args[2]);
            }
        }

        if (args.length == 4) {
            String type = args[0].toLowerCase();
            if (type.equals("generate")) {
                return Message.filter(
                        Arrays.stream(CustomGenerator.values())
                                .map(CustomGenerator::name)
                                .toList(),
                        args[3]);
            }
            if (type.equals("group")) {
                return Message.filter(
                        Bukkit.getWorlds().stream().map(World::getName).toList(), args[3]);
            }
        }

        return List.of();
    }
}
