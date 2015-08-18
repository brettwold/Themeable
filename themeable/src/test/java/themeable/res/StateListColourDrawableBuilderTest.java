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

package themeable.res;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by brett on 18/08/15.
 */
@Config(sdk = 16, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StateListColourDrawableBuilderTest {

    @Test
    public void can_create_state_list_drawable() {
        StateListColourDrawableBuilder builder = new StateListColourDrawableBuilder(Color.BLACK);

        assertNotNull(builder);
        Drawable d = builder.getDrawable();
        assertNotNull(d);
        assertThat(d, instanceOf(StateListDrawable.class));
    }

    @Test
    public void can_add_state_list_colour() {
        StateListColourDrawableBuilder builder = new StateListColourDrawableBuilder(Color.BLACK);
        builder.addStateColour(new int[]{android.R.attr.state_enabled}, Color.RED);

        Drawable d = builder.getDrawable();
        assertNotNull(d);
        assertThat(d, instanceOf(StateListDrawable.class));

        StateListDrawable stateListDrawable = (StateListDrawable)d;
        assertTrue(stateListDrawable.isStateful());
    }

    @Test
    public void adding_invalid_state_should_fail() {
        try {
            StateListColourDrawableBuilder builder = new StateListColourDrawableBuilder(Color.BLACK);
            builder.addStateColour(new int[]{1}, Color.RED);
            fail("Adding invalid state should fail in StateListColourDrawableBuilder");
        } catch (IllegalArgumentException e) {}
    }
}
