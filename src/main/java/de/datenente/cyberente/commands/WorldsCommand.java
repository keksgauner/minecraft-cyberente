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
        super("worlds", "Manage worlds", "/worlds", List.of());
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {

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
                    if (!sender.isOp()) {
                        Message.send(sender, "You do not have permission to use this command!");
                        return true;
                    }

                    Message.send(sender, "Remove {0} World...", world);
                    CustomWorldCreator.unloadWorld(world, true);

                    StorageConfig storageConfig = StorageConfig.getInstance();
                    storageConfig.removeWorld(world);
                    Message.send(sender, "{0} World removed!", world);
                    return true;
                }
                case "delete" -> {
                    if (!sender.isOp()) {
                        Message.send(sender, "You do not have permission to use this command!");
                        return true;
                    }

                    Message.send(sender, "Delete {0} World...", world);
                    CustomWorldCreator.deleteWorld(world);

                    StorageConfig storageConfig = StorageConfig.getInstance();
                    storageConfig.removeWorld(world);
                    Message.send(sender, "{0} World deleted!", world);
                    return true;
                }
            }
        }

        if (args.length == 4) {
            String type = args[0].toLowerCase();
            String world = args[1].toLowerCase();
            String environment = args[2].toUpperCase();
            String generator = args[3].toUpperCase();

            if (type.equals("generate")) {
                if (!sender.isOp()) {
                    Message.send(sender, "You do not have permission to use this command!");
                    return true;
                }

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
        }

        Message.send(sender, "Usage: /world <list>");
        Message.send(sender, "Usage: /world <tp/remove/delete> <world>");
        Message.send(sender, "Usage: /world <generate> <world> <environment> <generator>");
        return true;
    }

    // TODO: filter einbauen
    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {

        if (args.length == 1) {
            return List.of("tp", "generate", "remove", "delete", "list");
        }

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            return Bukkit.getWorlds().stream().map(World::getName).toList();
        }

        if (args.length == 3) {
            String type = args[0].toLowerCase();
            if (type.equals("generate")) {

                return Arrays.stream(World.Environment.values())
                        .map(World.Environment::name)
                        .toList();
            }
        }

        if (args.length == 4) {
            String type = args[0].toLowerCase();
            if (type.equals("generate")) {
                return Arrays.stream(CustomGenerator.values())
                        .map(CustomGenerator::name)
                        .toList();
            }
        }

        return List.of();
    }
}
