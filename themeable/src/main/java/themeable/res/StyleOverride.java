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
    private SparseArray<Boolean> bools = new SparseArray<Boolean>();
    private SparseArray<StateListColourDrawableBuilder> builders = new SparseArray<StateListColourDrawableBuilder>();

    public StyleOverride(int styleResId) {
        this.styleResId = styleResId;
    }

    public int getStyleResourceId() {
        return styleResId;
    }

    public void setColor(int resid, int color) {
        integerOverrides.put(resid, color);
    }

    public boolean hasColorOrDrawable(int resid) {
        return integerOverrides.indexOfKey(resid) >= 0 || drawables.indexOfKey(resid) >= 0 || builders.indexOfKey(resid) >= 0;
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
        if(builders.indexOfKey(resid) >= 0) {
            return builders.get(resid).getDrawable();
        }
        return drawables.get(resid);
    }

    public void setDrawable(int resid, Drawable drawable) {
        drawables.put(resid, drawable);
    }

    public void setDrawable(int resid, StateListColourDrawableBuilder stateListColourBuilder) {
        builders.put(resid, stateListColourBuilder);
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

    public void setBoolean(int resid, boolean value) {
        bools.put(resid, value);
    }

    public boolean getBoolean(int resid) {
        if(bools.indexOfKey(resid) >= 0) {
            return bools.get(resid);
        }
        return false;
    }

    public boolean hasBoolean(int resid) {
        return bools.indexOfKey(resid) >= 0;
    }

}