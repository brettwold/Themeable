/*
 * Copyright (C)2015 Brett Cherrington
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package themeable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import themeable.res.StyleBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by brett on 18/08/15.
 */
@Config(sdk = 16, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ThemeableTest {

    @Before
    public void setup() {
        // set robolectric logging to stdout
        // to see logcat logs in gradle use --info or --debug switch
        ShadowLog.stream = System.out;
    }

    @Test
    public void can_build_theme() {

        Themeable.Theme testTheme = Themeable.Theme.newInstance("TestTheme")
                .addStyle(new StyleBuilder(RuntimeEnvironment.application, 1)
                                .build());

        assertNotNull(testTheme);
        assertThat(testTheme.getThemeName(), is("TestTheme"));
    }
}
