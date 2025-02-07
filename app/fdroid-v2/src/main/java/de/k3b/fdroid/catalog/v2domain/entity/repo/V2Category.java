/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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
package de.k3b.fdroid.catalog.v2domain.entity.repo;
// V2Category.java

import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("unused")
public class V2Category {
    @Nullable
    private Map<String, V2File> icon;
    @Nullable
    private final Map<String, String> name;
    @Nullable
    private Map<String, String> description;

    public V2Category(@Nullable Map<String, String> name) {
        this.name = name;
    }

    @Nullable
    public Map<String, V2File> getIcon() {
        return this.icon;
    }

    @Nullable
    public Map<String, String> getName() {
        return this.name;
    }

    @Nullable
    public Map<String, String> getDescription() {
        return this.description;
    }

    @Nullable
    public String toString() {
        return "V2Category{icon=" + this.icon + ", name=" + this.name + ", description=" + this.description + "}";
    }
}
