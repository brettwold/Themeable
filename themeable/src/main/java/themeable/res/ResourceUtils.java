package themeable.res;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by brett on 04/08/15.
 */
public class ResourceUtils {

    private static final String TAG = ResourceUtils.class.getSimpleName();

    public static final int[] getResourceDeclareStyleableIntArray(Context context, String name) {
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

    public static final int getResourceDeclareStyleableInt(Context context, String name) {
        try {
            Class cls = Class.forName(context.getPackageName() + ".R$styleable");
            Field f = cls.getField(name);
            if(f != null) {
                int ret = (int)f.get(null);
                return ret;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get resource declared int", e);
        }

        return 0;
    }
}
