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

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by brett on 05/08/15.
 */
public class MaterialPalette {

    @ColorInt
    private int primaryColor;

    @ColorInt
    private int primaryDarkColor;

    @ColorInt
    private int primaryLightColor;

    @ColorInt
    private int accentColor;

    @ColorInt
    private int textPrimaryColor;

    @ColorInt
    private int textSecondaryColor;

    @ColorInt
    private int textIconsColor;

    @ColorInt
    private int windowBackgroundColor;

    public static MaterialPalette build(String primary, String primaryDark, String primaryLight, String accent,
                                        String textPrimary, String textSecondary, String textIcons,
                                        String windowBackground) throws IllegalArgumentException {
        MaterialPalette palette = new MaterialPalette();
        palette.primaryColor = Color.parseColor(primary);
        palette.primaryDarkColor = Color.parseColor(primaryDark);
        palette.primaryLightColor = Color.parseColor(primaryLight);
        palette.accentColor = Color.parseColor(accent);
        palette.textPrimaryColor = Color.parseColor(textPrimary);
        palette.textSecondaryColor = Color.parseColor(textSecondary);
        palette.textIconsColor = Color.parseColor(textIcons);
        palette.windowBackgroundColor = Color.parseColor(windowBackground);
        return palette;
    }

    private MaterialPalette() {

    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getPrimaryLightColor() {
        return primaryLightColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getTextPrimaryColor() {
        return textPrimaryColor;
    }

    public int getTextSecondaryColor() {
        return textSecondaryColor;
    }

    public int getTextIconsColor() {
        return textIconsColor;
    }

    public int getWindowBackgroundColor() {
        return windowBackgroundColor;
    }
}
