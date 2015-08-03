package themeable.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by brett on 31/07/15.
 */
public class StyleBuilder implements StyleableConstants{

    private static final String TAG = StyleBuilder.class.getSimpleName();

    private Context context;
    private int styleResId;
    private StyleOverride overrideAppearance;
    private TypedArray originalTextAppearance;

    public StyleBuilder(Context context, int styleResId) {
        this.context = context;
        this.styleResId = styleResId;

        int[] attrs = getResourceDeclareStyleableIntArray(context, "TextAppearance");
        originalTextAppearance = context.obtainStyledAttributes(styleResId, attrs);

        overrideAppearance = new StyleOverride(styleResId);
    }

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

    public StyleBuilder setBackgroundColor(int color) {
        overrideAppearance.setColor(backgroundColor, color);
        return this;
    }

    public StyleBuilder setTextColorHighlight(int color) {
        overrideAppearance.setColor(android.R.attr.textColorHighlight, color);
        return this;
    }

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
