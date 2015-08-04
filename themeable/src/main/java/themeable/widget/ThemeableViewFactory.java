package themeable.widget;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by brett on 31/07/15.
 */
public class ThemeableViewFactory {

    public static ViewOverride getViewOverride(View target, int styleResId) {
        Class cls = target.getClass();
        if (Button.class.isAssignableFrom(cls)) {
            return new ThemeableButton((Button) target, styleResId);
        } else if (TextView.class.isAssignableFrom(cls)) {
            return new ThemeableTextView((TextView) target, styleResId);
        }
        return null;
    }
}
