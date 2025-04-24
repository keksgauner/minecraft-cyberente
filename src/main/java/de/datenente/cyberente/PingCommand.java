package de.datenente.cyberente;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends Command {

    protected PingCommand() {
        super("Ping");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] args) {
        commandSender.sendMessage("Pong");

        if (commandSender instanceof Player player && args.length == 0) {
            player.sendMessage("Dein Ping ist: " + player.getPing());
        }

        if (args.length > 0) {
            Player target = Bukkit.getPlayer(args[0]);

            commandSender.sendMessage("Der Ping vom Spieler " + target.getName() + " ist " + target.getPing());
        }

        return true;
    }
}
