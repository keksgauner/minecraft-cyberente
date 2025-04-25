package de.datenente.cyberente.commands;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("clearchat", "Löscht den Chat", "/", List.of("cc"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcast(Component.text(""));
        }

        return true;
    }
}
