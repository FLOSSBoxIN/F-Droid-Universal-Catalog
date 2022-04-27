/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.fdroid.domain;


import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.interfaces.ItemWithId;
// import de.k3b.fdroid.domain.common.PojoCommon;

/**
 * Android independant: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id"),@androidx.room.Index(value = "name", unique = true)})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class Category extends PojoCommon implements ItemWithId {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "name", this.name);
        super.toStringBuilder(sb);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
