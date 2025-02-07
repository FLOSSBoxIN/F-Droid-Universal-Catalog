/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.catalog.v1domain.entity;

import de.k3b.fdroid.domain.interfaces.Enitity;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

/**
 * A FDroid-Catalog-v1-Json format {@link Enitity} used to read
 * F-Drdoid Catalog Data in the "V1" json format.
 * <p>
 * Each Xxx{@link V1JsonEntity} has a corresponding Xxx{@link IDatabaseEntityWithId}
 * and there is a Xxx{@link IV1UpdateService} that updates Xxx{@link IDatabaseEntityWithId} from {@link V1JsonEntity}
 */
public interface IV1UpdateService {
}
