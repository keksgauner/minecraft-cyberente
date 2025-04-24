package de.datenente.cyberente;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearChatCommand extends Command {
    protected ClearChatCommand() {
        super("clearChat", "/", "/", List.of("cc"));
    }

    @Override
    public boolean execute(
            @NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] strings) {

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcast(Component.text(""));
        }

        return true;
    }
}
