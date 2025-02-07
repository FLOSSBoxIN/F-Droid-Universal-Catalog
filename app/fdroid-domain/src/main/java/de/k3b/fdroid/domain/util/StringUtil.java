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

package de.k3b.fdroid.domain.util;

import java.util.List;

public class StringUtil {
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final String CSV_FIELD_DELIMITER = ",";

    public static <T> boolean isEmpty(T[] s) {
        return s == null || s.length == 0;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(long s) {
        return s == 0;
    }

    public static String emptyAsNull(String s) {
        if (s != null && s.isEmpty()) return null;
        return s;
    }

    public static String maxLen(String value, int maxLen, String fieldName) {
        if (value != null && value.length() > maxLen) {
            throw new IllegalArgumentException(fieldName + " ('" + value + "') > " + maxLen);
        }
        return value;
    }

    public static String replaceEmpty(String newValue, String emptyValueReplacement) {
        if (StringUtil.isEmpty(newValue)) return emptyValueReplacement;
        return newValue;
    }

    public static long replaceEmpty(long newValue, long emptyValueReplacement) {
        // do not overwrite existing value
        if (StringUtil.isEmpty(newValue)) return emptyValueReplacement;
        return newValue;
    }

    public static boolean replaceEmpty(boolean newValue, boolean emptyValueReplacement) {
        // do not overwrite existing value
        if (!newValue) return emptyValueReplacement;
        return newValue;
    }

    public static boolean contains(String search, String[] itemArray) {
        for (String item : itemArray) {
            if (search.equalsIgnoreCase(item)) return true;
        }
        return false;
    }

    public static <T> String toCsvStringOrNull(List<T> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        if (list != null) {
            for (T s : list) {
                if (s != null) {
                    if (result.length() > 0) result.append(delimiter);
                    result.append(s);
                }
            }
        }

        return result.length() == 0 ? null : result.toString();
    }

    public static <T> String toCsvStringOrNull(List<T> list) {
        return toCsvStringOrNull(list, CSV_FIELD_DELIMITER);
    }

    public static String[] toStringArray(String csvString) {
        if (StringUtil.isEmpty(csvString)) {
            return StringUtil.EMPTY_ARRAY;
        } else {
            return csvString.split(CSV_FIELD_DELIMITER);
        }
    }

    /**
     * @return getFirst(" a, b, c ", ", ", null) returns "a"
     */
    public static String getFirst(String combinedValue, String seperator, String notFoundValue) {
        if (isEmpty(combinedValue)) return notFoundValue;
        int pos = combinedValue.indexOf(seperator);
        if (pos > 0) return combinedValue.substring(0, pos);
        return combinedValue;
    }

    public static String getFirstWithPrefix(String combinedValue, String prefix, String seperator, String notFoundValue) {
        if (!isEmpty(combinedValue)) {
            int start = combinedValue.indexOf(prefix);
            if (start >= 0) {
                start += prefix.length();
                int end = combinedValue.indexOf(seperator, start);
                if (end > 0) return combinedValue.substring(start, end);
                return combinedValue.substring(start);
            }
        }
        return notFoundValue;
    }

    public static String getLast(String combinedValue, String seperator, String notFoundValue) {
        if (StringUtil.isEmpty(combinedValue)) return notFoundValue;
        int pos = combinedValue.lastIndexOf(seperator);
        if (pos >= 0) {
            String substring = combinedValue.substring(pos + seperator.length());
            if (!StringUtil.isEmpty(substring)) return substring;
            return notFoundValue;
        }
        return combinedValue;
    }

    public static int parseInt(String numberText, int defaultValue) {
        if (StringUtil.isEmpty(numberText)) return defaultValue;
        try {
            return Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
