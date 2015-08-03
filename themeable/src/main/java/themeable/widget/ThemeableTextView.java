package themeable.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import themeable.Themeable;
import themeable.res.ColorStateListWrapper;
import themeable.res.StyleOverride;
import themeable.res.StyleableConstants;

/**
 * Created by brett on 31/07/15.
 */
public class ThemeableTextView implements ViewOverride, StyleableConstants {

    private static final String TAG = ThemeableTextView.class.getSimpleName();

    private TextView textView;
    private List<TextView> textViews;
    private int styleResId;

    public ThemeableTextView(TextView textView, int styleResId) {
        this.textView = textView;
        this.styleResId = styleResId;
    }

    public ThemeableTextView(List<TextView> textViews, int styleResId) {
        this.textViews = textViews;
        this.styleResId = styleResId;
    }

    @Override
    public void overrideAppearance() {
        if(textView != null) {
            overrideAppearance(textView);
        } else if(textViews != null) {
            for (TextView tv : textViews) {
                overrideAppearance(tv);
            }
        }
    }

    private void overrideAppearance(TextView tv) {
        StyleOverride appearance = Themeable.getStyle(styleResId);
        if(appearance != null) {

            int color;
            ColorStateListWrapper colors;
            int ts;

            colors = appearance.getColorStateList(textColor);
            if (colors != null) {
                Log.d(TAG, "Got colors: " + colors.toString() + " default: " + colors.getColorStateList().hashCode());
                tv.setTextColor(colors.getColorStateList());
            }

            color = appearance.getColor(android.R.attr.textColorHighlight, 0);
            tv.setHighlightColor(color);

            Drawable d = appearance.getDrawable(backgroundColor);
            if(d != null) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv.setBackground(d);
                } else {
                    tv.setBackgroundDrawable(d);
                }
            } else {
                color = appearance.getColor(backgroundColor, 0);
                tv.setBackgroundColor(color);
            }
//
//            ts = appearance.getDimensionPixelSize(com.android.internal.R.styleable.TextAppearance_textSize, 0);
//            if (ts != 0) {
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ts);
//            }
//
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
//            //        String familyName;
//            //        int typefaceIndex, styleIndex;
//            //
//            //        familyName = appearance.getString(com.android.internal.R.styleable.TextAppearance_fontFamily);
//            //        typefaceIndex = appearance.getInt(com.android.internal.R.styleable.TextAppearance_typeface, -1);
//            //        styleIndex = appearance.getInt(com.android.internal.R.styleable.TextAppearance_textStyle, -1);
//            //
//                    textView.setTypefaceFromAttrs(familyName, typefaceIndex, styleIndex);
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
            //
            //        if (appearance.hasValue(com.android.internal.R.styleable.TextAppearance_elegantTextHeight)) {
            //            textView.setElegantTextHeight(appearance.getBoolean(com.android.internal.R.styleable.TextAppearance_elegantTextHeight, false));
            //        }
            //
            //        if (appearance.hasValue(com.android.internal.R.styleable.TextAppearance_letterSpacing)) {
            //            textView.setLetterSpacing(appearance.getFloat(com.android.internal.R.styleable.TextAppearance_letterSpacing, 0));
            //        }
            //
            //        if (appearance.hasValue(com.android.internal.R.styleable.TextAppearance_fontFeatureSettings)) {
            //            textView.setFontFeatureSettings(appearance.getString(com.android.internal.R.styleable.TextAppearance_fontFeatureSettings));
            //        }

            //appearance.recycle();
        }
    }
}
