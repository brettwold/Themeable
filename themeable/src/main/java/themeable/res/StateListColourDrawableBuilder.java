package themeable.res;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;

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
        stateColoursList.add(new StateColour(states, colour));
        return this;
    }

    public Drawable getDrawable() {
        Log.d("XXX", "building SLD");
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
