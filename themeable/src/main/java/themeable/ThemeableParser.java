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

package themeable;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

import themeable.res.ImageBuilder;
import themeable.res.ImageOverride;
import themeable.res.ResourceUtils;
import themeable.res.StateListColourDrawableBuilder;
import themeable.res.StyleBuilder;
import themeable.res.StyleOverride;

/**
 * Created by brett on 07/08/15.
 */
public class ThemeableParser {

    private static final String TAG = ThemeableParser.class.getSimpleName();

    private static class MaterialPaletteDeserializer implements JsonDeserializer<MaterialPalette> {

        private static final String PRIMARY             = "primary";
        private static final String PRIMARY_DARK        = "primaryDark";
        private static final String PRIMARY_LIGHT       = "primaryLight";
        private static final String ACCENT              = "accent";
        private static final String TEXT_PRIMARY        = "textPrimary";
        private static final String TEXT_SECONDARY      = "textSecondary";
        private static final String TEXT_ICONS          = "textIcons";
        private static final String WINDOW_BACKGROUND   = "windowBackground";

        private MaterialPalette palette;

        @Override
        public MaterialPalette deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                palette = MaterialPalette.build(
                        obj.get(PRIMARY).getAsString(),
                        obj.get(PRIMARY_DARK).getAsString(),
                        obj.get(PRIMARY_LIGHT).getAsString(),
                        obj.get(ACCENT).getAsString(),
                        obj.get(TEXT_PRIMARY).getAsString(),
                        obj.get(TEXT_SECONDARY).getAsString(),
                        obj.get(TEXT_ICONS).getAsString(),
                        obj.get(WINDOW_BACKGROUND).getAsString()
                );
                return palette;
            }
            return null;
        }

        public int getPaletteColor(String name) {
            if(PRIMARY.equals(name)) {
                return palette.getPrimaryColor();
            } else if(PRIMARY_DARK.equals(name)) {
                return palette.getPrimaryDarkColor();
            } else if(PRIMARY_LIGHT.equals(name)) {
                return palette.getPrimaryLightColor();
            } else if(ACCENT.equals(name)) {
                return palette.getAccentColor();
            } else if(TEXT_PRIMARY.equals(name)) {
                return palette.getTextPrimaryColor();
            } else if(TEXT_SECONDARY.equals(name)) {
                return palette.getTextSecondaryColor();
            } else if(TEXT_ICONS.equals(name)) {
                return palette.getTextIconsColor();
            } else if(WINDOW_BACKGROUND.equals(name)) {
                return palette.getWindowBackgroundColor();
            }
            return 0;
        }
    }

    private static class StyleOverrideDeserializer implements JsonDeserializer<StyleOverride> {

        private static final String NAME = "name";
        private static final String STYLE = "style";

        private static final String PALETTE_PREFIX      = "palette.";
        private static final String COLOR_PREFIX        = "#";
        private static final String BACKGROUND_COLOR    = "backgroundColor";
        private static final String BACKGROUND          = "background";
        private static final String DEFAULT             = "default";
        private static final String TYPEFACE            = "typeface";
        private static final String TEXT_ALL_CAPS       = "textAllCaps";
        private static final String TEXT_COLOR          = "textColor";
        private static final String TEXT_SIZE           = "textSize";
        private static final String PADDING             = "padding";
        private static final String UNIT                = "unit";
        private static final String TOP                 = "top";
        private static final String LEFT                = "left";
        private static final String BOTTOM              = "bottom";
        private static final String RIGHT               = "right";

        private Context context;
        private MaterialPaletteDeserializer mpd;

        public StyleOverrideDeserializer(Context context, MaterialPaletteDeserializer mpd) {
            this.context = context;
            this.mpd = mpd;
        }

        @Override
        public StyleOverride deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext jsonContext) throws JsonParseException {
            if(json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                String resIdName = obj.get(NAME).getAsString();

                int id = context.getResources().getIdentifier(resIdName, STYLE, context.getPackageName());
                if(id > 0) {
                    StyleBuilder builder = new StyleBuilder(context, id);

                    if(obj.has(BACKGROUND_COLOR)) {
                        builder.setBackgroundColor(parseSingleColor(obj.get(BACKGROUND_COLOR).getAsString()));
                    }

                    if(obj.has(BACKGROUND)) {
                        JsonObject background = obj.get(BACKGROUND).getAsJsonObject();
                        StateListColourDrawableBuilder slcb = new StateListColourDrawableBuilder(parseSingleColor(background.get(DEFAULT).getAsString()));
                        for(Map.Entry<String, JsonElement> entry : background.entrySet()) {
                            if(!entry.getKey().equals(DEFAULT)) {
                                slcb.addStateColour(parseState(entry.getKey()), parseSingleColor(entry.getValue().getAsString()));
                            }
                        }
                        builder.setBackground(slcb);
                    }

                    if(obj.has(TYPEFACE)) {
                        builder.setTypeface(obj.get(TYPEFACE).getAsString());
                    }
                    if(obj.has(TEXT_ALL_CAPS)) {
                        builder.setTextAllCaps(obj.get(TEXT_ALL_CAPS).getAsBoolean());
                    }
                    if(obj.has(TEXT_COLOR)) {
                        JsonElement textColor = obj.get(TEXT_COLOR);
                        builder.setTextColor(null,parseSingleColor(textColor.getAsString()));
                    }
                    if(obj.has(TEXT_SIZE)) {
                        ResourceUtils.ResourceDimension dim = ResourceUtils.stringToDimension(obj.get(TEXT_SIZE).getAsString());
                        builder.setTextSize(dim.getUnit(), dim.getValue());
                    }
                    if (obj.has(PADDING)) {
                        JsonElement padding = obj.get(PADDING);
                        if(padding.isJsonObject()) {
                            JsonObject pads = padding.getAsJsonObject();
                            int unit = ResourceUtils.getUnitFromString(pads.get(UNIT).getAsString());
                            builder.setPadding(unit,
                                    pads.get(LEFT).getAsInt(),
                                    pads.get(TOP).getAsInt(),
                                    pads.get(RIGHT).getAsInt(),
                                    pads.get(BOTTOM).getAsInt()
                            );
                        }
                    }
                    return builder.build();
                }
            }
            return null;
        }

        private int[] parseState(String stateStr) {
            boolean negative = false;
            if(stateStr.startsWith("!")) {
                negative = true;
                stateStr = stateStr.substring(1);
            }
            Log.d(TAG, "Looking for state called: " + stateStr + " neg:" + negative);
            Integer state = ResourceUtils.getStateFromString(stateStr);
            if(state != null) {
                int[] states = new int[1];
                states[0] = negative ? -ResourceUtils.getStateFromString(stateStr) : ResourceUtils.getStateFromString(stateStr);
                return states;
            } else {
                Log.d(TAG, "Failed to find state called: " + stateStr);
            }
            return new int[] {};
        }

        private int parseSingleColor(String colorStr) {
            if(colorStr != null && !colorStr.isEmpty()) {
                if (colorStr.startsWith(PALETTE_PREFIX)) {
                    return mpd.getPaletteColor(colorStr.substring(PALETTE_PREFIX.length()));
                } else if (colorStr.startsWith(COLOR_PREFIX)) {
                    return Color.parseColor(colorStr);
                }
            }
            return 0;
        }
    }

    private static class ImageOverrideDeserializer implements JsonDeserializer<ImageOverride> {
        private static final String NAME = "name";
        private static final String URL = "url";
        private static final String RESTORE_DRAWABLE = "restoreDrawable";
        private static final String DRAWABLE = "drawable";
        private static final String MAX_HEIGHT = "maxHeight";
        private static final String MAX_WIDTH = "maxWidth";
        private static final String HEIGHT = "height";
        private static final String WIDTH = "width";

        private Context context;

        public ImageOverrideDeserializer(Context context) {
            this.context = context;
        }

        @Override
        public ImageOverride deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext jsonContext) throws JsonParseException {
            if(json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                String name = obj.get(NAME).getAsString();

                ImageBuilder builder = new ImageBuilder(context, name);

                String url = obj.get(URL).getAsString();
                builder.setUrl(url);

                String restore = obj.get(RESTORE_DRAWABLE).getAsString();
                int id = context.getResources().getIdentifier(restore, DRAWABLE, context.getPackageName());
                builder.setRestoreResourceId(id);

                if(obj.has(MAX_HEIGHT)) {
                    ResourceUtils.ResourceDimension maxHeight = ResourceUtils.stringToDimension(obj.get(MAX_HEIGHT).getAsString());
                    builder.setMaxHeight(maxHeight.getUnit(), (int) maxHeight.getValue());
                }

                if(obj.has(MAX_WIDTH)) {
                    ResourceUtils.ResourceDimension maxWidth = ResourceUtils.stringToDimension(obj.get(MAX_WIDTH).getAsString());
                    builder.setMaxWidth(maxWidth.getUnit(), (int) maxWidth.getValue());
                }

                if(obj.has(HEIGHT)) {
                    ResourceUtils.ResourceDimension height = ResourceUtils.stringToDimension(obj.get(HEIGHT).getAsString());
                    builder.setHeight(height.getUnit(), (int) height.getValue());
                }

                if(obj.has(WIDTH)) {
                    ResourceUtils.ResourceDimension width = ResourceUtils.stringToDimension(obj.get(WIDTH).getAsString());
                    builder.setWidth(width.getUnit(), (int) width.getValue());
                }

                ImageOverride imageOverride = builder.build();

                return imageOverride;
            }
            return null;
        }
    }

    public static Themeable.Theme fromJSON(Context context, String json) throws ThemeableParseException {

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            MaterialPaletteDeserializer mpd = new MaterialPaletteDeserializer();
            gsonBuilder.registerTypeAdapter(MaterialPalette.class, mpd);
            gsonBuilder.registerTypeAdapter(StyleOverride.class, new StyleOverrideDeserializer(context, mpd));
            gsonBuilder.registerTypeAdapter(ImageOverride.class, new ImageOverrideDeserializer(context));
            Gson gson = gsonBuilder.create();
            Themeable.Theme theme = gson.fromJson(json, Themeable.Theme.class);
            return theme;
        } catch(Exception e) {
            throw new ThemeableParseException("Unable to parse Theme JSON file", e);
        }
    }
}
