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
package de.datenente.cyberente.special;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.utils.Message;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Getter
public class AFKDetector {
    final HashMap<UUID, Long> lastMovement = new HashMap<>();
    final HashMap<UUID, Boolean> status = new HashMap<>();

    static final long AFK_TIME_SECONDS = 60 * 5; // 5 minutes

    static AFKDetector instance;

    public static AFKDetector getInstance() {
        if (instance == null) {
            instance = new AFKDetector();
        }
        return instance;
    }

    /**
     * Cleans up the AFK status of a player.
     * @param player The player to clean up.
     */
    public void cleanUp(Player player) {
        UUID uuid = player.getUniqueId();
        this.getLastMovement().remove(uuid);
        this.getStatus().remove(uuid);

        Team team = getTeam(player);
        team.unregister();
    }

    /**
     * Updates players last movement time.
     * @param player The player who moved.
     */
    public void detectedMovement(Player player) {
        UUID uuid = player.getUniqueId();
        this.getLastMovement().put(uuid, System.currentTimeMillis());
        status(player, false);
    }

    /**
     * Returns the last movement time of a player.
     * If the player has not moved, it sets the last movement time to now.
     * @param player The player to check.
     * @return The last movement time of the player.
     */
    public long lastMovement(Player player) {
        UUID uuid = player.getUniqueId();
        long last = this.getLastMovement().getOrDefault(uuid, 0L);
        // Set last movement to now if not set
        if (last == 0L) {
            detectedMovement(player);
            return System.currentTimeMillis();
        }
        return last;
    }
    /**
     * Checks if a player is AFK.
     * @param uuid The UUID of the player to check.
     * @return True if the player is AFK, false otherwise.
     */
    public boolean status(UUID uuid) {
        return this.getStatus().getOrDefault(uuid, false);
    }

    /**
     * Sets the AFK status of a player.
     * @param player The player to set the status for.
     * @param status The AFK status to set.
     */
    public void status(Player player, boolean status) {
        UUID uuid = player.getUniqueId();
        boolean oldStatus = this.getStatus().getOrDefault(uuid, false);
        if (oldStatus == status) {
            return;
        }

        this.getStatus().put(uuid, status);
        // AFK Message & Info
        if (status) {
            Message.broadcast("{0} ist jetzt AFK.", player.getName());

            updateTeam(player);
            return;
        }
        Message.broadcast("{0} ist nicht mehr AFK.", player.getName());

        updateTeam(player);
    }

    public Team getTeam(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("AFK");
        if (team == null) {
            team = scoreboard.registerNewTeam("AFK");
            team.prefix(Message.text("[AFK] "));
        }
        return team;
    }

    public void updateTeam(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            Team team = getTeam(other);
            if (status(player.getUniqueId())) {
                team.addPlayer(player);
                continue;
            }
            team.removePlayer(player);
        }
    }

    public void checkStatus(Player player) {
        long now = System.currentTimeMillis();
        long last = lastMovement(player);
        long diff = (now - last) / 1000;

        if (diff >= AFK_TIME_SECONDS) {
            status(player, true);
        }
    }

    public void startAFKCheckTask() {
        CyberEnte.getInstance()
                .getScheduledExecutorService()
                .scheduleAtFixedRate(
                        () -> Bukkit.getOnlinePlayers().forEach(this::checkStatus), 0L, 1L, TimeUnit.SECONDS);
    }
}
