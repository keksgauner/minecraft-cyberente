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
package de.datenente.cyberente.hibernate.database;

import de.datenente.cyberente.hibernate.mappings.SQLPlayer;
import de.datenente.cyberente.utils.hibernate.HibernateConnection;
import de.datenente.cyberente.utils.hibernate.HibernateDatabase;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Getter
public class PlayerDatabase implements HibernateDatabase {
    final HibernateConnection hibernateConnection;

    public PlayerDatabase(final HibernateConnection hibernateConnection) {
        this.hibernateConnection = hibernateConnection;
        this.getHibernateConnection().getMetadataSources().addAnnotatedClass(SQLPlayer.class);
    }

    public SQLPlayer createOrUpdate(final UUID uuid, final String name) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLPlayer> playerQuery =
                    session.createNamedQuery(SQLPlayer.HQL_GET_SEARCH_BY_UUID, SQLPlayer.class);
            playerQuery.setParameter("uuid", uuid.toString());

            if (playerQuery.getResultList().isEmpty()) {
                final SQLPlayer sqlPlayer = new SQLPlayer(uuid.toString(), name);
                session.beginTransaction();
                session.persist(sqlPlayer);
                session.getTransaction().commit();
                return sqlPlayer;
            }
            final SQLPlayer sqlPlayer = playerQuery.getSingleResult();
            sqlPlayer.setName(name);

            session.beginTransaction();
            session.merge(sqlPlayer);
            session.getTransaction().commit();
            return sqlPlayer;
        }
    }

    public boolean existPlayer(final String playerName) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLPlayer> playerQuery =
                    session.createNamedQuery(SQLPlayer.HQL_GET_SEARCH_BY_NAME, SQLPlayer.class);
            playerQuery.setParameter("name", playerName);
            return !playerQuery.getResultList().isEmpty();
        }
    }

    public List<SQLPlayer> searchPlayer(final String playerName, final int firstResult, final int limit) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLPlayer> playerQuery =
                    session.createNamedQuery(SQLPlayer.HQL_GET_LIST_SEARCH_BY_NAME, SQLPlayer.class);
            playerQuery.setParameter("name", playerName + "%");

            playerQuery.setFirstResult(firstResult);
            playerQuery.setMaxResults(limit);

            return playerQuery.getResultList();
        }
    }

    public SQLPlayer getPlayer(final UUID uuid) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLPlayer> playerQuery =
                    session.createNamedQuery(SQLPlayer.HQL_GET_SEARCH_BY_UUID, SQLPlayer.class);
            playerQuery.setParameter("uuid", uuid.toString());
            return playerQuery.getSingleResult();
        }
    }

    public SQLPlayer getPlayer(final String name) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            final Query<SQLPlayer> playerQuery =
                    session.createNamedQuery(SQLPlayer.HQL_GET_SEARCH_BY_NAME, SQLPlayer.class);
            playerQuery.setParameter("name", name);
            return playerQuery.getSingleResult();
        }
    }

    public void savePlayer(final SQLPlayer player) {
        try (final Session session = this.getHibernateConnection().openSession()) {
            session.beginTransaction();
            session.merge(player);
            session.getTransaction().commit();
        }
    }
}
