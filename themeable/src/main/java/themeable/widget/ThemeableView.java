package themeable.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import themeable.Themeable;
import themeable.res.StyleOverride;
import themeable.res.StyleableConstants;

/**
 * Created by brett on 04/08/15.
 */
public class ThemeableView implements ViewOverride, StyleableConstants {

    private View view;
    protected int styleResId;

    private Drawable originalBackground;

    public ThemeableView(View view, int styleResId) {
        this.view = view;
        this.styleResId = styleResId;
        originalBackground = view.getBackground();
    }

    @Override
    public void overrideAppearance() {
        StyleOverride appearance = Themeable.getStyle(styleResId);
        if(appearance != null) {

            if (appearance.hasColorOrDrawable(backgroundColor)) {
                Drawable d = appearance.getDrawable(backgroundColor);
                if (d != null) {
                    setBackground(d);
                } else {
                    int color = appearance.getColor(backgroundColor, 0);
                    view.setBackgroundColor(color);
                }
            } else if (originalBackground != null) {
                setBackground(originalBackground);
            }
        }
    }

    protected void restore() {
        setBackground(originalBackground);
    }

    private void setBackground(Drawable background) {
        if(background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }
}
