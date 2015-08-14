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
import android.util.TypedValue;
import android.view.View;

import themeable.MaterialPalette;
import themeable.Themeable;
import themeable.res.ResourceUtils;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedView implements ChromeOverride {

    private View view;

    public ChromedView(View view) {
        this.view = view;
    }

    @Override
    public void overrideAppearance() {
        MaterialPalette palette = Themeable.getCurrentPalette();
        if(palette != null) {
            view.setBackgroundColor(palette.getWindowBackgroundColor());
        } else {
            restore();
        }
    }

    public void restore() {
        view.setBackgroundColor(getThemeColor(android.R.attr.windowBackground));
    }

    protected int getThemeColor(String resAttrName) {
        int resAttrId = ResourceUtils.getResourceDeclareStyleableInt(view.getContext(), resAttrName);
        return getThemeColor(resAttrId);
    }

    protected int getThemeColor(int resAttrId) {
        TypedValue a = new TypedValue();
        Context context = view.getContext();
        context.getTheme().resolveAttribute(resAttrId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        }
        return 0;
    }
}
