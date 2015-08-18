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

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brett on 04/08/15.
 */
public class ResourceUtils {

    private static final String TAG = ResourceUtils.class.getSimpleName();

    private static final Map<String, Integer> DIMENSION_LOOKUP = initDimens();
    private static Map<String, Integer> initDimens() {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("px", TypedValue.COMPLEX_UNIT_PX);
        m.put("dip", TypedValue.COMPLEX_UNIT_DIP);
        m.put("dp", TypedValue.COMPLEX_UNIT_DIP);
        m.put("sp", TypedValue.COMPLEX_UNIT_SP);
        m.put("pt", TypedValue.COMPLEX_UNIT_PT);
        m.put("in", TypedValue.COMPLEX_UNIT_IN);
        m.put("mm", TypedValue.COMPLEX_UNIT_MM);
        return Collections.unmodifiableMap(m);
    }

    private static final Pattern DIMENSION_PATTERN = Pattern.compile("^\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$");

    private static final Map<String, Integer> STATE_LOOKUP = initStates();
    private static Map<String, Integer> initStates() {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("state_pressed", android.R.attr.state_activated);
        m.put("state_active", android.R.attr.state_active);
        m.put("state_checkable", android.R.attr.state_checkable);
        m.put("state_checked", android.R.attr.state_checked);
        m.put("state_enabled", android.R.attr.state_enabled);
        m.put("state_first", android.R.attr.state_first);
        m.put("state_focused", android.R.attr.state_focused);
        m.put("state_last", android.R.attr.state_last);
        m.put("state_middle", android.R.attr.state_middle);
        m.put("state_pressed", android.R.attr.state_pressed);
        m.put("state_selected", android.R.attr.state_selected);
        m.put("state_single", android.R.attr.state_single);
        m.put("state_window_focused", android.R.attr.state_window_focused);
        return Collections.unmodifiableMap(m);
    }

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

    public static int getStateFromString(String state) {
        return STATE_LOOKUP.get(state);
    }

    public static int getUnitFromString(String unit) {
        return DIMENSION_LOOKUP.get(unit);
    }

    public static ResourceDimension stringToDimension(String dimension) {
        Matcher matcher = DIMENSION_PATTERN.matcher(dimension);
        if (matcher.matches()) {
            float value = Float.valueOf(matcher.group(1)).floatValue();
            String unit = matcher.group(3).toLowerCase();
            Integer dimensionUnit = getUnitFromString(unit);
            if (dimensionUnit == null) {
                throw new NumberFormatException();
            } else {
                return new ResourceDimension(value, dimensionUnit);
            }
        } else {
            throw new NumberFormatException();
        }
    }

    public static class ResourceDimension {
        private float value;
        private int unit;

        public ResourceDimension(float value, int unit) {
            this.value = value;
            this.unit = unit;
        }

        public float getValue() {
            return value;
        }

        public int getUnit() {
            return unit;
        }
    }

    public static boolean isValidState(int state) {
        return STATE_LOOKUP.containsValue(state);
    }

    public static boolean isValidUnitType(int units) {
        return DIMENSION_LOOKUP.containsValue(units);
    }
}
