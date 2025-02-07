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

package de.k3b.fdroid.domain.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LanguageServiceTest {

    @Test
    public void getCanonicalLocale() {
        assertCanonical("en-US", "en");

        assertCanonical("de", "de");
        assertCanonical("de-DE", "de");
        assertCanonical("de_DE", "de");
        assertCanonical("de-rDE", "de");
        assertCanonical("de-AT", "de");

        // special case chineese
        assertCanonical("zh-rTW", "zh-TW");

        // special case non-2-letter-language (plattdütsch, ancient german)
        assertCanonical("nds-DE", "nds-DE");

        // error non-locale
        assertCanonical(null, null);
        assertCanonical("", "");
        assertCanonical("heLLo World", "heLLo World");
    }

    @Test
    public void splitToCanonicalLanguageArray() {
        // happy day
        assertSplitToCanonicalLanguageArray("en-US,de-DE", "en", "de");

        // corner cases: duplicate
        assertSplitToCanonicalLanguageArray("en-US,de-DE,en-AU", "en", "de", "en");

        // corner cases: empty element
        assertSplitToCanonicalLanguageArray("en-US,,de-DE", "en", "", "de");

        // null/empty handling
        assertSplitToCanonicalLanguageArray(null, (String[]) null);
        assertSplitToCanonicalLanguageArray("", (String[]) null);
    }

    private void assertCanonical(String fdroidLocale, String expected) {
        assertEquals("LanguageService.getCanonicalLocale(" + fdroidLocale + ")", expected, LanguageService.getCanonicalLocale(fdroidLocale));
    }

    private void assertSplitToCanonicalLanguageArray(String fdroidLocale, String... expected) {
        String message = "LanguageService.getCanonicalLocalesArray(" + fdroidLocale + ")";
        String[] result = LanguageService.splitToCanonicalLanguageArray(fdroidLocale);
        if (result == null) {
            assertNull(message, expected);
        } else {
            assertArrayEquals(message, expected, result);
        }
    }

    @Test
    public void getCanonicalLocaleChangeIndex() {
        String[] keys = new String[]{
                "de", "zh-TW", "de-DE", "ar-SA", "de-rDE", "en-us"};
        Integer[] index = LanguageService.getCanonicalLocaleChangeIndex(keys);
        assertArrayEquals("sorted keys", new String[]{
                "ar-SA", "de", "de-DE", "de-rDE", "en-us", "zh-TW"}, keys);

        assertArrayEquals("index", new Integer[]{0, 1, 4, 5, 6}, index);
    }

    @Test
    public void getLanguageTranslation() {
        String[] translation = LanguageService.getHardcodedPredefinedLanguageDefinition("de");
        /** "de|German|Deutsch|symbol" + */
        assertEquals("de size", 4, translation.length);
        assertEquals("de native", "Deutsch", translation[2]);

        translation = LanguageService.getHardcodedPredefinedLanguageDefinition("zh-CN");
        /** "zh-CN|Simplified Chinese|简体中文||" */
        assertEquals("zh-CN size", 3, translation.length);
        assertEquals("zh-CN english", "Simplified Chinese", translation[1]);

        translation = LanguageService.getHardcodedPredefinedLanguageDefinition("nds");
        assertNull("nds not found", translation);
    }
}