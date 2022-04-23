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

package de.k3b.fdroid.domain.common;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
public class RepoCommon extends PojoCommon {
    public static final String V1_JAR_NAME = "index-v1.jar";
    public static final String V1_JSON_NAME = "index-v1.json";

    private String name;
    private long timestamp;
    private int version;
    private long maxage;
    private String icon;
    private String address;
    private String description;

    public static void copyCommon(RepoCommon dest, RepoCommon src) {
        dest.setName(src.getName());
        dest.setTimestamp(src.getTimestamp());
        dest.setVersion(src.getVersion());
        dest.setMaxage(src.getMaxage());
        dest.setIcon(src.getIcon());
        dest.setAddress(src.getAddress());
        dest.setDescription(src.getDescription());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getV1JarFileName(String name) {
        String fileName = name
                .replace(' ', '_')
                .replace('/', '_')
                .replace('\\', '_')
                .replace(':', '_')
                .replace("__", "_");
        return fileName + "-" + V1_JAR_NAME;
    }

    public String getV1JarFileName() {
        return getV1JarFileName(this.getName());
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getMaxage() {
        return maxage;
    }

    public void setMaxage(long maxage) {
        this.maxage = maxage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "name", this.name);
        toDateStringBuilder(sb, "timestamp", this.timestamp);
        toStringBuilder(sb, "version", this.version);
        toStringBuilder(sb, "maxage", this.maxage);
        toStringBuilder(sb, "icon", this.icon);
        toStringBuilder(sb, "address", this.address);
        toStringBuilder(sb, "description", this.description, 40);
    }
}
