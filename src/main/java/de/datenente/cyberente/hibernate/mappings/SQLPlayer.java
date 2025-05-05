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
package de.datenente.cyberente.hibernate.mappings;

import de.datenente.cyberente.utils.hibernate.TimestampEntity;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "players")
@NamedQuery(name = SQLPlayer.HQL_GET_SEARCH_BY_UUID, query = "FROM SQLPlayer sqlPlayer WHERE sqlPlayer.uuid = :uuid")
@NamedQuery(
        name = SQLPlayer.HQL_GET_SEARCH_BY_NAME,
        query = "FROM SQLPlayer sqlPlayer WHERE LOWER(sqlPlayer.name) = LOWER(:name)")
@NamedQuery(
        name = SQLPlayer.HQL_GET_LIST_SEARCH_BY_NAME,
        query = "FROM SQLPlayer sqlPlayer WHERE LOWER(sqlPlayer.name) LIKE LOWER(:name)")
@StaticMetamodel(SQLPlayer.class)
public class SQLPlayer extends TimestampEntity implements Serializable {

    public static final String HQL_GET_SEARCH_BY_UUID = "@HQL_GET_SQLPLAYER_SEARCH_BY_UUID";
    public static final String HQL_GET_SEARCH_BY_NAME = "@HQL_GET_SQLPLAYER_SEARCH_BY_NAME";
    public static final String HQL_GET_LIST_SEARCH_BY_NAME = "@HQL_GET_SQLPLAYER_LIST_SEARCH_BY_NAME";

    public SQLPlayer(final String uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String uuid;

    @Column(nullable = false)
    String name = "NO_VALUE";

    @Column(nullable = false)
    LocalDateTime firstJoin = LocalDateTime.now();

    LocalDateTime lastJoin;
    LocalDateTime lastLeave;

    @Column(nullable = false)
    Long playtime = 0L;
}
