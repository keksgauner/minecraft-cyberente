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
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class DayNight {

    BossBar bossBar;

    static DayNight instance;

    public static DayNight getInstance() {
        if (instance == null) {
            instance = new DayNight();
        }
        return instance;
    }

    public void startDayNightCycleTask() {
        if (bossBar != null) return;

        bossBar = Bukkit.createBossBar("Tagesverlauf", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

        CyberEnte.getInstance()
                .getScheduledExecutorService()
                .scheduleAtFixedRate(
                        () -> {
                            World world = Bukkit.getWorlds().getFirst(); // Hauptwelt
                            long time = world.getTime(); // 0 - 23999
                            double progress = time / 24000.0;
                            bossBar.setProgress(progress);

                            if (time < 12000) {
                                long remainingTicks = 12000 - time;
                                long totalSeconds = remainingTicks / 20;
                                long minutes = totalSeconds / 60;
                                long seconds = totalSeconds % 60;

                                bossBar.setTitle("☀ Tag: noch " + minutes + " Minuten " + seconds + " Sekunden");
                                bossBar.setColor(BarColor.YELLOW);
                            } else {
                                long remainingTicks = 24000 - time;
                                long totalSeconds = remainingTicks / 20;
                                long minutes = totalSeconds / 60;
                                long seconds = totalSeconds % 60;

                                bossBar.setTitle("🌙 Nacht: noch " + minutes + " Minuten " + seconds + " Sekunden");
                                bossBar.setColor(BarColor.PURPLE);
                            }
                        },
                        0L,
                        1L,
                        TimeUnit.SECONDS);
    }

    public void removeAll() {
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
    }

    public void addPlayer(Player player) {
        if (bossBar != null) bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        if (bossBar != null) bossBar.removePlayer(player);
    }
}
