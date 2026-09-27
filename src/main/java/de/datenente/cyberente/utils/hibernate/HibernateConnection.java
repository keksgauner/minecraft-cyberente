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
package de.datenente.cyberente.utils.hibernate;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;

@Getter
public class HibernateConnection {

    final Logger logger;
    SessionFactory sessionFactory;
    StandardServiceRegistryBuilder serviceRegistryBuilder;
    StandardServiceRegistry serviceRegistry;
    MetadataSources metadataSources;

    private final Map<String, Object> connectionSettings = new HashMap<>();
    private final Set<Class<?>> annotatedClasses = new LinkedHashSet<>();

    public HibernateConnection(final Logger logger) {
        this.logger = logger;
        this.serviceRegistryBuilder = new StandardServiceRegistryBuilder();
    }

    public synchronized HibernateConnection addConnectionSettings(
            final String host,
            final int port,
            final String database,
            final String username,
            final String password,
            final boolean showSql) {
        this.connectionSettings.put(
                JdbcSettings.JAKARTA_JDBC_URL,
                "jdbc:mariadb://" + host + ":" + port + "/" + database
                        + "?autoReconnect=true&connectTimeout=5000&socketTimeout=30000&tcpKeepAlive=true");
        this.connectionSettings.put(JdbcSettings.JAKARTA_JDBC_USER, username);
        this.connectionSettings.put(JdbcSettings.JAKARTA_JDBC_PASSWORD, password);
        this.connectionSettings.put(SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, "UPDATE");
        this.connectionSettings.put(JdbcSettings.ISOLATION, "READ_COMMITTED");
        this.connectionSettings.put(JdbcSettings.AUTOCOMMIT, true);
        this.connectionSettings.put(JdbcSettings.SHOW_SQL, showSql);
        this.connectionSettings.put(JdbcSettings.FORMAT_SQL, true);

        return this;
    }

    public synchronized HibernateConnection addAnnotatedClass(final Class<?> annotatedClass) {
        if (annotatedClass != null) {
            this.annotatedClasses.add(annotatedClass);
            if (this.metadataSources != null) {
                this.metadataSources.addAnnotatedClass(annotatedClass);
            }
        }
        return this;
    }

    public synchronized HibernateConnection buildServiceRegistry() {
        this.serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        this.connectionSettings.forEach(this.serviceRegistryBuilder::applySetting);
        this.serviceRegistry = this.serviceRegistryBuilder.build();
        return this;
    }

    public synchronized HibernateConnection buildMetadataSources() {
        if (this.getServiceRegistry() == null) {
            this.getLogger().severe("ServiceRegistry is null");
            return this;
        }
        this.metadataSources = new MetadataSources(this.getServiceRegistry());
        for (Class<?> clazz : this.annotatedClasses) {
            this.metadataSources.addAnnotatedClass(clazz);
        }
        return this;
    }

    public synchronized HibernateConnection buildSessionFactory() {
        if (this.getMetadataSources() == null) {
            this.getLogger().severe("MetadataSources is null");
            return this;
        }
        this.sessionFactory = this.getMetadataSources().buildMetadata().buildSessionFactory();
        return this;
    }

    public synchronized boolean reconnect() {
        close();
        try {
            buildServiceRegistry();
            buildMetadataSources();
            buildSessionFactory();
            return test();
        } catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Failed to connect to database: " + ex.getMessage(), ex);
            close();
            return false;
        }
    }

    public Session openSession() {
        if (this.getSessionFactory() == null || this.getSessionFactory().isClosed()) {
            this.getLogger().warning("SessionFactory is not available. Attempting reconnect...");
            if (!reconnect()) {
                this.getLogger().severe("SessionFactory is null or closed and reconnect failed.");
                return null;
            }
        }

        try {
            return this.getSessionFactory().openSession();
        } catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Failed to open session. Attempting reconnect...", ex);
            if (reconnect()) {
                try {
                    return this.getSessionFactory().openSession();
                } catch (Exception retryEx) {
                    this.getLogger().log(Level.SEVERE, "Failed to open session after reconnect!", retryEx);
                }
            }
            return null;
        }
    }

    public boolean test() {
        if (this.getSessionFactory() == null || this.getSessionFactory().isClosed()) {
            return false;
        }

        try (final Session session = this.getSessionFactory().openSession()) {
            Object result = session.createNativeQuery("SELECT 1", Object.class).getSingleResult();
            return result != null;
        } catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "MySQL connection test failed: " + ex.getMessage());
            return false;
        }
    }

    public synchronized void close() {
        if (this.sessionFactory != null) {
            try {
                if (!this.sessionFactory.isClosed()) {
                    this.sessionFactory.close();
                }
            } catch (Exception ex) {
                this.getLogger().log(Level.WARNING, "Error closing SessionFactory", ex);
            }
            this.sessionFactory = null;
        }

        if (this.serviceRegistry != null) {
            try {
                StandardServiceRegistryBuilder.destroy(this.serviceRegistry);
            } catch (Exception ex) {
                this.getLogger().log(Level.WARNING, "Error destroying ServiceRegistry", ex);
            }
            this.serviceRegistry = null;
        }

        this.metadataSources = null;
        this.serviceRegistryBuilder = null;
    }
}
