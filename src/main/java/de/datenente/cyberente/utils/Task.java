package de.datenente.cyberente.utils;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.hibernate.Databases;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

@Getter
public class Task {

    static Task instance;

    final Logger logger;
 final boolean folia;

    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    public static synchronized Task getInstance() {
        if (instance == null) {
            CyberEnte cyberEnte = CyberEnte.getInstance();
            Logger logger = cyberEnte.getLogger();
            instance = new Task(logger);
        }
        return instance;
    }

    public Task(final Logger logger) {
        this.logger = logger;

this.folia = classExists(
            "io.papermc.paper.threadedregions.RegionizedServer"
    );
getLogger().info("Task init: Server-Type: %s".formatted(folia ? "Folia" : "Paper"));

    }

    public void cancel() {
        scheduledExecutorService.shutdown();
    }

     boolean classExists(String className) {
    try {
        Class.forName(className);
        return true;
    } catch (ClassNotFoundException exception) {
        return false;
    }
}
}
