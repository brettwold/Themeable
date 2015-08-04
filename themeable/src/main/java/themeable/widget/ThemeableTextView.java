package themeable.widget;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.widget.TextView;

import themeable.Themeable;
import themeable.ThemeableFonts;
import themeable.res.ColorStateListWrapper;
import themeable.res.StyleOverride;
import themeable.res.StyleableConstants;

/**
 * Created by brett on 31/07/15.
 */
public class ThemeableTextView implements ViewOverride, StyleableConstants {

    private static final String TAG = ThemeableTextView.class.getSimpleName();

    private TextView textView;
    protected int styleResId;

    private Drawable originalBackground;
    private Typeface originalTypeface;
    private float originalTextSize;
    private ColorStateList originalColors;

    public ThemeableTextView(TextView textView, int styleResId) {
        this.textView = textView;
        this.styleResId = styleResId;
        originalBackground = textView.getBackground();
        originalTypeface = textView.getTypeface();
        originalTextSize = textView.getTextSize();
        originalColors = textView.getTextColors();
    }

    @Override
    public void overrideAppearance() {

        StyleOverride appearance = Themeable.getStyle(styleResId);
        if(appearance != null) {

            ColorStateListWrapper colors = appearance.getColorStateList(textColor);
            if (colors != null) {
                textView.setTextColor(colors.getColorStateList());
            }

            int color = appearance.getColor(textColorHighlight, 0);
            textView.setHighlightColor(color);

            Drawable d = appearance.getDrawable(backgroundColor);
            if(d != null) {
                setBackground(d);
            } else {
                color = appearance.getColor(backgroundColor, 0);
                textView.setBackgroundColor(color);
            }

            String font = appearance.getTypeface(typeface);
            if(font != null) {
                ThemeableFonts.setTypeface(textView.getContext(), textView, font);
            }

            int ts = appearance.getDimensionPixelSize(textSize, 0);
            if (ts != 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ts);
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
//            if (appearance.getBoolean(com.android.internal.R.styleable.TextAppearance_textAllCaps,
//                    false)) {
//                //            textView.setTransformationMethod(new AllCapsTransformationMethod(getContext()));
//            }

        } else {
            restore();
        }
    }

    private void restore() {
        textView.setTextAppearance(textView.getContext(), styleResId);
        setBackground(originalBackground);
        textView.setTypeface(originalTypeface);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
        textView.setTextColor(originalColors);
    }

    private void setBackground(Drawable originalBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(originalBackground);
        } else {
            textView.setBackgroundDrawable(originalBackground);
        }
    }
}
