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
package de.k3b.fdroid.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;
import de.k3b.fdroid.domain.util.StringUtil;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa.
 * <p>
 * Some repo-s
 * <p>
 *
 * @see <a href="https://guardianproject.info/fdroid/repo/index-v1.jar">https://guardianproject.info/fdroid/repo/index-v1.jar</a>
 * @see <a href="https://apt.izzysoft.de/fdroid/repo/index-v1.jar">https://apt.izzysoft.de/fdroid/repo/index-v1.jar</a>
 * @see <a href="https://f-droid.org/repo/index-v1.jar">https://f-droid.org/repo/index-v1.jar</a>
 * @see <a href="https://f-droid.org/archive/index-v1.jar">https://f-droid.org/archive/index-v1.jar</a>
 * <p>
 * @see <a href="https://fdroid.cgeo.org/repo/index-v1.jar">https://fdroid.cgeo.org/repo/index-v1.jar</a>
 * @see <a href="https://fdroid.cgeo.org/nightly/index-v1.jar">https://fdroid.cgeo.org/nightly/index-v1.jar</a> with wrong repo.address
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo",
        description = "An Android [App] Repository that contains a catalogue of Android apps. " +
                "A [Repo] allows to download APK-File in one or more [Version]-s")
@SuppressWarnings("unused")
public class Repo extends RepoCommon implements IDatabaseEntityWithId {
    public static final String STATE_BUSY = "busy"; // while downloding. bg-color=yellow
    public static final String STATE_ERROR = "error"; // download failed bg-color=red
    public static final String STATE_ENABLED = "enabled"; // bg-color=green
    public static final String STATE_DISABLED = null; // no bg-color

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @Schema(description = "List of duplicates of this [Ropo] that allows to download under a different Adress or url. (used for Load balancing.)",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Mirror"),
            example = "https://ftp.fau.de/fdroid/repo,https://mirror.cyberbits.eu/fdroid/repo,https://fdroid.tetaneutral.net/fdroid/repo,https://ftp.lysator.liu.se/pub/fdroid/repo,https://plug-mirror.rcac.purdue.edu/fdroid/repo")
    @Column(length = MAX_LEN_AGGREGATED)
    private String mirrors = null;
    /**
     * calculated and cached from {@link #mirrors}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    @JsonIgnore
    private String[] mirrorsArray;

    private String jarSigningCertificate;

    private String jarSigningCertificateFingerprint;

    @Schema(description = "Server, where Catalogfile was last downloaded from .",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Mirror"),
            example = "https://f-droid.org/repo")
    private String lastUsedDownloadMirror;

    @Schema(description = "Last download ErrorMessage.")
    private String lastErrorMessage;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "When the [Repo-Catalog]-download-file was downloaded in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "1654792862000")
    private long lastUsedDownloadDateTimeUtc;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "Number of different [App]s available in this Repo.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "App"),
            example = "3868")
    private int lastAppCount;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "Number of different App-[Version]s available in this Repo.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Version"),
            example = "9087")
    private int lastVersionCount;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @JsonIgnore
    private boolean autoDownloadEnabled;

    // uuid of WorkRequest used to download/import
    @JsonIgnore
    private String downloadTaskId;

    private String repoTyp;

    public Repo() {
    }

    @androidx.room.Ignore
    public Repo(String name, String address) {
        this(name, address, null);
    }

    @androidx.room.Ignore
    public Repo(String name, String address, String repoTyp) {
        setName(name);
        setAddress(address);
        setRepoTyp(repoTyp);
    }

    @Schema(description = "Calculated Catalog download file-url.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Mirror"),
            example = "https://f-droid.org/repo/index-v1.jar")
    public static String getV1Url(String server) {
        return getUrl(server, V1_JAR_NAME);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMirrors() {
        return mirrors;
    }

    public void setMirrors(String mirrors) {
        this.mirrors = maxlen(mirrors, MAX_LEN_AGGREGATED);
        mirrorsArray = null;
    }

    public String[] getMirrorsArray() {
        if (mirrorsArray == null) {
            mirrorsArray = StringUtil.toStringArray(getAddress() + "," + mirrors);
        }
        return mirrorsArray;
    }

    /**
     * values of current "index-v1.jar"
     */
    public String getJarSigningCertificate() {
        return jarSigningCertificate;
    }

    public void setJarSigningCertificate(String jarSigningCertificate) {
        this.jarSigningCertificate = maxlen(jarSigningCertificate, MAX_LEN_AGGREGATED);
    }

    /**
     * values of current "index-v1.jar"
     */
    public String getJarSigningCertificateFingerprint() {
        return jarSigningCertificateFingerprint;
    }

    public void setJarSigningCertificateFingerprint(String jarSigningCertificateFingerprint) {
        this.jarSigningCertificateFingerprint = maxlen(jarSigningCertificateFingerprint);
    }

    public String getLastUsedDownloadMirror() {
        if (lastUsedDownloadMirror != null) {
            return lastUsedDownloadMirror;
        }
        return getAddress();
    }

    static String removeName(String url) {
        if (url != null) {
            int lastDir = url.lastIndexOf('/');
            int lastDot = url.lastIndexOf('.');
            if (lastDot > lastDir && lastDir > 0) return url.substring(0, lastDir + 1);
        }
        return url;
    }

    public String getUrl(String name) {
        return getUrl(getLastUsedDownloadMirror(), name);
    }

