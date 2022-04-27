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

package de.k3b.fdroid.service;

import com.samskivert.mustache.Mustache;

import junit.framework.TestCase;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.TestDataGenerator;

public class FormatServiceTest extends TestCase {

    public void testFormat() {
        FormatService formatService = new FormatService(
                "Repo: '{{name}}({{lastAppCount}})' {{timestampDate}}");
        Repo repo = TestDataGenerator.fill(new Repo(), 4);
        repo.setName("name with <html/>");

        assertEquals("Repo: 'name with &lt;html/&gt;(4)' 1970-01-01", formatService.format(repo));
    }

    public void testFormatCustom() {

        class CustomType {
            class CustomTypeMitContext implements Mustache.CustomContext  {
                @Override
                public Object get(String name) throws Exception {
                    return name;
                }
            }

            String a = "hello";
            CustomTypeMitContext s = new CustomTypeMitContext();
        }

        FormatService formatService = new FormatService(
                "a = {{a}} s.world = {{s.world}}");
        CustomType customType = new CustomType();

        assertEquals("a = hello s.world = world", formatService.format(customType));
    }

}