/*
 * Copyright (c) 2022-2023 by k3b.
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Generated;

import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

/**
 * Data for a FDroid-Repository (read from FDroid-Catalog-v1-Json format).
 * <p>
 * The {@link V1JsonEntity} {@link V1Repo} correspond to the
 * {@link IDatabaseEntityWithId} {@link de.k3b.fdroid.domain.entity.Repo}.
 */
@Generated("jsonschema2pojo")
@SuppressWarnings("unused")
public class V1Repo extends RepoCommon implements V1JsonEntity {

    private List<String> mirrors = null;

    public List<String> getMirrors() {
        return mirrors;
    }

    public void setMirrors(List<String> mirrors) {
        this.mirrors = mirrors;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "mirrors", this.mirrors);
    }
}
