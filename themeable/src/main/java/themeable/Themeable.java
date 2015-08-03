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

    static final Map<Class<?>, StyleBinder> binders = new LinkedHashMap<>();

    static final SparseArray<StyleOverride> overrides = new SparseArray<>();

    public static final void bind(Object source, View rootView) {
        try {
            StyleBinder styleBinder = findStyleBinderForClass(source.getClass());
            styleBinder.bind(source, rootView);
        } catch (Exception e) {
            Log.e(TAG, "Failed to find binding class", e);
        }
    }

    public static final void unbind(Object source) {

    }

    public static void setStyle(StyleOverride override) {
        overrides.put(override.getStyleResourceId(), override);
        notifyBinders(override.getStyleResourceId());
    }

    private static void notifyBinders(int styleResId) {
        for(StyleBinder binder : binders.values()) {
            binder.notifyStyleChange(styleResId);
        }
    }

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
