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

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * {@link Locale}: Language of a {@link Localized}.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
@ExternalDocumentation(description = "Locale or Language of Translation of an [App]", url = ExtDoc.GLOSSAR_URL + "Locale")
public class Locale extends EntityCommon {

    /**
     * Translation with this priority will not be added to the database.
     */
    public static final int PRIORITY_HIDDEN = -1;
    /**
     * Translation-priority of english language that should always exist.
     */
    public static final int PRIORITY_FALLBACK = 1;
    /**
     * Translation-priority highest in users-operatingsystem or web-browser.
     */
    public static final int PRIORITY_MAX = 99;
    @javax.persistence.Id
    @androidx.room.PrimaryKey
    @NotNull
    @Schema(description = "Iso-Language-Code (Without the Country-Code).",
            example = "de")
    @Column(length = MAX_LEN_LOCALE)
    private String id; // ie de

    @Schema(description = "Emoji-Character of the flag-symbol of the Language.",
            example = "🇩🇪")
    private String symbol; // ie 🇩🇪

    @Schema(description = "The native name of the language.",
            example = "Deutsch")
    private String nameNative; // ie Deutsch
    @Schema(description = "The english name of the language.",
            example = "German")
    private String nameEnglish; // ie German

    /**
     * Translation order: highest first; -1 == hidden (Translations are NOT added to Database)
     */
    @Schema(description = "Translation order: highest first; -1 == hidden (Translations are NOT added to Database).",
            example = "1")
    private int languagePriority;

    public Locale() {
    }

    @androidx.room.Ignore
    public Locale(String localeCode) {
        setId(localeCode);
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "symbol", this.symbol);
        toStringBuilder(sb, "nameNative", this.nameNative);
        toStringBuilder(sb, "nameEnglish", this.nameEnglish);
        toStringBuilder(sb, "languagePriority", this.getLanguagePriority());
    }

    /**
     * locale-language-code. Usually two-letter-lowercase. i.e. it for italian
     */
    @NotNull
    // @Override
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = maxlen(id, MAX_LEN_LOCALE);
    }

    /**
     * a flag to symbolize the language. i.e. 🇮🇹
     */
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = maxlen(symbol);
    }

    public String getNameNative() {
        return nameNative;
    }

    public void setNameNative(String nameNative) {
        this.nameNative = maxlen(nameNative);
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = maxlen(nameEnglish);
    }

    public int getLanguagePriority() {
        return languagePriority;
    }

    public void setLanguagePriority(int languagePriority) {
        this.languagePriority = languagePriority;
    }
}
