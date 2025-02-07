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

package de.k3b.fdroid.android;

import androidx.work.NetworkType;

/**
 * Android sprecific golabl configuration settings
 */
public class Global extends de.k3b.fdroid.Global {
    public static final NetworkType DOWNLOAD_NETWORK_TYPE = NetworkType.CONNECTED;

    /**
     * true verifying-download plus seperate file import.
     * false non-verifying import while downloading (android 4.2)
     */
    // public static final boolean USE_VERIFYING_DOWNLOAD = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
    public static final boolean USE_VERIFYING_DOWNLOAD = true;
}
