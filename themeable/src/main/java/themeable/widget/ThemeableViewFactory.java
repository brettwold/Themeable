package themeable.widget;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by brett on 31/07/15.
 */
public class ThemeableViewFactory {

    public static ViewOverride getViewOverride(Object target, int styleResId) {

        if(List.class.isAssignableFrom(target.getClass())) {
            return getFromClass(((List)target).get(0).getClass(), target, styleResId);
        } else {
            return getFromClass(target.getClass(), target, styleResId);
        }
    }

    private static ViewOverride getFromClass(Class cls, Object target, int styleResId) {
        if (Button.class.isAssignableFrom(cls)) {
            return new ThemeableButton((Button) target, styleResId);
        } else if (TextView.class.isAssignableFrom(cls)) {
            return new ThemeableTextView((TextView) target, styleResId);
        }
        return null;
    }
}
