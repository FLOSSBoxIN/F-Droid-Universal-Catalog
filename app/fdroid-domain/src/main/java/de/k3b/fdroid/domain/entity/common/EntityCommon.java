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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import de.k3b.fdroid.domain.interfaces.Enitity;
import de.k3b.fdroid.domain.util.TestDataGenerator;

/**
 * what all {@link Enitity} have in Common:
 * Nearly a pojo except there is toString() with toStringBuilder support.
 * pojo Properties are not allowed
 */
public class EntityCommon implements Enitity {
    public static final int MAX_LEN_LOCALE = 8;

    /**
     * non standard string len for aggregated fields and for app description
     */
    public static final int MAX_LEN_AGGREGATED = 8000;
    /**
     * non standard string len for aggregated_description
     */
    public static final int MAX_LEN_AGGREGATED_DESCRIPTION = 256000;

    public static String asDateString(long longDate) {
        if (longDate == 0) return "";

        Date date = new Date(longDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    public static void createPojoFieldsFile(Class<?>[] classes) throws FileNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        File currentDir = null;
        try {
            currentDir = new File(".").getCanonicalFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return;
        }
        File srcDir = currentDir;
        while (!new File(srcDir, "src").exists()) {
            srcDir = srcDir.getParentFile();
            if (srcDir == null) {
                throw new IllegalArgumentException("No dir 'src' found below " + currentDir);
            }
        }
        srcDir = new File(srcDir, "test/resources");
        srcDir.mkdirs();
        PrintStream out = new PrintStream(new File(srcDir, "PojoFields.txt"));
        out.println("toString() - Properties of Entities");

        try {
            for (Class<?> c : classes) {
                Object o = c.getConstructor().newInstance();
                TestDataGenerator.fill(o, 4, true);
                String[] fields = o.toString().split("[,\\[\\]{}]");
                fields[0] = "-";
                Arrays.sort(fields);

                out.println(o.getClass().getName());
                for (String f : fields) {
                    String[] f2 = f.split("=");
                    if (f2.length == 2) {
                        StringBuilder fieldValue = new StringBuilder().append("\t").append(f2[0]);
                        String stringPrefix = "#4-";
                        int stringOffset = -1;
                        if (f2[1].startsWith("4")) {
                            fieldValue.append("(").append(f2[1].substring(1)).append(")");
                        } else if ((stringOffset = f2[1].indexOf(stringPrefix)) >= 0) {
                            fieldValue.append("(").append(f2[1].substring(stringOffset + stringPrefix.length())).append(")");
                        }
                        out.println(fieldValue);
                    }
                }
            }
        } finally {
            out.flush();
            out.close();
        }
    }

    protected static void toStringBuilder(StringBuilder sb, String name, Object value, int maxLen) {
        if (value != null) {
            String strValue = value.toString();
            int length = strValue.length();
            if (maxLen > 8 && length > maxLen) {
                int middle = maxLen / 2;
                strValue = strValue.replace("\n", " ").replace("\t", " ");
                strValue = strValue.substring(0, middle) + "..." + strValue.substring(length - middle + 3, length);
            }
            toStringBuilder(sb, name, strValue);
        }
    }

    protected static void toStringBuilder(StringBuilder sb, String name, Object value) {
        if (value != null) {
            sb.append(name).append('=').append(value).append(',');
        }
    }

    protected static void toStringBuilder(StringBuilder sb, String name, Object[] values) {
        if (values != null && values.length > 0) {
            sb.append(name).append("= [");
            String delimiter = "";
            for (Object value : values) {
                if (value != null) {
                    sb.append(delimiter).append(value);
                    delimiter = ", ";
                }
            }
            sb.append("],");
        }
    }

    protected static void toStringBuilder(StringBuilder sb, String name, EntityCommon value) {
        if (value != null) {
            sb.append(name).append("=").append(value).append(",");
        }
    }

    protected static void toStringBuilder(StringBuilder sb, String name, long value) {
        if (value != 0 && value != -1)
            sb.append(name).append('=').append(value).append(',');
    }

    protected static void toStringBuilder(StringBuilder sb, String name, boolean value) {
        if (value) {
            sb.append(name).append('=').append(value).append(',');
        }
    }

    public static void toDateStringBuilder(StringBuilder sb, String name, long value) {
        if (!isEmpty(value)) {
            sb.append(name).append('=').append(asDateString(value)).append(',');
        }
    }

    public static long ifNotNull(long val, long defaultValue) {
        return !isEmpty(val) ? val : defaultValue;
    }

    public static String ifNotNull(String val, String defaultValue) {
        return !isEmpty(val) ? val : defaultValue;
    }

    public static boolean isEmpty(long val) {
        return val == 0L;
    }

    public static boolean isEmpty(int val) {
        return val == 0;
    }

    public static boolean isEmpty(String val) {
        return val == null || val.isEmpty();
    }

    public static boolean isEmpty(Object val) {
        return val == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append('[');
        toStringBuilder(sb);
        int last = sb.length() - 1;
        if (sb.charAt(last) == ',') {
            sb.setCharAt(last, ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public static String maxlen(String s) {
        return maxlen(s, 255);
    }

    public static String maxlen(String s, int maxlen) {
        if (s != null && s.length() > maxlen) {
            throw new IllegalArgumentException("string-len > " + maxlen + " : '" + s + "'");
        }
        return s;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
    }
}
