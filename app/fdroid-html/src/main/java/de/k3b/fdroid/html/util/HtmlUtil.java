/*
 * Copyright (c) 2022 by k3b.
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
package de.k3b.fdroid.html.util;

public class HtmlUtil {
    public static final String HTML_CSS_CLASS_STATE = "class='state_"; // ie: status_error

    /**
     * extracts css-class from html starting with 'state_....'.
     * Example getHtmlCssClassState("...class='state_test' ") returns "test"
     *
     * @return null if not found
     */
    public static String getHtmlCssClassState(String html) {
        int found = html.indexOf(HTML_CSS_CLASS_STATE);
        if (found > 1) {
            found += HTML_CSS_CLASS_STATE.length();
            int end = html.indexOf("'", found);
            if (end > 0) {
                return html.substring(found, end);
            }
        }
        return null;
    }
}
