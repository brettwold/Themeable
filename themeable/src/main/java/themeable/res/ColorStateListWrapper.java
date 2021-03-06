/*
 * Copyright (C)2015 Brett Cherrington
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
    private int defaultColour;
    private boolean hasDefault = false;

    private ColorStateList csl;

    public ColorStateListWrapper() {
        colors = new int[1];
        states = new int[1][20];
        states[0] = new int[] {};
    }

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

        boolean exists = false;
        for (int i = 0; i < states.length; i++) {
            int[] stateMatch = states[i];
            if ((stateMatch.length == 0 && colorStates == null) || Arrays.equals(stateMatch, colorStates)) {
                colors[i] = color;
                exists = true;
                break;
            }
        }

        if (!exists) {
            if(colorStates == null || colorStates.length == 0) {
                defaultColour = color;
                hasDefault = true;
            } else {
                addStateColour(colorStates, color, false);
            }
        }
    }

    private void addStateColour(int[] colorStates, int color, boolean end) {
        int[][] stateSpecList = new int[states.length + 1][20];
        int[] colorList = new int[stateSpecList.length];

        if(!end) {
            System.arraycopy(colors, 0, colorList, 1, colors.length);
            System.arraycopy(states, 0, stateSpecList, 1, states.length);
            stateSpecList[0] = colorStates;
            colorList[0] = color;
        } else {
            System.arraycopy(colors, 0, colorList, 0, colors.length);
            System.arraycopy(states, 0, stateSpecList, 0, states.length);
            stateSpecList[stateSpecList.length - 1] = colorStates;
            colorList[stateSpecList.length - 1] = color;
        }

        colors = colorList;
        states = stateSpecList;
    }

    public ColorStateList getColorStateList() {
        if(hasDefault && csl == null) {
            addStateColour(new int[] {}, defaultColour, true);
        }

        if(csl == null) {
            csl = new ColorStateList(states, colors);
        }
        return csl;
    }
}
