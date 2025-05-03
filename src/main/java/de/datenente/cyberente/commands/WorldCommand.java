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

import de.datenente.cyberente.worlds.MoonWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldCommand extends Command {

    public WorldCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /world <name>");
            return false;
        }

        if (args.length == 1) {
            String arg = args[0];
            Player player = (Player) sender;
            switch (arg) {
                case "moon":
                    sender.sendMessage("Creating Moon World...");
                    // MoonGenerator moonGenerator = new MoonGenerator();
                    MoonWorld.createCustomWorld("MoonWorld");
                    sender.sendMessage("Moon World created!");
                    return true;
                case "mars":
                    sender.sendMessage("Creating Mars World...");
                    // MarsGenerator marsGenerator = new MarsGenerator();
                    MoonWorld.createCustomWorld("MarsWorld");
                    sender.sendMessage("Mars World created!");
                    return true;
                case "tp":
                    sender.sendMessage("Teleporting to Moon World...");
                    player.teleport(new Location(Bukkit.getWorld("MoonWorld"), 0, 62, 0));
                    player.setGameMode(GameMode.CREATIVE);
                    player.getVehicle().addPassenger(player);
                    return true;
                case "tpback":
                    sender.sendMessage("Teleporting back to the original world...");
                    player.teleport(new Location(Bukkit.getWorld("world"), -168, 68, -54));
                    return true;
                default:
                    sender.sendMessage("moon,tp,tpback");
                    return false;
            }
        }

        if (args.length == 2) {
            String arg = args[0];
            String arg2 = args[1];
            Player player = (Player) sender;
            switch (arg) {
                case "tp":
                    if (arg2.equals("moon")) {
                        sender.sendMessage("Teleporting to Moon World...");
                        player.teleport(new Location(Bukkit.getWorld("MoonWorld"), 0, 62, 0));
                        player.setGameMode(GameMode.CREATIVE);
                    } else if (arg2.equals("mars")) {
                        sender.sendMessage("Teleporting to Mars World...");
                        player.teleport(new Location(Bukkit.getWorld("MarsWorld"), 0, 62, 0));
                        player.setGameMode(GameMode.CREATIVE);
                    }
                    return true;
                default:
                    sender.sendMessage("moon,tp,tpback");
                    return false;
            }
        }

        // MoonGenerator moonGenerator = new MoonGenerator();
        //  MoonWorld.createCustomWorld("MoonWorld");
        return true;
    }
}
