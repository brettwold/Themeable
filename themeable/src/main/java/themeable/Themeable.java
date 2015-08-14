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

import android.support.annotation.StyleRes;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import themeable.images.ImageCache;
import themeable.res.ImageOverride;
import themeable.res.StyleOverride;

public class Themeable {

    private static final String TAG = Themeable.class.getSimpleName();

    public static final String SUFFIX = "$$StyleBinder";

    private static final Map<Class<?>, StyleBinder> binders = new LinkedHashMap<>();
    private static final SparseArray<StyleOverride> overrides = new SparseArray<>();

    private static MaterialPalette palette;
    private static Theme currentTheme;

    private static boolean bound = false;

    /**
     * Binds all objects found with a {@link @BindStyle} annotation in the given source object
     *
     * @param source The object to scan for {@link @BindStyle} bindings
     * @param rootView The root Android View associated with the source object given
     */
    public static final void bind(Object source, View rootView) {
        try {
            StyleBinder styleBinder = findStyleBinderForClass(source.getClass());
            styleBinder.bind(source, rootView);
            bound = true;
            notifyBindersStyleChange();
            notifyBindersChromeChange();
        } catch (Exception e) {
            Log.i(TAG, "Failed to find binding for class: " + source.getClass().getName() + " no binding will occur. Attempt to bind an object with no @BindStyle annotations");
        }
    }

    /**
     * Unbinds all objects previously bound.
     * NOTE: This does NOT restore there previous styling
     *
     * @param source The object to scan for {@link @BindStyle} bindings
     */
    public static final void unbind(Object source) {
        try {
            StyleBinder styleBinder = findStyleBinderForClass(source.getClass());
            styleBinder.unbind(source);
            bound = false;
        } catch (Exception e) {
            Log.i(TAG, "Failed to find binding for class: " + source.getClass().getName() + " no binding will occur. Attempt to bind an object with no @BindStyle annotations");
        }
    }

    /**
     * Applies a whole theme to all bound views at once
     *
     * @param theme The {@link themeable.Themeable.Theme} to apply
     */
    public static void applyTheme(Theme theme) {
        if(currentTheme != null) {
            ImageCache.restore(currentTheme);
        }
        currentTheme = theme;
        palette = theme.getPalette();
        notifyBindersChromeChange();
        applyStyles(theme.getOverrides().toArray(new StyleOverride[0]));
        ImageCache.applyImages(theme);
    }

    /**
     * Applies the given styles to all bound views registered to the same style ids
     *
     * @param styleOverrides A list of {@link StyleOverride} objects to apply
     */
    public static void applyStyles(StyleOverride... styleOverrides) {
        if(bound) {
            for (StyleOverride o : styleOverrides) {
                if(o != null) {
                    overrides.put(o.getStyleResourceId(), o);
                } else {
                    throw new RuntimeException("Attempt to apply dynamic style to an unknown style resource");
                }
            }
            notifyBindersStyleChange();
            return;
        }
        throw new RuntimeException("Attempt to apply styles before Themeables are bound. bind() method must be called first");
    }

    /**
     * Removes all theming and styling
     */
    public static void removeTheme() {
        if(currentTheme != null) {
            ImageCache.restore(currentTheme);
        }
        overrides.clear();
        palette = null;
        notifyBindersChromeChange();
        notifyBindersStyleChange();
    }

    /**
     * Removes any styles that have been customised and attempts to restore the original style
     * as per the applications registered Theme
     *
     * @param styleResIds The style ids to remove overrides from
     */
    public static void removeStyles(@StyleRes int... styleResIds) {
        if(bound) {
            for (int resid : styleResIds) {
                overrides.remove(resid);
            }
            notifyBindersStyleChange();
            return;
        }
        throw new RuntimeException("Attempt to remove styles before Themeables are bound. bind() method must be called first");
    }

    /**
     * Removes all images stored in the ImageCache so they will be forced to be re-loaded.
     */
    public static void clearImageCache() {
        ImageCache.clear();
    }

    private static void notifyBindersStyleChange() {
        for(final StyleBinder binder : binders.values()) {
            View v = binder.getRootView();
            v.post(new Runnable() {
                @Override
                public void run() {
                    binder.notifyStyleChange();
                }
            });
        }
    }

    private static void notifyBindersChromeChange() {
        for(final StyleBinder binder : binders.values()) {
            View v = binder.getRootView();
            v.post(new Runnable() {
                @Override
                public void run() {
                    binder.notifyChromeChange();
                }
            });
        }
    }

    /**
     * Return the {@link StyleOverride} for a style if any has been setup for this style id
     *
     * @param resid The style id to find an override for
     *
     * @return A {@link StyleOverride} object if one has been created otherwise null
     */
    public static StyleOverride getStyle(@StyleRes int resid) {
        return overrides.get(resid);
    }

    public static MaterialPalette getCurrentPalette() {
        return palette;
    }

    private static StyleBinder findStyleBinderForClass(Class<?> cls) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        StyleBinder styleBinder = binders.get(cls);
        if (styleBinder != null) {
            return styleBinder;
        }

        Class<?> viewBindingClass = Class.forName(cls.getName() + SUFFIX);
        styleBinder = (StyleBinder) viewBindingClass.newInstance();
        binders.put(cls, styleBinder);
        return styleBinder;
    }

    public static final class Theme {

        private String name;
        private MaterialPalette palette;
        private Set<StyleOverride> styles = new HashSet<>();
        private Set<ImageOverride> images = new HashSet<>();

        public static Theme newInstance(String themeName) {
            return new Theme(themeName);
        }

        private Theme(String themeName) {
            if(themeName == null || themeName.isEmpty()) {
                throw new IllegalArgumentException("Theme name cannot be null");
            }

            if(!themeName.matches("[a-zA-Z].*")) {
                throw new IllegalArgumentException("Theme name should consist only of letters");
            }

            this.name = themeName;
        }

        public String getThemeName() {
            return name;
        }

        public Theme setPalette(MaterialPalette palette) {
            this.palette = palette;
            return this;
        }

        public Theme addStyle(StyleOverride styleOverride) {
            styles.add(styleOverride);
            return this;
        }

        public Theme addImage(ImageOverride imageOverride) {
            images.add(imageOverride);
            return this;
        }

        public MaterialPalette getPalette() {
            return palette;
        }

        public Set<StyleOverride> getOverrides() {
            return styles;
        }

        public Set<ImageOverride> getImageOverrides() {
            return images;
        }
    }
}