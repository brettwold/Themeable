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
    private int[] originalPadding = new int[4];

    public ThemeableView(View view, int styleResId) {
        this.view = view;
        this.styleResId = styleResId;
        originalBackground = view.getBackground();
        originalPadding[0] = view.getPaddingLeft();
        originalPadding[1] = view.getPaddingTop();
        originalPadding[2] = view.getPaddingRight();
        originalPadding[3] = view.getPaddingBottom();
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

            if(appearance.hasBoolean(padding)) {
                view.setPadding(appearance.getDimensionPixelSize(paddingLeft, 0),
                        appearance.getDimensionPixelSize(paddingTop, 0),
                        appearance.getDimensionPixelSize(paddingRight, 0),
                        appearance.getDimensionPixelSize(paddingBottom, 0));
            }
        }
    }

    protected void restore() {
        setBackground(originalBackground);

        view.setPadding(originalPadding[0], originalPadding[1], originalPadding[2], originalPadding[3]);
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
