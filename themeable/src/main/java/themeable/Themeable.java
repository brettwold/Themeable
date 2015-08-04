package themeable;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

import themeable.res.StyleOverride;

public class Themeable {

    private static final String TAG = Themeable.class.getSimpleName();

    public static final String SUFFIX = "$$StyleBinder";

    private static final Map<Class<?>, StyleBinder> binders = new LinkedHashMap<>();
    private static final SparseArray<StyleOverride> overrides = new SparseArray<>();

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
            notifyBinders();
        } catch (Exception e) {
            Log.e(TAG, "Failed to find binding class", e);
        }
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
            notifyBinders();
            return;
        }
        throw new RuntimeException("Attempt to apply styles before Themeables are bound. bind() method must be called first");
    }

    /**
     * Removes any styles that have been customised and attempts to restore the original style
     * as per the applications registered Theme
     *
     * @param styleResIds The style ids to remove overrides from
     */
    public static void removeStyles(int... styleResIds) {
        if(bound) {
            for (int resid : styleResIds) {
                overrides.remove(resid);
            }
            notifyBinders();
            return;
        }
        throw new RuntimeException("Attempt to remove styles before Themeables are bound. bind() method must be called first");
    }

    private static void notifyBinders() {
        for(StyleBinder binder : binders.values()) {
            binder.notifyStyleChange();
        }
    }

    /**
     * Return the {@link StyleOverride} for a style if any has been setup for this style id
     *
     * @param resid The style id to find an override for
     *
     * @return A {@link StyleOverride} object if one has been created otherwise null
     */
    public static StyleOverride getStyle(int resid) {
        return overrides.get(resid);
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
}
