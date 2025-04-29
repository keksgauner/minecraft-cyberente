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
package de.datenente.cyberente.listeners;

import de.datenente.cyberente.utils.Message;
import io.papermc.paper.event.world.WorldGameRuleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheatBlockListener implements Listener {

    @EventHandler
    public void handleGameModeChange(PlayerGameModeChangeEvent gameModeChangeEvent) {
        gameModeChangeEvent.setCancelled(true);
        Player player = gameModeChangeEvent.getPlayer();
        Bukkit.broadcast(Message.text(
                "<red>{0} wollte in den {1}-Modus wechseln – wurde erfolgreich blockiert.</red>",
                player.getName(), gameModeChangeEvent.getNewGameMode().name()));
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void handleGameRuleChange(WorldGameRuleChangeEvent gameRuleChangeEvent) {
        gameRuleChangeEvent.setCancelled(true);
        Bukkit.broadcast(Message.text("<red>Die Gamerule darf nicht verändert werden.</red>"));
    }

    @EventHandler
    public void handleTeleport(PlayerTeleportEvent teleportEvent) {
        if (teleportEvent.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
            teleportEvent.setCancelled(true);
            Bukkit.broadcast(Message.text(
                    "<red>{0} - Es darf sich nicht Teleportiert werden!</red>",
                    teleportEvent.getPlayer().getName()));
        }
    }
}
