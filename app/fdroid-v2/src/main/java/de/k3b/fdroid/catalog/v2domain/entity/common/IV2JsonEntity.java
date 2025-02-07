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
package de.k3b.fdroid.catalog.v2domain.entity.common;

import de.k3b.fdroid.domain.interfaces.Enitity;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

/**
 * A FDroid-Catalog-v2-Json format {@link Enitity} used to import
 * F-Drdoid Catalog Data in the "V2" json format.
 * <p>
 * Each Xxx{@link IV2JsonEntity} has a corresponding
 * Xxx{@link IDatabaseEntityWithId}
 * and there is a Xxx{@link IV2UpdateService} that transfers
 * Xxx{@link IV2JsonEntity} to Xxx{@link IDatabaseEntityWithId}
 */
public interface IV2JsonEntity extends Enitity {
}
