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
package de.datenente.cyberente.hibernate.database;

import de.datenente.cyberente.hibernate.mappings.SQLHome;
import de.datenente.cyberente.utils.hibernate.HibernateConnection;
import de.datenente.cyberente.utils.hibernate.HibernateDatabase;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Getter
public class HomeDatabase implements HibernateDatabase {
    final HibernateConnection hibernateConnection;

    public HomeDatabase(final HibernateConnection hibernateConnection) {
        this.hibernateConnection = hibernateConnection;
        this.getHibernateConnection().getMetadataSources().addAnnotatedClass(SQLHome.class);
    }

    public List<SQLHome> getHomes(final UUID playerUuid) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLHome> homeQuery =
                    session.createNamedQuery(SQLHome.HQL_GET_LIST_BY_PLAYER_UUID, SQLHome.class);
            homeQuery.setParameter("playerUuid", playerUuid.toString());
            return homeQuery.getResultList();
        }
    }

    public SQLHome getHome(final UUID playerUuid, final String homeName) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLHome> homeQuery =
                    session.createNamedQuery(SQLHome.HQL_GET_SEARCH_BY_PLAYER_AND_NAME, SQLHome.class);
            homeQuery.setParameter("playerUuid", playerUuid.toString());
            homeQuery.setParameter("homeName", normalizeHomeName(homeName));
            List<SQLHome> homes = homeQuery.getResultList();
            if (homes.isEmpty()) {
                return null;
            }
            return homes.getFirst();
        }
    }

    public SQLHome createOrUpdate(
            final UUID playerUuid,
            final String homeName,
            final String worldName,
            final double x,
            final double y,
            final double z,
            final float yaw,
            final float pitch) {
        SQLHome sqlHome = getHome(playerUuid, homeName);
        String normalizedHomeName = normalizeHomeName(homeName);

        if (sqlHome == null) {
            sqlHome = new SQLHome(playerUuid.toString(), normalizedHomeName, worldName, x, y, z, yaw, pitch);
        } else {
            sqlHome.setWorldName(worldName);
            sqlHome.setX(x);
            sqlHome.setY(y);
            sqlHome.setZ(z);
            sqlHome.setYaw(yaw);
            sqlHome.setPitch(pitch);
        }

        try (final Session session = this.getHibernateConnection().openSession()) {
            session.beginTransaction();
            session.merge(sqlHome);
            session.getTransaction().commit();
            return sqlHome;
        }
    }

    public boolean deleteHome(final UUID playerUuid, final String homeName) {
        SQLHome sqlHome = getHome(playerUuid, homeName);
        if (sqlHome == null) {
            return false;
        }

        try (final Session session = this.getHibernateConnection().openSession()) {
            session.beginTransaction();
            session.remove(session.merge(sqlHome));
            session.getTransaction().commit();
            return true;
        }
    }

    public static String normalizeHomeName(final String homeName) {
        return homeName.trim().toLowerCase();
    }
}
