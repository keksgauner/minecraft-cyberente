package de.datenente.cyberente.commands;

import de.datenente.cyberente.utils.Message;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player && args.length == 0) {
            player.sendMessage(Message.get("Dein Ping ist: <green>" + player.getPing()));
            return true;
        }

        if (args.length == 1) {
            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                sender.sendMessage(Message.get("<red>Der Spieler " + targetName + " ist nicht Online!"));
                return true;
            }

            sender.sendMessage(
                    Message.get("Der Ping vom Spieler " + target.getName() + " ist <green>" + target.getPing()));
            return true;
        }

        sender.sendMessage(Message.get("<red>Befehl wurde falsch eingegeben! /ping [player]"));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {

        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        return List.of();
    }
}
