package themeable.res;

import android.content.res.ColorStateList;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by brett on 03/08/15.
 */
public class ColorStateListWrapper {

    private static final String TAG = ColorStateListWrapper.class.getSimpleName();

    private int[][] states;
    private int[] colors;

    public ColorStateListWrapper(ColorStateList original) {
        try {
            Field colorsField = ColorStateList.class.getDeclaredField("mColors");
            colorsField.setAccessible(true);
            int[] colourSpec = (int[]) colorsField.get(original);

            Field statesField = ColorStateList.class.getDeclaredField("mStateSpecs");
            statesField.setAccessible(true);
            int[][] stateSpec = (int[][]) statesField.get(original);

            colors = new int[colourSpec.length];
            states = new int[stateSpec.length][20];
            System.arraycopy(colourSpec, 0, colors, 0, stateSpec.length);
            System.arraycopy(stateSpec, 0, states, 0, stateSpec.length);

        } catch(Exception e) {
            Log.e(TAG, "Failed to get ColorStateList member variables", e);
        }
    }

    public void setColor(int[] colorStates, int color) {
        if(colorStates == null || colorStates.length == 0) {
            Log.d(TAG, "Setting default color: " + color);
            colors[0] = color;
        } else {
            boolean exists = false;
            for (int i = 0; i < states.length; i++) {
                int[] stateMatch = states[i];
                if (Arrays.equals(stateMatch, colorStates)) {
                    colors[i] = color;
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                int[][] stateSpecList = new int[states.length + 1][20];
                int[] colorList = new int[stateSpecList.length];

                System.arraycopy(colors, 0, colorList, 0, colors.length);
                System.arraycopy(states, 0, stateSpecList, 0, states.length);

                stateSpecList[stateSpecList.length - 1] = colorStates;
                colorList[stateSpecList.length - 1] = color;

                colors = colorList;
                states = stateSpecList;
            }
        }
    }

    public ColorStateList getColorStateList() {
        return new ColorStateList(states, colors);
    }
}
