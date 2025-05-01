package de.datenente.cyberente.special;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.StorageConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayTime {
    public void startTimer()
    {
        CyberEnte.getInstance()
                .getScheduledExecutorService()
                .schedule(new Runnable() {
                    @Override
                    public void run() {
                        StorageConfig config = StorageConfig.getInstance();
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            config.addPlayTime(player.getUniqueId(), 1L);
                        }
                        config.save();
                    }
                }, 1L, TimeUnit.MINUTES);
    }
}
