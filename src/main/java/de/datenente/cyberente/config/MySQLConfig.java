package de.datenente.cyberente.config;

import de.datenente.cyberente.config.mappings.MySQLObject;
import de.datenente.cyberente.utils.config.JsonDocument;
import lombok.Getter;

import java.io.File;
import java.util.logging.Logger;

@Getter
public class MySQLConfig extends JsonDocument<MySQLObject> {

    @Getter
    static MySQLConfig instance;

    public MySQLConfig(Logger pluginLogger, File dataFolder) {
        super(
                pluginLogger,
                dataFolder,
                MySQLObject.class,
                "mysql.json");

        synchronized (this) {
            instance = this;
        }
    }

    @Override
    public void loadContent() {
        this.reload();
    }
}