    private static String getUrl(String server, String name) {
        if (server == null || name == null) return null;
        server = removeName(server);
        StringBuilder url = new StringBuilder().append(server);
        if (!server.endsWith(".jar")) {
            if (!server.endsWith("/")) url.append("/");
            url.append(name);
        }
        return url.toString();
    }

    public void setLastUsedDownloadMirror(String lastUsedDownloadMirror) {
        this.lastUsedDownloadMirror = maxlen(removeName(lastUsedDownloadMirror));
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "autoDownloadEnabled", this.autoDownloadEnabled);
        toStringBuilder(sb, "lastErrorMessage", this.lastErrorMessage);

        super.toStringBuilder(sb);
        toStringBuilder(sb, "repoTyp", this.getRepoTyp());
        toStringBuilder(sb, "mirrors", this.mirrors);
        toDateStringBuilder(sb, "lastUsedDownloadDateTimeUtc", this.lastUsedDownloadDateTimeUtc);
        toStringBuilder(sb, "lastAppCount", this.lastAppCount);
        toStringBuilder(sb, "lastVersionCount", this.lastVersionCount);
        toStringBuilder(sb, "lastUsedDownloadMirror", this.lastUsedDownloadMirror);
        toStringBuilder(sb, "jarSigningCertificate", this.jarSigningCertificate, 14);
        toStringBuilder(sb, "jarSigningCertificateFingerprint", this.jarSigningCertificateFingerprint, 14);
        toStringBuilder(sb, "downloadTaskId", this.downloadTaskId);
    }

    @Schema(description = "Calculated Url where V1-Repo-Catalog-file is downloaded from.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "https://f-droid.org/repo/index-v1.jar")
    public String getV1Url() {
        String server = removeName(getLastUsedDownloadMirror());
        return getV1Url(server);
    }

    public String getAppIconUrl(String icon) {
        String url = null;
        if (!StringUtil.isEmpty(icon)) {
            String relPath = "icons/" + icon;
            // For security reasons: web server does not allow paths that contain ".."
            // "icons/../" is a legit usecase when icon comes from localized outside the icons dir
            url = getUrl(relPath.replace("icons/../", ""));
        }
        return url;
    }

    @Schema(description = "Calculated Icon-Url of the [Repo]",
            example = "https://f-droid.org/repo/icon/fdroid-icon.png")
    public String getRepoIconUrl() {
        return getAppIconUrl(getIcon());
    }

    public long getLastUsedDownloadDateTimeUtc() {
        if (lastUsedDownloadDateTimeUtc != 0) {
            return lastUsedDownloadDateTimeUtc;
        }
        return getTimestamp();
    }

    @Schema(description = "Date, when the [Repo-Catalog]-download-file was downloaded.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "2022-06-09")

    public String getLastUsedDownloadDateTimeUtcDate() {
        long l = getLastUsedDownloadDateTimeUtc();
        return l == 0 ? null : asDateString(l);
    }

    public void setLastUsedDownloadDateTimeUtc(long lastUsedDownloadDateTimeUtc) {
        this.lastUsedDownloadDateTimeUtc = lastUsedDownloadDateTimeUtc;
    }

    public int getLastAppCount() {
        return lastAppCount;
    }

    public void setLastAppCount(int lastAppCount) {
        this.lastAppCount = lastAppCount;
    }

    public int getLastVersionCount() {
        return lastVersionCount;
    }

    public void setLastVersionCount(int lastVersionCount) {
        this.lastVersionCount = lastVersionCount;
    }

    public boolean isAutoDownloadEnabled() {
        return autoDownloadEnabled;
    }

    public void setAutoDownloadEnabled(boolean autoDownloadEnabled) {
        this.autoDownloadEnabled = autoDownloadEnabled;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = maxlen(lastErrorMessage);
    }

    public String getDownloadTaskId() {
        return downloadTaskId;
    }

    public void setDownloadTaskId(String downloadTaskId) {
        this.downloadTaskId = maxlen(downloadTaskId);
    }

    /**
     * will be translated to html-css-class='state_xxxxx'.
     * In android to backgroundcolor 'bg_state_xxxxx'
     * Called by reflection from fdroid-html
     */
    @Schema(description = "Status of the Repository that will be translated to html-css-class='state_xxxxx'.",
            example = "enabled")
    public String getStateCode() {
        if (isBusy()) return STATE_BUSY;
        if (!StringUtil.isEmpty(getLastErrorMessage())) return STATE_ERROR;
        if (isAutoDownloadEnabled()) return STATE_ENABLED;
        return STATE_DISABLED;
    }

    public boolean isBusy() {
        return !StringUtil.isEmpty(getDownloadTaskId());
    }

    public static Repo findByDownloadTaskId(Iterable<Repo> repos, String downloadTaskId) {
        if (repos != null && !StringUtil.isEmpty(downloadTaskId)) {
            for (Repo repo : repos) {
                if (downloadTaskId.compareTo(repo.getDownloadTaskId()) == 0) return repo;
            }
        }
        return null;
    }

    /**
     * ('t'est, 'n'ightly, 's'table, 'h'istoric, 'u'nknown)
     */
    public String getRepoTyp() {
        return repoTyp;
    }

    public void setRepoTyp(String repoTyp) {
        this.repoTyp = maxlen(repoTyp);
    }
}
