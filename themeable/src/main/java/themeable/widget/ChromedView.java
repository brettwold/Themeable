package themeable.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import themeable.MaterialPalette;
import themeable.Themeable;
import themeable.res.ResourceUtils;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedView implements ChromeOverride {

    private View view;

    public ChromedView(View view) {
        this.view = view;
    }

    @Override
    public void overrideAppearance() {
        MaterialPalette palette = Themeable.getCurrentPalette();
        if(palette != null) {
            view.setBackgroundColor(palette.getWindowBackgroundColor());
        } else {
            restore();
        }
    }

    public void restore() {
        view.setBackgroundColor(getThemeColor(android.R.attr.windowBackground));
    }

    protected int getThemeColor(String resAttrName) {
        int resAttrId = ResourceUtils.getResourceDeclareStyleableInt(view.getContext(), resAttrName);
        return getThemeColor(resAttrId);
    }

    protected int getThemeColor(int resAttrId) {
        TypedValue a = new TypedValue();
        Context context = view.getContext();
        context.getTheme().resolveAttribute(resAttrId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        }
        return 0;
    }
}
