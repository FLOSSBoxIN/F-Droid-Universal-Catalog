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
package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Locale;

import de.k3b.fdroid.html.util.FormatUtil;

/**
 * iterates over all mustache templates *.html and executes it-s formatter.
 * Assumed file structure
 * <p>
 * * .../FDroidUniversal/app/fdroid-html/src/test/java/de/k3b/fdroid/html/service
 * * .../FDroidUniversal/app/fdroid-html/build/resources/main/html/Repo/list_repo.html
 */
@RunWith(Parameterized.class)
public class GenericTemplateTest {
    private static Mustache.CustomContext translator;

    private final Object testParamExampleItem;
    private final String testParamTemplateId;

    public GenericTemplateTest(Object exampleItem, String templateId) {
        super();
        this.testParamExampleItem = exampleItem;
        this.testParamTemplateId = templateId;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> input() throws Exception {
        translator = new ResourceBundleMustacheContext(Locale.US);
        return FormatUtil.getTestCases();
    }

    @Test
    public void domainTemplateTest() {
        System.out.println("<!-- running template " + testParamExampleItem.getClass().getSimpleName() + "/" + testParamTemplateId + " -->");
        FormatService formatService = new FormatService(
                testParamTemplateId, testParamExampleItem.getClass(), translator);
        String format = formatService.format(testParamExampleItem);
        System.out.println(format);
    }
}
