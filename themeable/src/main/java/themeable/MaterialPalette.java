package themeable;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by brett on 05/08/15.
 */
public class MaterialPalette {

    @ColorInt
    private int primaryColor;

    @ColorInt
    private int primaryDarkColor;

    @ColorInt
    private int primaryLightColor;

    @ColorInt
    private int accentColor;

    @ColorInt
    private int textPrimaryColor;

    @ColorInt
    private int textSecondaryColor;

    @ColorInt
    private int textIconsColor;

    @ColorInt
    private int windowBackgroundColor;

    public static MaterialPalette build(String primary, String primaryDark, String primaryLight, String accent,
                                        String textPrimary, String textSecondary, String textIcons,
                                        String windowBackground) throws IllegalArgumentException {
        MaterialPalette palette = new MaterialPalette();
        palette.primaryColor = Color.parseColor(primary);
        palette.primaryDarkColor = Color.parseColor(primaryDark);
        palette.primaryLightColor = Color.parseColor(primaryLight);
        palette.accentColor = Color.parseColor(accent);
        palette.textPrimaryColor = Color.parseColor(textPrimary);
        palette.textSecondaryColor = Color.parseColor(textSecondary);
        palette.textIconsColor = Color.parseColor(textIcons);
        palette.windowBackgroundColor = Color.parseColor(windowBackground);
        return palette;
    }

    private MaterialPalette() {

    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getPrimaryLightColor() {
        return primaryLightColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getTextPrimaryColor() {
        return textPrimaryColor;
    }

    public int getTextSecondaryColor() {
        return textSecondaryColor;
    }

    public int getTextIconsColor() {
        return textIconsColor;
    }

    public int getWindowBackgroundColor() {
        return windowBackgroundColor;
    }
}
