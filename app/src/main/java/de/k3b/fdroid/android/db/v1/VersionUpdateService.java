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

package de.k3b.fdroid.android.db.v1;

import org.fdroid.model.VersionCommon;

import de.k3b.fdroid.android.db.AppDao;
import de.k3b.fdroid.android.db.VersionDao;
import de.k3b.fdroid.android.model.Version;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class VersionUpdateService {
    private final AppDao appDao;
    private final VersionDao versionDao;

    public VersionUpdateService(AppDao appDao, VersionDao versionDao) {
        this.appDao = appDao;
        this.versionDao = versionDao;
    }

    public Version update(int repoId, String packageName, org.fdroid.model.v1.Version v1Version) {
        Version roomVersion = versionDao.findForPackageNameAndVersionCode(repoId, packageName, v1Version.getVersionCode());
        if (roomVersion == null) {
            Integer appId = appDao.findIdByPackageName(repoId, packageName);
            if (appId != null) {
                roomVersion = new Version();
                roomVersion.appId = appId;
                VersionCommon.copyCommon(roomVersion, v1Version);
                versionDao.insertAll(roomVersion);
            }
        } else {
            VersionCommon.copyCommon(roomVersion, v1Version);
            versionDao.updateAll(roomVersion);
        }

        return roomVersion;
    }
}
