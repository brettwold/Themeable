package themeable;

import android.content.Context;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import themeable.res.StyleOverride;

public class Themeable {

    private static final String TAG = Themeable.class.getSimpleName();

    public static final String SUFFIX = "$$StyleBinder";

    private static final Map<Class<?>, StyleBinder> binders = new LinkedHashMap<>();
    private static final SparseArray<StyleOverride> overrides = new SparseArray<>();

    private static MaterialPalette palette;

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
            Log.e(TAG, "Failed to find binding class", e);
        }
    }

    /**
     * Applies a whole theme to all bound views at once
     *
     * @param theme The {@link themeable.Themeable.Theme} to apply
     */
    public static void applyTheme(Theme theme) {
        palette = theme.getPalette();
        notifyBindersChromeChange();
        applyStyles(theme.getOverrides().toArray(new StyleOverride[0]));
    }

    /**
     * Applies the given styles to all bound views registered to the same style ids
     *
     * @param styleOverrides A list of {@link StyleOverride} objects to apply
     */
    public static void applyStyles(StyleOverride... styleOverrides) {
        if(bound) {
            for (StyleOverride o : styleOverrides) {
                overrides.put(o.getStyleResourceId(), o);
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

    private static void notifyBindersStyleChange() {
        for(StyleBinder binder : binders.values()) {
            binder.notifyStyleChange();
        }
    }

    private static void notifyBindersChromeChange() {
        for(StyleBinder binder : binders.values()) {
            binder.notifyChromeChange();
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

        private Context context;
        private MaterialPalette palette;
        private Set<StyleOverride> overrides = new HashSet<>();

        public static Theme newInstance(Context context) {
            return new Theme(context);
        }

        private Theme(Context context) {
            this.context = context;
        }

        public Theme setPalette(MaterialPalette palette) {
            this.palette = palette;
            return this;
        }

        public Theme addStyle(StyleOverride styleOverride) {
            overrides.add(styleOverride);
            return this;
        }

        public MaterialPalette getPalette() {
            return palette;
        }

        public Set<StyleOverride> getOverrides() {
            return overrides;
        }
    }
}
