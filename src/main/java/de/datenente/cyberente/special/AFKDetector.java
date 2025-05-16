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
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class AFKDetector {
    final HashMap<UUID, Long> lastMovement = new HashMap<>();
    final HashMap<UUID, Boolean> afkStatus = new HashMap<>();

    static final long AFK_TIME_SECONDS = 10;

    static AFKDetector instance;

    public static AFKDetector getInstance() {
        if (instance == null) {
            instance = new AFKDetector();
        }
        return instance;
    }

    public void cleanUp(UUID uuid) {
        lastMovement.remove(uuid);
        afkStatus.remove(uuid);
    }

    public void detectMovement(Player player) {
        UUID uuid = player.getUniqueId();
        getLastMovement().put(uuid, System.currentTimeMillis());

        if (getAfkStatus().getOrDefault(uuid, false)) {
            getAfkStatus().put(uuid, false);
            Bukkit.broadcast(Message.text("Spieler {0} ist nicht mehr AFK.", player.getName()));
        }
    }

    public void startAFKCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    long last = lastMovement.getOrDefault(uuid, now);
                    long diff = (now - last) / 1000;

                    if (diff >= AFK_TIME_SECONDS && !afkStatus.getOrDefault(uuid, false)) {
                        afkStatus.put(uuid, true);
                        Bukkit.broadcast(Message.text("Spieler {0} ist jetzt AFK.", player.getName()));
                    }
                }
            }
        }.runTaskTimer(CyberEnte.getInstance(), 0L, 20L);
    }
}
