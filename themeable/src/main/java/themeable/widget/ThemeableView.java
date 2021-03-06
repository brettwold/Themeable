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

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import themeable.Themeable;
import themeable.res.StyleOverride;
import themeable.res.StyleableConstants;

/**
 * Created by brett on 04/08/15.
 */
public class ThemeableView implements ViewOverride, StyleableConstants {

    private View view;
    protected int styleResId;

    private Drawable originalBackground;
    private int[] originalPadding = new int[4];
    private ViewGroup.LayoutParams originalLayoutParams;

    public ThemeableView(View view, int styleResId) {
        this.view = view;
        this.styleResId = styleResId;
        originalBackground = view.getBackground();
        originalPadding[0] = view.getPaddingLeft();
        originalPadding[1] = view.getPaddingTop();
        originalPadding[2] = view.getPaddingRight();
        originalPadding[3] = view.getPaddingBottom();
        originalLayoutParams = view.getLayoutParams();

    }

    @Override
    public void overrideAppearance() {
        StyleOverride appearance = Themeable.getStyle(styleResId);
        if(appearance != null) {

            if (appearance.hasColorOrDrawable(backgroundColor)) {
                Drawable d = appearance.getDrawable(backgroundColor);
                if (d != null) {
                    setBackground(d);
                } else {
                    int color = appearance.getColor(backgroundColor, 0);
                    view.setBackgroundColor(color);
                }
            } else if (originalBackground != null) {
                setBackground(originalBackground);
            }

            if(appearance.hasBoolean(padding)) {
                view.setPadding(appearance.getDimensionPixelSize(paddingLeft, 0),
                        appearance.getDimensionPixelSize(paddingTop, 0),
                        appearance.getDimensionPixelSize(paddingRight, 0),
                        appearance.getDimensionPixelSize(paddingBottom, 0));
            }
        }
    }

    protected void restore() {
        setBackground(originalBackground);

        view.setPadding(originalPadding[0], originalPadding[1], originalPadding[2], originalPadding[3]);
    }

    private void setBackground(Drawable background) {
        if(background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }
}
