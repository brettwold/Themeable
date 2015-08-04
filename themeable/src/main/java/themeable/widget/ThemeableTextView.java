package themeable.widget;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

import themeable.Themeable;
import themeable.ThemeableFonts;
import themeable.res.ColorStateListWrapper;
import themeable.res.StyleOverride;

/**
 * Created by brett on 31/07/15.
 */
public class ThemeableTextView extends ThemeableView {

    private static final String TAG = ThemeableTextView.class.getSimpleName();

    private TextView textView;

    private Typeface originalTypeface;
    private float originalTextSize;
    private ColorStateList originalColors;

    public ThemeableTextView(TextView textView, int styleResId) {
        super(textView, styleResId);
        this.textView = textView;
        this.styleResId = styleResId;
        originalTypeface = textView.getTypeface();
        originalColors = textView.getTextColors();
        originalTextSize = textView.getTextSize();
    }

    @Override
    public void overrideAppearance() {

        super.overrideAppearance();

        StyleOverride appearance = Themeable.getStyle(styleResId);
        if(appearance != null) {

            ColorStateListWrapper colors = appearance.getColorStateList(textColor);
            if (colors != null) {
                textView.setTextColor(colors.getColorStateList());
            }

            if(appearance.hasColorOrDrawable(textColorHighlight)) {
                int color = appearance.getColor(textColorHighlight, 0);
                textView.setHighlightColor(color);
            }

            String font = appearance.getTypeface(typeface);
            if(font != null) {
                ThemeableFonts.setTypeface(textView.getContext(), textView, font);
            }

            int ts = appearance.getDimensionPixelSize(textSize, 0);
            if (ts != 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ts);
            }

            if(appearance.hasBoolean(textAllCaps)) {
                textView.setAllCaps(appearance.getBoolean(textAllCaps));
            }

//            colors = appearance.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColorHint);
//            if (colors != null) {
//                textView.setHintTextColor(colors);
//            }
//
//            colors = appearance.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColorLink);
//            if (colors != null) {
//                textView.setLinkTextColor(colors);
//            }
//
//
//            final int shadowcolor = appearance.getInt(com.android.internal.R.styleable.TextAppearance_shadowColor, 0);
//            if (shadowcolor != 0) {
//                final float dx = appearance.getFloat(com.android.internal.R.styleable.TextAppearance_shadowDx, 0);
//                final float dy = appearance.getFloat(com.android.internal.R.styleable.TextAppearance_shadowDy, 0);
//                final float r = appearance.getFloat(com.android.internal.R.styleable.TextAppearance_shadowRadius, 0);
//
//                textView.setShadowLayer(r, dx, dy, shadowcolor);
//            }
//
        } else {
            restore();
        }
    }

    protected void restore() {
        super.restore();
        textView.setTextAppearance(textView.getContext(), styleResId);
        textView.setTypeface(originalTypeface);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
        textView.setTextColor(originalColors);
    }

}
