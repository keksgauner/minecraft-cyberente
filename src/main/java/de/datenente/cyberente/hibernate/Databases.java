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
package de.datenente.cyberente.hibernate;

import de.datenente.cyberente.CyberEnte;
import de.datenente.cyberente.config.MySQLConfig;
import de.datenente.cyberente.config.mappings.MySQLObject;
import de.datenente.cyberente.hibernate.database.PlayerDatabase;
import de.datenente.cyberente.utils.hibernate.HibernateConnection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class Databases {

    @Getter
    static Databases instance;

    final HibernateConnection hibernateConnection;
    ScheduledFuture<?> databaseConnectTask;

    // All databases
    PlayerDatabase playerDatabase;

    public Databases(Logger logger) {
        this.hibernateConnection = new HibernateConnection(logger);

        synchronized (this) {
            instance = this;
        }
    }

    public void openDatabaseConnection() {
        final MySQLObject mysqlObject = MySQLConfig.getInstance().getStorage();

        this.getHibernateConnection()
                .addConnectionSettings(
                        mysqlObject.getHost(),
                        mysqlObject.getPort(),
                        mysqlObject.getDatabase(),
                        mysqlObject.getUsername(),
                        mysqlObject.getPassword(),
                        mysqlObject.isShowSql());
        this.getHibernateConnection().buildServiceRegistry().buildMetadataSources();

        // Register all databases
        playerDatabase = new PlayerDatabase(this.getHibernateConnection());

        this.getHibernateConnection().buildSessionFactory().test();
    }

    public void startDatabaseConnectTask() {
        this.databaseConnectTask = CyberEnte.getInstance()
                .getScheduledExecutorService()
                .scheduleAtFixedRate(
                        () -> {
                            if (getHibernateConnection() == null) {
                                openDatabaseConnection();
                            }

                            if (!getHibernateConnection().test()) {
                                getHibernateConnection().close();
                                openDatabaseConnection();
                            }
                        },
                        2,
                        15,
                        TimeUnit.MINUTES);
    }
}
