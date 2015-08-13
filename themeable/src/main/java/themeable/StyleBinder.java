package themeable;

import android.view.View;

/**
 * Created by brett on 30/07/15.
 */
public abstract class StyleBinder<T> {

    public abstract void bind(T source, View view);

    public abstract View getRootView();

    public abstract void unbind(T source);

    public abstract void notifyStyleChange();

    public abstract void notifyChromeChange();
}
