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

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import themeable.ThemeableFonts;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by brett on 18/08/15.
 */
@Config(sdk = 16, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StyleBuilderTest {

    @Test
    public void can_build_style() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setBackgroundColor(Color.GREEN)
                .build();

        assertNotNull(override);

        assertTrue(override.hasColorOrDrawable(StyleableConstants.backgroundColor));
        assertNull(override.getDrawable(StyleableConstants.backgroundColor));
        assertThat(override.getColor(StyleableConstants.backgroundColor, 0), is(Color.GREEN));
    }

    @Test
    public void prevent_invalid_style_overrides() {
        try {
            new StyleBuilder(null, 1)
                    .setBackgroundColor(Color.GREEN)
                    .build();
            fail("Should throw exception when context is null");
        } catch (IllegalArgumentException e) {}

        try {
            new StyleBuilder(RuntimeEnvironment.application, 0)
                    .setBackgroundColor(Color.GREEN)
                    .build();
            fail("Should throw exception when style resource isn't set");
        } catch (IllegalArgumentException e) {}

        try {
            new StyleBuilder(RuntimeEnvironment.application, 1)
                    .setBackground((Drawable) null)
                    .build();
            fail("Should throw exception when attempt to set null background");
        } catch (IllegalArgumentException e) {}

        try {
            new StyleBuilder(RuntimeEnvironment.application, 1)
                    .setBackground((StateListColourDrawableBuilder)null)
                    .build();
            fail("Should throw exception when attempt to set null background");
        } catch (IllegalArgumentException e) {}

        try {
            new StyleBuilder(RuntimeEnvironment.application, 1)
                    .setPadding(11, 10, 10, 10, 10)
                    .build();
            fail("Should throw exception when attempt to set invalid padding units");
        } catch (IllegalArgumentException e) {}

        try {
            new StyleBuilder(RuntimeEnvironment.application, 1)
                    .setTextSize(11, 32)
                    .build();
            fail("Should throw exception when attempt to set invalid text size units");
        } catch (IllegalArgumentException e) {}


        try {
            new StyleBuilder(RuntimeEnvironment.application, 1)
                    .setTypeface(null)
                    .build();
            fail("Should throw exception when attempt to set invalid typeface");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void can_set_background_drawable() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setBackground(new ColorDrawable(Color.RED))
                .build();

        assertNotNull(override);

        assertTrue(override.hasColorOrDrawable(StyleableConstants.backgroundColor));
        assertNotNull(override.getDrawable(StyleableConstants.backgroundColor));
    }

    @Test
    public void can_set_color_list_background() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setBackground(new StateListColourDrawableBuilder(Color.RED))
                .build();

        assertNotNull(override);

        assertTrue(override.hasColorOrDrawable(StyleableConstants.backgroundColor));
        Drawable drawable = override.getDrawable(StyleableConstants.backgroundColor);
        assertNotNull(drawable);
        assertThat(drawable, instanceOf(StateListDrawable.class));
    }

    @Test
    public void can_set_padding() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setPadding(TypedValue.COMPLEX_UNIT_PX, 10, 20, 30, 40)
                .build();

        assertTrue(override.hasBoolean(StyleableConstants.padding));

        assertThat(override.getDimensionPixelSize(StyleableConstants.paddingLeft, 0), is(10));
        assertThat(override.getDimensionPixelSize(StyleableConstants.paddingTop, 0), is(20));
        assertThat(override.getDimensionPixelSize(StyleableConstants.paddingRight, 0), is(30));
        assertThat(override.getDimensionPixelSize(StyleableConstants.paddingBottom, 0), is(40));
    }

    @Test
    public void can_set_single_text_color() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextColor(Color.RED)
                .build();

        ColorStateListWrapper colorStateListWrapper = override.getColorStateList(StyleableConstants.textColor);
        assertNotNull(colorStateListWrapper);
        ColorStateList colorStateList = colorStateListWrapper.getColorStateList();
        assertNotNull(colorStateList);
        assertThat(colorStateList.getDefaultColor(), is(Color.RED));
    }

    @Test
    public void can_set_text_color_state_list() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextColor(Color.RED)
                .setTextColor(new int[]{android.R.attr.state_enabled}, Color.YELLOW)
                .setTextColor(new int[]{-android.R.attr.state_enabled}, Color.GREEN)
                .setTextColor(new int[]{android.R.attr.state_pressed}, Color.BLACK)
                .build();

        ColorStateListWrapper colorStateListWrapper = override.getColorStateList(StyleableConstants.textColor);
        assertNotNull(colorStateListWrapper);
        ColorStateList colorStateList = colorStateListWrapper.getColorStateList();
        System.out.println(colorStateList.toString());
        assertNotNull(colorStateList);
        assertTrue(colorStateList.isStateful());
        assertThat(colorStateList.getDefaultColor(), is(Color.RED));
        assertThat(colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, 0), is(Color.YELLOW));
        assertThat(colorStateList.getColorForState(new int[]{-android.R.attr.state_enabled}, 0), is(Color.GREEN));
        assertThat(colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, 0), is(Color.BLACK));
    }

    @Test
    public void can_set_test_size() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, 24)
                .build();

        assertThat(override.getDimensionPixelSize(StyleableConstants.textSize, 0), is(24));
    }

    @Test
    public void can_set_typeface() {
        ThemeableFonts.registerTypeface("Banana", "tmp/file");
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTypeface("Banana")
                .build();

        assertNotNull(override.getTypeface(StyleableConstants.typeface));
    }

    @Test
    public void can_set_all_caps() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextAllCaps(true)
                .build();

        assertTrue(override.getBoolean(StyleableConstants.textAllCaps));

        StyleOverride override2 = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextAllCaps(false)
                .build();

        assertFalse(override2.getBoolean(StyleableConstants.textAllCaps));
    }

    @Test
    public void can_set_highlight_color() {
        StyleOverride override = new StyleBuilder(RuntimeEnvironment.application, 1)
                .setTextColorHighlight(Color.RED)
                .build();

        assertThat(override.getColor(StyleableConstants.textColorHighlight, 0), is(Color.RED));
    }
}
