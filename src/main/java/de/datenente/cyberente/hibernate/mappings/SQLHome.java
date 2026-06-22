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
package de.datenente.cyberente.hibernate.mappings;

import de.datenente.cyberente.utils.hibernate.TimestampEntity;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "homes",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_homes_player_name",
                    columnNames = {"playerUuid", "homeName"})
        })
@NamedQuery(
        name = SQLHome.HQL_GET_SEARCH_BY_PLAYER_AND_NAME,
        query = "FROM SQLHome sqlHome WHERE sqlHome.playerUuid = :playerUuid AND sqlHome.homeName = :homeName")
@NamedQuery(
        name = SQLHome.HQL_GET_LIST_BY_PLAYER_UUID,
        query = "FROM SQLHome sqlHome WHERE sqlHome.playerUuid = :playerUuid")
@StaticMetamodel(SQLHome.class)
public class SQLHome extends TimestampEntity implements Serializable {

    public static final String HQL_GET_SEARCH_BY_PLAYER_AND_NAME = "@HQL_GET_SQLHOME_SEARCH_BY_PLAYER_AND_NAME";
    public static final String HQL_GET_LIST_BY_PLAYER_UUID = "@HQL_GET_SQLHOME_LIST_BY_PLAYER_UUID";

    public SQLHome(
            final String playerUuid,
            final String homeName,
            final String worldName,
            final double x,
            final double y,
            final double z,
            final float yaw,
            final float pitch) {
        this.playerUuid = playerUuid;
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String playerUuid;

    @Column(nullable = false)
    String homeName;

    @Column(nullable = false)
    String worldName;

    @Column(nullable = false)
    double x;

    @Column(nullable = false)
    double y;

    @Column(nullable = false)
    double z;

    @Column(nullable = false)
    float yaw;

    @Column(nullable = false)
    float pitch;
}
