package de.datenente.cyberente.commands;

import de.datenente.cyberente.special.AFKDetector;
import de.datenente.cyberente.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AFKCommand extends Command {
    public AFKCommand() {
        super("afk");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String  commandLabel, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) {
            Message.send(sender, "This command can only be used by players.");
            return true;
        }

        AFKDetector afkDetector = AFKDetector.getInstance();
        afkDetector.setAfkStatus(player, !afkDetector.getAfkStatus().getOrDefault(player.getUniqueId(), false));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        return List.of();
    }
}
