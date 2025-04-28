package de.datenente.cyberente.config;

import de.datenente.cyberente.config.mappings.StorageObject;
import de.datenente.cyberente.utils.config.JsonDocument;
import java.io.File;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class StorageConfig extends JsonDocument<StorageObject> {

    @Getter
    static StorageConfig instance;

    public StorageConfig(Logger pluginLogger, File dataFolder) {
        super(pluginLogger, dataFolder, StorageObject.class, "storage.json");

        synchronized (this) {
            instance = this;
        }
    }

    @Override
    public void loadContent() {
        this.reload();
    }

    public void getLocation() {}
}
