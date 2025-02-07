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
package de.k3b.fdroid.android.util;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.k3b.fdroid.android.html.util.HtmlUtil;
import de.k3b.fdroid.android.repository.R;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HtmlUtilInstrumentedTest {
    @Test
    public void getColorByName() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        int colorFromName = HtmlUtil.getColorByName(context, "busy", "bg_state_", 123);

        int colorFromRes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorFromRes = context.getColor(R.color.bg_state_busy);
        } else {
            colorFromRes = context.getResources().getColor(R.color.bg_state_busy);
        }
        assertEquals(colorFromName, colorFromRes);
    }
}