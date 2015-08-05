package themeable.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;

import themeable.MaterialPalette;
import themeable.Themeable;
import themeable.res.ResourceUtils;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedToolbar extends ChromedView {

    private static final int colorPrimary = 80;

    private Toolbar toolbar;

    public ChromedToolbar(Toolbar toolbar) {
        super(toolbar);
        this.toolbar = toolbar;
    }

    @Override
    public void overrideAppearance() {
        MaterialPalette palette = Themeable.getCurrentPalette();
        if(palette != null) {
            toolbar.setBackgroundColor(palette.getPrimaryColor());
            toolbar.setTitleTextColor(palette.getTextIconsColor());
            toolbar.setSubtitleTextColor(palette.getTextSecondaryColor());
        } else {
            restore();
        }
    }

    public void restore() {
        Context context = toolbar.getContext();
        int[] attrs = ResourceUtils.getResourceDeclareStyleableIntArray(context, "Theme");
        TypedArray typedArray = context.obtainStyledAttributes(android.R.style.Theme, attrs);
        toolbar.setBackgroundColor(typedArray.getColor(colorPrimary, 0));
    }
}
