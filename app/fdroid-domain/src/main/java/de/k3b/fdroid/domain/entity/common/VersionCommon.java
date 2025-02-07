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

package de.k3b.fdroid.domain.entity.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@SuppressWarnings("unused")
public class VersionCommon extends ProfileCommon implements IVersionCommon {

    @Schema(description = "Device compatibility: minSdkVersion = 'Minimal Required Android SDK API Version'.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"),
            example = "14")
    private int minSdkVersion;
    @Schema(description = "'Android SDK API Version' for which the [App] was created.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"),
            example = "21")
    private int targetSdkVersion;
    @Schema(description = "Device compatibility: maxSdkVersion = 'Maximum Required Android SDK API Version'.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"),
            example = "28")
    private int maxSdkVersion;

    private String hash;
    private String signer;
    @Schema(description = "filename of the sourcecode.",
            example = "de.k3b.android.androFotoFinder_44_src.tar.gz")
    private String srcname;

    public VersionCommon() {
        super();
    }

    @androidx.room.Ignore
    public VersionCommon(@Nullable IVersionCommon src) {
        super();
        copyCommon(this, src);
    }

    public static void copyCommon(@NotNull VersionCommon dest, @Nullable IVersionCommon src) {
        if (src != null) {
            ProfileCommon.copyCommon(dest, src);
            dest.setSrcname(src.getSrcname());

            dest.setHash(src.getHash());
            dest.setSigner(src.getSigner());

            dest.setSdk(src.getMinSdkVersion(), src.getTargetSdkVersion(), src.getMaxSdkVersion());
        }
    }

    @Override
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = maxlen(hash);
    }

    @Override
    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    @Override
    public int getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(int maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }

    public void setSdk(int minSdkVersion, int targetSdkVersion, int maxSdkVersion) {
        setMinSdkVersion(minSdkVersion);
        setTargetSdkVersion(targetSdkVersion);
        setMaxSdkVersion(maxSdkVersion);
    }

    @Override
    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = maxlen(signer);
    }

    @Override
    public String getSrcname() {
        return srcname;
    }

    public void setSrcname(String srcname) {
        this.srcname = maxlen(srcname);
    }

    @Override
    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public static <T extends IVersionCommon> Comparator<T> compareByVersionCodeDescending() {
        // requires api 24. not compatible with api 14
        // equivalent to java.util.Comparator.comparing(T::versionCode)

        return (v1, v2) -> v2.getVersionCode() - v1.getVersionCode();
    }

    public static void toStringBuilder(
            @NotNull StringBuilder sb, @Nullable IVersionCommon content) {
        if (content != null) {
            toStringBuilder(sb, "minSdkVersion", content.getMinSdkVersion());
            toStringBuilder(sb, "targetSdkVersion", content.getTargetSdkVersion());
            toStringBuilder(sb, "maxSdkVersion", content.getMaxSdkVersion());
            toStringBuilder(sb, "srcname", content.getSrcname());
            toStringBuilder(sb, "hash", content.getHash(), 14);
            toStringBuilder(sb, "signer", content.getSigner(), 14);
        }
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, this);
    }
}
