/*
 * Copyright (c) 2022 by k3b.
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

import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

/**
 * Android independent interfaces to use the Database.
 * <p>
 * Persists {@link Version} (that implements {@link IDatabaseEntityWithId}) in the Database.
 */
public interface VersionRepository extends AppDetailRepository<Version> {
    void insert(Version version);

    void update(Version version);

    void delete(Version version);

    List<Version> findByAppId(int appId);

    /**
     * @param sdkversion 0 means all versions
     * @param nativeCode null means all native code. non null means: all versoins that are nativeCode
     *                   independent or that is contained. Must include preceeding/trailing "%".
     * @return pseuddo versions, one entry per app/repo combination populated with found min/max values
     */
    List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode);

    List<Version> findByMinSdkAndAppIds(int minSdk, List<Integer> appIds);

    default void save(Version o) {
        if (mustInsert(o)) {
            insert(o);
        } else {
            update(o);
        }
    }

    default boolean mustInsert(Version o) {
        return o.getId() == 0;
    }

}
