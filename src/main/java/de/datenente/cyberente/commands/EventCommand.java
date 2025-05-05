package de.datenente.cyberente.commands;

import de.datenente.cyberente.luckyblocks.LuckyBlockGenerator;
import de.datenente.cyberente.utils.Message;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EventCommand extends Command {

    public EventCommand() {
        super("events");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player) {
            // Neue Welt für das Event erstellen
            WorldCreator creator = new WorldCreator("world_luckyblocks");
            creator.environment(World.Environment.NORMAL);
            World world = creator.createWorld();

            // LuckyBlocks erzeugen
            LuckyBlockGenerator generator = new LuckyBlockGenerator();
            generator.generateLuckyBlocks(world, 20, -50, 50, -50, 50, 70);

            player.sendMessage(Message.text("<red>LuckyBlock-Event wurde gestartet!"));
        } else {
            sender.sendMessage(Message.text("<red>Nur Spieler können dieses Kommando ausführen."));
        }

        return true;
    }
}
