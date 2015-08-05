package themeable.widget;

import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedViewFactory {

    public static ChromeOverride getChromeOverride(View target) {
        Class cls = target.getClass();
        if (Toolbar.class.isAssignableFrom(cls)) {
            return new ChromedToolbar((Toolbar)target);
        }

        return new ChromedView(target);
    }
}
