package themeable.res;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;

/**
 * Created by brett on 06/08/15.
 */
public class ImageBuilder {

    private ImageOverride imageOverride = new ImageOverride();
    private DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

    public ImageBuilder(Context context, String key) {
        if(key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        imageOverride.setKey(key);
    }

    public ImageBuilder setUrl(String url) {
        if(url == null || !Patterns.WEB_URL.matcher(url).matches()) {
            throw new IllegalArgumentException("Invalid or null URL passed to addImage");
        }
        imageOverride.setUrl(url);
        return this;
    }

    public ImageBuilder setRestoreResourceId(@DrawableRes int restoreResourceId) {
        imageOverride.setRestoreId(restoreResourceId);
        return this;
    }

    public ImageBuilder setMaxHeight(int unit, int size) {
        imageOverride.setMaxHeightPixels(getPixels(unit, size));
        return this;
    }

    public ImageBuilder setMaxWidth(int unit, int size) {
        imageOverride.setMaxWidthPixels(getPixels(unit, size));
        return this;
    }

    private int getPixels(int unit, float size) {
        return (int) TypedValue.applyDimension(unit, size, metrics);
    }

    public ImageOverride build() {
        return imageOverride;
    }
}
