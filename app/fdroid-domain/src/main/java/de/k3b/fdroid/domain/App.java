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
package de.k3b.fdroid.domain;

import javax.persistence.Column;

import de.k3b.fdroid.domain.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;

/**
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(
        indices = {@androidx.room.Index("id"), @androidx.room.Index({"packageName"})}
)
@javax.persistence.Entity
@javax.persistence.Table(name = "App")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class App extends AppCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    // public List<Localized> localisations;
    // public List<Version> versions;

    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale name values concatenated for faster search */
    private String searchName;

    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale summary values concatenated for faster search */
    private String searchSummary;

    @Column(length = MAX_LEN_AGGREGATED_DESCRIPTION)
    /** all different locale name description concatenated for faster search */
    private String searchDescription;

    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale whatsNew values concatenated for faster search */
    private String searchWhatsNew;

    private String searchVersion;
    private String searchSdk;
    private String searchSigner;
    private String searchCategory;

    // needed by android-room and jpa
    public App() {
    }

    @androidx.room.Ignore
    public App(String packageName) {
        setPackageName(packageName);
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "searchVersion", this.searchVersion);
        toStringBuilder(sb, "searchSdk", this.searchSdk);
        toStringBuilder(sb, "searchCategory", this.searchCategory);

        toStringBuilder(sb, "searchName", this.searchName, 20);
        toStringBuilder(sb, "searchSummary", this.searchSummary, 20);
        toStringBuilder(sb, "searchDescription", this.searchDescription, 20);
        toStringBuilder(sb, "searchWhatsNew", this.searchWhatsNew, 20);

        toStringBuilder(sb, "searchSigner", this.searchSigner, 14);
    }

    public int getId() {
        return id;
    }

    @Override
    public int getAppId() {
        return getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchSummary() {
        return searchSummary;
    }

    public void setSearchSummary(String searchSummary) {
        this.searchSummary = searchSummary;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public String getSearchWhatsNew() {
        return searchWhatsNew;
    }

    public void setSearchWhatsNew(String searchWhatsNew) {
        this.searchWhatsNew = searchWhatsNew;
    }

    public String getSearchVersion() {
        return searchVersion;
    }

    public void setSearchVersion(String searchVersion) {
        this.searchVersion = searchVersion;
    }

    public String getSearchSdk() {
        return searchSdk;
    }

    public void setSearchSdk(String searchSdk) {
        this.searchSdk = searchSdk;
    }

    public String getSearchSigner() {
        return searchSigner;
    }

    public void setSearchSigner(String searchSigner) {
        this.searchSigner = searchSigner;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }
}
