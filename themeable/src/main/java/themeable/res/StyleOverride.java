package themeable.res;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * Created by brett on 31/07/15.
 */
public class StyleOverride {

    private static final String TAG = StyleOverride.class.getSimpleName();

    private int styleResId;

    private SparseIntArray integerOverrides = new SparseIntArray();
    private SparseArray<ColorStateListWrapper> colorStateListOverrides = new SparseArray<ColorStateListWrapper>();

    public StyleOverride(int styleResId) {
        this.styleResId = styleResId;
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

    public int getStyleResourceId() {
        return styleResId;
    }

    public Drawable getDrawable(int resid) {
        return null;
    }


}