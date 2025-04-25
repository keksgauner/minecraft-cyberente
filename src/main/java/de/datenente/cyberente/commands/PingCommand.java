package de.datenente.cyberente.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player player && args.length == 0) {
            player.sendMessage("Dein Ping ist: " + player.getPing());
        }

        if (args.length == 1) {
            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                sender.sendMessage("Der Spieler " + targetName + " ist nicht Online!");
                return true;
            }

            sender.sendMessage("Der Ping vom Spieler " + target.getName() + " ist " + target.getPing());
            return true;
        }

        sender.sendMessage("Befehl wurde falsch eingegeben! /ping [player]");

        return true;
    }
}
