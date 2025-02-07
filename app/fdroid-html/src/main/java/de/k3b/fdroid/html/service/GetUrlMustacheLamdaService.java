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
package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.service.AppWithDetailsPagerService;
import de.k3b.fdroid.domain.service.CacheServiceInteger;

/**
 * Translates local url to repo relative url.
 * <p>
 * Example {{#getUrl}}{{apkName}}{{/getUrl}} becomes
 *
 * @see <a href="https://apt.izzysoft.de/fdroid/repo/com.inator.calculator_4.apk">com.inator.calculator_4.apk</a>
 * if context is Version
 */
public class GetUrlMustacheLamdaService implements Mustache.Lambda {
    private final CacheServiceInteger<Repo> repoCacheService;

    public GetUrlMustacheLamdaService(CacheServiceInteger<Repo> repoCacheService) {
        this.repoCacheService = repoCacheService;
    }

    @Override
    public void execute(Template.Fragment frag, Writer out) throws IOException {
        out.write(calculateUrl(frag.context(), frag.execute()));
    }

    private String calculateUrl(Object context, String parameter) {
        int repoId = 0;
        if (context instanceof Version) {
            repoId = ((Version) context).getRepoId();
        } else if (context instanceof App) {
            repoId = ((App) context).getResourceRepoId();
        } else if (context instanceof Repo) {
            repoId = ((Repo) context).getId();
        } else if (context instanceof AppWithDetailsPagerService.AppItemAtOffset) {
            repoId = ((AppWithDetailsPagerService.AppItemAtOffset) context).getApp().getResourceRepoId();
        }
        Repo repo = repoCacheService.getItemById(repoId);
        if (repo != null) return repo.getUrl(parameter);

        return parameter;
    }

}
