package de.datenente.cyberente.commands;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("clearchat", "Löscht den Chat", "/", List.of("cc"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcast(Component.text(""));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        return List.of();
    }
}
