/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.fdroid.domain.repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

/**
 * Android independent interfaces to use the Database.
 * <p>
 * Persists {@link App} (that implements {@link IDatabaseEntityWithId}) in the Database.
 */
public interface AppRepository extends Repository, AppRepositoryFindDynamic {
    void insert(App app);

    void update(App app);

    default void save(App app) {
        if (mustInsert(app)) {
            insert(app);
        } else {
            update(app);
        }
    }

    default boolean mustInsert(App app) {
        return app.getId() == 0;
    }

    void delete(App app);

    App findByPackageName(String packageName);

    List<App> findAll();

    List<App> findByIds(List<Integer> ids);
}
