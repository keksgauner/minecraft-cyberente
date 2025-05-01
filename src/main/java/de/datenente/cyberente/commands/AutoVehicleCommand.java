package de.datenente.cyberente.commands;

import de.datenente.cyberente.listeners.AutoVehicle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AutoVehicleCommand extends Command {

    public AutoVehicleCommand() {
        super("autovehicle", "Fahre mit dem Auto", "/autovehicle", List.of("av"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {

        if(sender instanceof Player player) {
            if (args.length == 0) {
                AutoVehicle.spawn(player);
                return true;
            }
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
        }
        return false;
    }

}
