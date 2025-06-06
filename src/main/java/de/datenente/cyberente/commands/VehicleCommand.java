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

import de.datenente.cyberente.listeners.VehicleListener;
import de.datenente.cyberente.utils.Message;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VehicleCommand extends Command {

    public VehicleCommand() {
        super("vehicle", "Fahre mit dem Auto", "/vehicle", List.of("av"));
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            Message.send(sender, "<red>Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        if (args.length == 0) {
            VehicleListener.spawnCar(player);
            return true;
        }

        Message.send(sender, "<red>Benutze /vehicle");
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        return List.of();
    }
}
