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

package de.k3b.fdroid.v1.service;

import java.io.IOException;
import java.io.InputStream;

import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.domain.interfaces.AppHardwareRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.HardwareProfileRepository;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.ProgressListener;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.service.CategoryService;
import de.k3b.fdroid.service.HardwareProfileService;
import de.k3b.fdroid.service.LanguageService;
import de.k3b.fdroid.v1.domain.App;
import de.k3b.fdroid.v1.domain.Repo;
import de.k3b.fdroid.v1.domain.Version;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public abstract class V1UpdateService {
    private final LanguageService languageService;
    JsonStreamParser jsonStreamParser = new JsonStreamParser();
    RepoUpdateService repoUpdateService;

    AppUpdateService appUpdateService;
    VersionUpdateService versionUpdateService;

    FixLocaleService fixLocaleService = new FixLocaleService();

    private int currentRepoId = 0;

    /*
    public V1UpdateService(FDroidDatabase fDroidDatabase) {

        this(
                fDroidDatabase.repoDao(),
                fDroidDatabase.appDao(),
                fDroidDatabase.categoryDao(),
                fDroidDatabase.appCategoryDao(),
                fDroidDatabase.versionDao(),
                fDroidDatabase.localizedDao(),
                fDroidDatabase.localeDao());
    }
     */

    public V1UpdateService(RepoRepository repoRepository, AppRepository appRepository,
                           CategoryService categoryService,
                           AppCategoryRepository appCategoryRepository,
                           VersionRepository versionRepository,
                           LocalizedRepository localizedRepository,
                           LocaleRepository localeRepository,
                           HardwareProfileRepository hardwareProfileRepository,
                           AppHardwareRepository appHardwareRepository,
                           LanguageService languageService) {
        this(repoRepository, appRepository,
                categoryService,
                appCategoryRepository,
                versionRepository,
                localizedRepository,
                localeRepository,
                hardwareProfileRepository, appHardwareRepository, languageService, null);
    }

    public V1UpdateService(RepoRepository repoRepository, AppRepository appRepository,
                           CategoryService categoryService,
                           AppCategoryRepository appCategoryRepository,
                           VersionRepository versionRepository,
                           LocalizedRepository localizedRepository,
                           LocaleRepository localeRepository,
                           HardwareProfileRepository hardwareProfileRepository,
                           AppHardwareRepository appHardwareRepository,
                           LanguageService languageService,
                           ProgressListener progressListener) {
        this.languageService = languageService;
        repoUpdateService = new RepoUpdateService(repoRepository);

        AppCategoryUpdateService appCategoryUpdateService = new AppCategoryUpdateService(
                categoryService, appCategoryRepository);
        LocalizedUpdateService localizedUpdateService = new LocalizedUpdateService(
                localizedRepository, languageService);
        appUpdateService = new AppUpdateService(appRepository, appCategoryUpdateService, localizedUpdateService, progressListener);

        HardwareProfileService hardwareProfileService = new HardwareProfileService(appRepository, hardwareProfileRepository, appHardwareRepository, progressListener);
        versionUpdateService = new VersionUpdateService(appRepository, versionRepository, hardwareProfileService, progressListener);
    }

    public void readFromJar(InputStream jarInputStream) throws IOException {
        init();
        jsonStreamParser.readFromJar(jarInputStream);
    }

    public void readJsonStream(InputStream jsonInputStream) throws IOException {
        init();
        jsonStreamParser.readJsonStream(jsonInputStream);
    }

    public void init() {
        appUpdateService.init();
        versionUpdateService.init();
    }

    abstract protected String log(String s);

    class JsonStreamParser extends FDroidCatalogJsonStreamParserBase {

        /**
         * Stream event, when something has to be logged
         *
         * @param s
         */
        @Override
        protected String log(String s) {
            return V1UpdateService.this.log(s);
        }

        /**
         * Stream event, when a {@link Repo} was read
         *
         * @param v1Repo
         */
        @Override
        protected void onRepo(Repo v1Repo) {
            de.k3b.fdroid.domain.Repo roomRepo = repoUpdateService.update(v1Repo);
            currentRepoId = roomRepo.getId();
        }

        /**
         * Stream event, when a {@link App} was read
         *
         * @param v1App
         */
        @Override
        protected void onApp(App v1App) {
            if (v1App != null) {
                fixLocaleService.fix(v1App);
                appUpdateService.update(currentRepoId, v1App);
            }
        }

        /**
         * Stream event, when a {@link Version} was read
         *
         * @param packageName
         * @param v1Version
         */
        @Override
        protected void onVersion(String packageName, Version v1Version) {
            versionUpdateService.updateCollectVersions(currentRepoId, packageName, v1Version);
        }
    }
}
