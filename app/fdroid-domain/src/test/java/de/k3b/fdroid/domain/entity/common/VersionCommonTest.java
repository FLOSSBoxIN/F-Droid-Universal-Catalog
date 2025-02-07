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
package de.k3b.fdroid.domain.entity.common;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionCommonTest {

    @Test
    public void compareByVersionCodeDescending() {
        VersionCommon v4 = new VersionCommon();
        v4.setVersionCode(4);

        VersionCommon v5 = new VersionCommon();
        v5.setVersionCode(5);

        assertTrue("descending sort: v5 comes before v4", VersionCommon.compareByVersionCodeDescending().compare(v5, v4) < 0);
    }
}