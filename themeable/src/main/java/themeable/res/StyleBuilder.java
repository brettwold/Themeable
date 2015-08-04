package themeable.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;

import java.lang.reflect.Field;

/**
 * Created by brett on 31/07/15.
 */
public class StyleBuilder implements StyleableConstants {

    private static final String TAG = StyleBuilder.class.getSimpleName();

    private Context context;
    private int styleResId;
    private StyleOverride overrideAppearance;
    private TypedArray originalTextAppearance;
    private DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

    public StyleBuilder(Context context, int styleResId) {
        this.context = context;
        this.styleResId = styleResId;

        int[] attrs = getResourceDeclareStyleableIntArray(context, "TextAppearance");
        originalTextAppearance = context.obtainStyledAttributes(styleResId, attrs);

        overrideAppearance = new StyleOverride(styleResId);
    }

    /**
     * Sets the text colours of the view for the given states. If null or an empty array is
     * given for the states then the default colour will be overridden
     * @param states The android states when this colour should be applied
     * @param color The colour
     * @return StyleBuilder
     */
    public StyleBuilder setTextColor(int[] states, int color) {
        ColorStateListWrapper colors = overrideAppearance.getColorStateList(textColor);
        if(colors == null) {
            ColorStateList colorStateList = originalTextAppearance.getColorStateList(textColor);
            colors = new ColorStateListWrapper(colorStateList);
        }
        colors.setColor(states, color);
        overrideAppearance.setColorStateList(textColor, colors);
        return this;
    }

    /**
     * Overrides the background color of the view
     * @param color The color to override
     * @return StyleBuilder
     */
    public StyleBuilder setBackgroundColor(int color) {
        overrideAppearance.setColor(backgroundColor, color);
        return this;
    }

    /**
     * Overrides the background drawable of the view element with the style.
     * Can be a {@link StateListDrawable} object in the case
     * of a {@link Button}
     *
     * @param drawable The drawable to use when overriding
     * @return StyleBuilder
     */
    public StyleBuilder setBackground(Drawable drawable) {
        overrideAppearance.setDrawable(backgroundColor, drawable);
        return this;
    }

    public StyleBuilder setTextColorHighlight(int color) {
        overrideAppearance.setColor(textColorHighlight, color);
        return this;
    }

    /**
     * Sets the typeface used for view elements with this style.
     * The name given must have previously been registered
     * with the {@link themeable.ThemeableFonts} class.
     *
     * @param fontName The name of the font to set
     * @return StyleBuilder
     */
    public StyleBuilder setTypeface(String fontName) {
        overrideAppearance.setTypeface(typeface, fontName);
        return this;
    }

    /**
     * Sets the size of the text displayed on view elements with this style.
     * @param unit The unit of measurement for the value given should be one of the {@link TypedValue}
     *             constants e.g. TypedValue.COMPLEX_UNIT_SP
     * @param size The size to set
     * @return StyleBuilder
     */
    public StyleBuilder setTextSize(int unit, float size) {
        int px = getPixels(unit, size);
        overrideAppearance.setDimensionPixelSize(textSize, px);
        return this;
    }

    private int getPixels(int unit, float size) {
        return (int)TypedValue.applyDimension(unit, size, metrics);
    }

    /**
     * Sets the capitalisation of the text of a view on or off.
     * @param caps If ture all text will be captialised
     * @return StyleBuilder
     */
    public StyleBuilder setTextAllCaps(boolean caps) {
        overrideAppearance.setBoolean(textAllCaps, caps);
        return this;
    }

    /**
     * Build the new override and return the resultant {@link StyleOverride} object
     * @return The new {@link StyleOverride} object
     */
    public StyleOverride build() {
        originalTextAppearance.recycle();
        return overrideAppearance;
    }

    private static final int[] getResourceDeclareStyleableIntArray(Context context, String name) {
        try {
            Class cls = Class.forName(context.getPackageName() + ".R$styleable");
            Field f = cls.getField(name);
            if(f != null) {
                int[] ret = (int[])f.get(null);
                return ret;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get resource declared array", e);
        }

        return null;
    }

}
