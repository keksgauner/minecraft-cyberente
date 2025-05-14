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
package de.datenente.cyberente.config;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.mappings.MySQLObject;
import de.datenente.cyberente.utils.config.JsonDocument;
import java.io.File;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class MySQLConfig extends JsonDocument<MySQLObject> {

    static MySQLConfig instance;

    public static MySQLConfig getInstance() {
        if (instance == null) {
            CyberEnte cyberEnte = CyberEnte.getInstance();
            Logger logger = cyberEnte.getLogger();
            File dataFolder = cyberEnte.getDataFolder();
            instance = new MySQLConfig(logger, dataFolder);
        }
        return instance;
    }

    public MySQLConfig(Logger pluginLogger, File dataFolder) {
        super(pluginLogger, dataFolder, MySQLObject.class, "mysql.json");
    }

    @Override
    public void loadContent() {
        this.save();
    }
}
