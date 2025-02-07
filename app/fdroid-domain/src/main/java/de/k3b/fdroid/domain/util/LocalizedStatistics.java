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
package de.k3b.fdroid.domain.util;

import java.util.Arrays;
import java.util.HashMap;

import de.k3b.fdroid.domain.entity.common.ILocalizedCommon;

/**
 * Count how many translations exist per language/locale
 */
public class LocalizedStatistics {
    private final HashMap<String, Integer> locale2summaryCount = new HashMap<>();
    private final HashMap<String, Integer> locale2descriptionCount = new HashMap<>();

    public void addStatistics(String locale, ILocalizedCommon localizedCommon) {
        addStatistics(locale2summaryCount, locale, localizedCommon.getSummary());
        addStatistics(locale2descriptionCount, locale, localizedCommon.getDescription());
    }

    private void addStatistics(HashMap<String, Integer> map, String locale, String value) {
        if (value != null && !value.trim().isEmpty()) {
            Integer oldValue = map.get(locale);
            if (oldValue == null) oldValue = 0;
            map.put(locale, oldValue + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String[] keys = locale2summaryCount.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        for (String key : keys) {
            result.append(key)
                    .append(": ").append(locale2summaryCount.get(key))
                    .append(", ").append(locale2descriptionCount.get(key))
            .append("\n");
        }
        return result.toString();
    }
}
