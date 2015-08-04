package themeable.res;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import themeable.ThemeableFonts;

/**
 * Created by brett on 31/07/15.
 */
public class StyleOverride {

    private static final String TAG = StyleOverride.class.getSimpleName();

    private int styleResId;

    private SparseIntArray integerOverrides = new SparseIntArray();
    private SparseArray<ColorStateListWrapper> colorStateListOverrides = new SparseArray<ColorStateListWrapper>();
    private SparseArray<Drawable> drawables = new SparseArray<Drawable>();
    private SparseArray<String> fonts = new SparseArray<String>();
    private SparseIntArray dimensions = new SparseIntArray();

    public StyleOverride(int styleResId) {
        this.styleResId = styleResId;
    }

    public int getStyleResourceId() {
        return styleResId;
    }

    public void setColor(int resid, int color) {
        integerOverrides.put(resid, color);
    }

    public int getColor(int resid, int defaultColor) {
        if(integerOverrides.indexOfKey(resid) >= 0) {
            return integerOverrides.get(resid);
        }
        return defaultColor;
    }

    public ColorStateListWrapper getColorStateList(int resid) {
        if(colorStateListOverrides.indexOfKey(resid) >= 0) {
            return colorStateListOverrides.get(resid);
        }
        return null;
    }

    public void setColorStateList(int resid, ColorStateListWrapper colorStateList) {
        colorStateListOverrides.put(resid, colorStateList);
    }

    public Drawable getDrawable(int resid) {
        return drawables.get(resid);
    }

    public void setDrawable(int resid, Drawable drawable) {
        drawables.put(resid, drawable);
    }

    public void setTypeface(int resid, String fontName) {
        if(ThemeableFonts.hasFont(fontName)) {
            fonts.put(resid, fontName);
        }
    }

    public String getTypeface(int resid) {
        return fonts.get(resid);
    }

    public void setDimensionPixelSize(int resid, int size) {
        dimensions.put(resid, size);
    }

    public int getDimensionPixelSize(int resid, int defaultValue) {
        if(dimensions.indexOfKey(resid) >= 0) {
            return dimensions.get(resid);
        }
        return defaultValue;
    }

}