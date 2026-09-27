/*
 * MIT License
 *
 * Copyright (c) 2025 - 2026 KeksGauner, CyberEnte
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
import de.datenente.cyberente.configuration.MySQLConfig;
import de.datenente.cyberente.configuration.mappings.MySQLObject;
import de.datenente.cyberente.hibernate.database.HomeDatabase;
import de.datenente.cyberente.hibernate.database.PlayerDatabase;
import de.datenente.cyberente.utils.hibernate.HibernateConnection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class Databases {

    static Databases instance;

    public static Databases getInstance() {
        if (instance == null) {
            CyberEnte cyberEnte = CyberEnte.getInstance();
            Logger logger = cyberEnte.getLogger();
            instance = new Databases(logger);
        }
        return instance;
    }

    final HibernateConnection hibernateConnection;
    ScheduledFuture<?> databaseConnectTask;

    // All databases
    PlayerDatabase playerDatabase;
    HomeDatabase homeDatabase;

    public Databases(Logger logger) {
        this.hibernateConnection = new HibernateConnection(logger);
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

        // Register all databases
        if (playerDatabase == null) {
            playerDatabase = new PlayerDatabase(this.getHibernateConnection());
        }
        if (homeDatabase == null) {
            homeDatabase = new HomeDatabase(this.getHibernateConnection());
        }

        boolean connected = this.getHibernateConnection().reconnect();
        if (connected) {
            this.getHibernateConnection().getLogger().info("Database connection established.");
        } else {
            this.getHibernateConnection()
                    .getLogger()
                    .warning("Initial database connection failed. Background task will retry.");
        }
    }

    public void startDatabaseConnectTask() {
        if (this.databaseConnectTask != null && !this.databaseConnectTask.isCancelled()) {
            this.databaseConnectTask.cancel(false);
        }

        this.databaseConnectTask = CyberEnte.getInstance()
                .getScheduledExecutorService()
                .scheduleWithFixedDelay(
                        () -> {
                            try {
                                if (getHibernateConnection() == null) {
                                    openDatabaseConnection();
                                    return;
                                }

                                if (!getHibernateConnection().test()) {
                                    CyberEnte.getInstance()
                                            .getLogger()
                                            .warning("Database connection lost! Attempting to reconnect...");
                                    getHibernateConnection().reconnect();
                                }
                            } catch (Throwable t) {
                                CyberEnte.getInstance()
                                        .getLogger()
                                        .log(
                                                Level.WARNING,
                                                "Error in database connection check task: " + t.getMessage(),
                                                t);
                            }
                        },
                        10,
                        30,
                        TimeUnit.SECONDS);
    }
}
