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

import de.datenente.cyberente.hibernate.Databases;
import de.datenente.cyberente.hibernate.database.PlayerDatabase;
import de.datenente.cyberente.hibernate.mappings.SQLPlayer;
import de.datenente.cyberente.special.AFKDetector;
import de.datenente.cyberente.special.DayNight;
import de.datenente.cyberente.utils.Message;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onPlayerJoinMessage(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Component finalMessage = Message.text(
                "<dark_gray>[</dark_gray><green>+</green><dark_gray>]</dark_gray> <hover:show_text:'<gold>{0}</gold>'><gradient:#ADF3FD:#ADF3FD>{1}</gradient></hover>",
                currentTime, player.getName());

        joinEvent.joinMessage(finalMessage);
    }

    @EventHandler
    public void onPlayerJoinDatabase(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();

        PlayerDatabase playerDatabase = Databases.getInstance().getPlayerDatabase();
        SQLPlayer sqlPlayer = playerDatabase.createOrUpdate(player.getUniqueId(), player.getName());
        sqlPlayer.setLastJoin(Instant.now());
        playerDatabase.savePlayer(sqlPlayer);
    }

    @EventHandler
    public void onPlayerJoinSpecial(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();

        AFKDetector afkDetector = AFKDetector.getInstance();
        afkDetector.updateTeam();

        DayNight dayNight = DayNight.getInstance();
        dayNight.addPlayer(player);
    }

    @EventHandler
    public void onPlayerQuitMessage(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Component finalMessage = Message.text(
                "<dark_gray>[</dark_gray><dark_red>-</dark_red><dark_gray>]</dark_gray> <hover:show_text:'<gold>{0}</gold>'><gradient:#ADF3FD:#ADF3FD>{1}</gradient></hover>",
                currentTime, player.getName());

        quitEvent.quitMessage(finalMessage);
    }

    @EventHandler
    public void onPlayerQuitDatabase(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();

        PlayerDatabase playerDatabase = Databases.getInstance().getPlayerDatabase();
        SQLPlayer sqlPlayer = playerDatabase.getPlayer(player.getUniqueId());
        sqlPlayer.setLastLeave(Instant.now());
        playerDatabase.savePlayer(sqlPlayer);
    }

    @EventHandler
    public void onPlayerQuitCleanUp(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();

        AFKDetector.getInstance().cleanUp(player);

        DayNight dayNight = DayNight.getInstance();
        dayNight.removePlayer(player);
    }
}
