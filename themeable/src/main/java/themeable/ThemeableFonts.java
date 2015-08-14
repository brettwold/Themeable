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

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brett on 16/07/15.
 */
public class ThemeableFonts {

    private static Map<String, String> fontMap = new HashMap();
    private static Map<String, Typeface> typefaceCache = new HashMap();

    /**
     * Register a new font with this utility. The fontName given should be unique and the the
     * font file itself must exist in the assets folder of your application
     *
     * @param fontName A unique name for this font
     * @param assetPath The path within in the assets folder of the font file (e.g. fonts/Roboto-Bold.ttf)
     */
    public static void registerTypeface(String fontName, String assetPath) {
        fontMap.put(fontName, assetPath);
    }

    /**
     * Check to see if this utility is holding a font with the given name
     * @param fontName The name of the font to find
     * @return True if previously registered
     */
    public static boolean hasFont(String fontName) {
        return fontMap.containsKey(fontName);
    }

    private static Typeface getTypeface(Context context, String fontName) {
        String fontPath = fontMap.get(fontName);
        if (!typefaceCache.containsKey(fontName)) {
            typefaceCache.put(fontName, Typeface.createFromAsset(context.getAssets(), fontPath));
        }
        return typefaceCache.get(fontName);
    }

    /**
     * Iterates all ViewGroups, finds TextViews and applies the given font. If the font is not
     * known or cannot be found from the assets folder then this method will just fail gracefully
     *
     * @param context The context
     * @param view Root view to apply typeface to
     * @param fontName The name of the font to apply
     */
    public static void setTypeface(Context context, View view, String fontName) {
        if(fontMap.containsKey(fontName)) {
            Typeface typeface = getTypeface(context, fontName);
            if(typeface != null) {
                if (view instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                        setTypeface(context, ((ViewGroup) view).getChildAt(i), fontName);
                    }
                } else if (view instanceof TextView) {
                    ((TextView) view).setTypeface(typeface);
                }
            }
        }
    }
}

