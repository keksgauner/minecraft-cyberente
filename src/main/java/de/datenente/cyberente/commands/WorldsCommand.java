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

import de.datenente.cyberente.utils.Message;
import de.datenente.cyberente.worlds.CustomWorldCreator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldsCommand extends Command {

    public WorldsCommand() {
        super("worlds");
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {

        if (args.length == 2) {
            String type = args[0].toLowerCase();
            String world = args[1].toLowerCase();
            if (type.equals("tp")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(Message.text("You must be a player to use this command!"));
                    return true;
                }

                World realWorld = Bukkit.getWorld(world);
                if (realWorld == null) {
                    sender.sendMessage(Message.text("World not found!"));
                    return true;
                }

                Location spawnLocation = realWorld.getSpawnLocation();
                Location location =
                        new Location(realWorld, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());

                player.teleport(location);
                return true;
            }
            if (type.equals("generate")) {
                if (!sender.isOp()) {
                    sender.sendMessage(Message.text("You do not have permission to use this command!"));
                    return true;
                }

                if (world.equals("moon")) {
                    sender.sendMessage(Message.text("Generating Moon World..."));
                    CustomWorldCreator.createMoonWorld();
                    sender.sendMessage(Message.text("Moon World generated!"));
                    return true;
                }
                if (world.equals("mars")) {
                    sender.sendMessage(Message.text("Generating Mars World..."));
                    CustomWorldCreator.createMarsWorld();
                    sender.sendMessage(Message.text("Mars World generated!"));
                    return true;
                }

                sender.sendMessage(Message.text("Invalid world type! Available: moon, mars"));
                return true;
            }

            if (type.equals("unload")) {
                if (!sender.isOp()) {
                    sender.sendMessage(Message.text("You do not have permission to use this command!"));
                    return true;
                }

                if (world.equals("moon")) {
                    sender.sendMessage(Message.text("Deleting Moon World..."));
                    CustomWorldCreator.unloadWorld("world_moon");
                    sender.sendMessage(Message.text("Moon World deleted!"));
                    return true;
                }
                if (world.equals("mars")) {
                    sender.sendMessage(Message.text("Deleting Mars World..."));
                    CustomWorldCreator.unloadWorld("world_mars");
                    sender.sendMessage(Message.text("Mars World deleted!"));
                    return true;
                }

                sender.sendMessage(Message.text("Invalid world type! Available: moon, mars"));
                return true;
            }

            sender.sendMessage(Message.text("Invalid command! Available: tp, generate"));
            return true;
        }

        sender.sendMessage(Message.text("Usage: /world <tp/generate> <moon/mars/world>"));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {

        if (args.length == 1) {
            return List.of("tp", "generate", "delete");
        }

        if (args.length == 2) {
            // Wenn es sich um den ersten Parameter "tp" handelt, gebe die Welten zurück
            String type = args[0].toLowerCase();
            if (type.equals("tp")) {
                return List.of("moon", "mars", "world");
            }
            return List.of("moon", "mars");
        }

        return List.of();
    }
}
