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

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brett on 04/08/15.
 */
public class StateListColourDrawableBuilder {

    private int defaultColour;
    private List<StateColour> stateColoursList = new ArrayList<>();

    public StateListColourDrawableBuilder(int defaultColour) {
        this.defaultColour = defaultColour;
    }

    public StateListColourDrawableBuilder addStateColour(int[] states, int colour) {

        for(int state: states) {
            if(!ResourceUtils.isValidState(state)) {
                throw new IllegalArgumentException("Invalid state passed to StateListColourDrawableBuilder: " + state);
            }
        }

        stateColoursList.add(new StateColour(states, colour));
        return this;
    }

    public Drawable getDrawable() {
        StateListDrawable sld = new StateListDrawable();
        for (StateColour stateColour : stateColoursList) {
            sld.addState(stateColour.states, new ColorDrawable(stateColour.colour));
        }
        sld.addState(new int[]{}, new ColorDrawable(defaultColour));
        return sld;
    }

    class StateColour {
        int[] states;
        int colour;

        public StateColour(int[] states, int colour) {
            this.states = states;
            this.colour = colour;
        }
    }
}
