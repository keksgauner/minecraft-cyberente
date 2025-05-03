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
package de.datenente.cyberente.utils.hibernate;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;

@Setter
@Getter
public class HibernateConnection {

    final Logger logger;
    SessionFactory sessionFactory;
    StandardServiceRegistryBuilder serviceRegistryBuilder;
    StandardServiceRegistry serviceRegistry;
    MetadataSources metadataSources;

    public HibernateConnection(final Logger logger) {
        this.logger = logger;
        this.serviceRegistryBuilder = new StandardServiceRegistryBuilder();
    }

    public HibernateConnection addConnectionSettings(
            final String host,
            final int port,
            final String database,
            final String username,
            final String password,
            final boolean showSql) {
        final Map<String, Object> settings = Map.of(
                // JdbcSettings.JAKARTA_JDBC_DRIVER,
                // "com.mysql.cj.jdbc.Driver",
                // JdbcSettings.DIALECT,
                // "org.hibernate.dialect.MySQLDialect",
                JdbcSettings.JAKARTA_JDBC_URL,
                "jdbc:mariadb://" + host + ":" + port + "/" + database,
                JdbcSettings.JAKARTA_JDBC_USER,
                username,
                JdbcSettings.JAKARTA_JDBC_PASSWORD,
                password,
                SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                "UPDATE",
                JdbcSettings.ISOLATION,
                "READ_COMMITTED",
                JdbcSettings.AUTOCOMMIT,
                true,
                JdbcSettings.SHOW_SQL,
                showSql,
                JdbcSettings.FORMAT_SQL,
                true);

        settings.forEach(this.getServiceRegistryBuilder()::applySetting);
        return this;
    }

    public HibernateConnection buildServiceRegistry() {
        this.serviceRegistry = this.getServiceRegistryBuilder().build();
        return this;
    }

    public HibernateConnection buildMetadataSources() {
        if (this.getServiceRegistry() == null) {
            this.getLogger().severe("ServiceRegistry is null");
            return this;
        }
        this.metadataSources = new MetadataSources(this.getServiceRegistry());
        return this;
    }

    public HibernateConnection buildSessionFactory() {
        if (this.getMetadataSources() == null) {
            this.getLogger().severe("MetadataSources is null");
            return this;
        }
        this.sessionFactory = this.getMetadataSources().buildMetadata().buildSessionFactory();
        return this;
    }

    public Session openSession() {
        if (this.getSessionFactory() == null) {
            this.getLogger().severe("SessionFactory is null");
            return null;
        }
        return this.getSessionFactory().openSession();
    }

    public boolean test() {
        if (this.getSessionFactory() == null) {
            this.getLogger().severe("SessionFactory is null");
            return false;
        }
        if (this.getSessionFactory().isClosed()) {
            this.getLogger().severe("SessionFactory is closed");
            return false;
        }

        try (final Session session = this.openSession()) {
            session.beginTransaction();
            if (session.getTransaction().isActive()) {
                return true;
            }
            this.getLogger().warning("MySQL connection not working!");
        } catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, "Could not connect to MySQL!", ex);
        }
        return false;
    }

    public void close() {
        if (this.getSessionFactory() != null) {
            this.getSessionFactory().close();
        }
    }
}
