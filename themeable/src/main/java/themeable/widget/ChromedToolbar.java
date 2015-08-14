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

package themeable.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;

import themeable.MaterialPalette;
import themeable.Themeable;
import themeable.res.ResourceUtils;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedToolbar extends ChromedView {

    private static final int colorPrimary = 80;

    private Toolbar toolbar;

    public ChromedToolbar(Toolbar toolbar) {
        super(toolbar);
        this.toolbar = toolbar;
    }

    @Override
    public void overrideAppearance() {
        MaterialPalette palette = Themeable.getCurrentPalette();
        if(palette != null) {
            toolbar.setBackgroundColor(palette.getPrimaryColor());
            toolbar.setTitleTextColor(palette.getTextIconsColor());
            toolbar.setSubtitleTextColor(palette.getTextSecondaryColor());
        } else {
            restore();
        }
    }

    public void restore() {
        Context context = toolbar.getContext();
        int[] attrs = ResourceUtils.getResourceDeclareStyleableIntArray(context, "Theme");
        TypedArray typedArray = context.obtainStyledAttributes(android.R.style.Theme, attrs);
        toolbar.setBackgroundColor(typedArray.getColor(colorPrimary, 0));
    }
}
