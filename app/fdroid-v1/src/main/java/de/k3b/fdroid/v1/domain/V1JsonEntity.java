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

package de.k3b.fdroid.v1.domain;

import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;
import de.k3b.fdroid.domain.interfaces.Enitity;

/**
 * A FDroid-Catalog-v1-Json format {@link Enitity} used to import
 * F-Drdoid Catalog Data in the "V1" json format.
 * <p>
 * Each Xxx{@link V1JsonEntity} has a corresponding Xxx{@link DatabaseEntityWithId}
 * and there is a Xxx{@link UpdateService} that transfers Xxx{@link V1JsonEntity} to Xxx{@link DatabaseEntityWithId}
 */
public interface V1JsonEntity extends Enitity {
}
