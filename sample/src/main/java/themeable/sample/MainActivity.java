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

package themeable.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.internal.Util;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okio.BufferedSource;
import okio.Okio;
import themeable.BindChrome;
import themeable.BindImage;
import themeable.BindStyle;
import themeable.MaterialPalette;
import themeable.ThemeableParseException;
import themeable.Themeable;
import themeable.ThemeableFonts;
import themeable.ThemeableParser;
import themeable.res.StateListColourDrawableBuilder;
import themeable.res.StyleBuilder;

/**
 * Created by brett on 03/08/15.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ROBOTO_BOLD = "RobotoBold";
    private static final String ROBOTO_LIGHT = "RobotoLight";
    private static final String ROBOTO_THIN = "RobotoThin";
    private static final String INDIE_FLOWER = "IndieFlower";

    private static final String KEY_LOGO = "logo";

    @BindStyle(R.style.Title)
    @InjectView(R.id.title_text)
    TextView title;

    @BindStyle(R.style.PlainText)
    @InjectView(R.id.another_text_view)
    TextView anotherTextView;

    @BindStyle(value = R.style.ButtonFull, resourceIds = { R.id.button_red, R.id.button_blue, R.id.button_green, R.id.button_disabled })
    List<Button> buttons;

    @InjectView(R.id.button_restore)
    @BindStyle(R.style.ButtonPrimary)
    Button buttonRestore;

    @BindChrome(R.id.toolbar)
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @BindChrome(R.id.main_content)
    @InjectView(R.id.main_content)
    LinearLayout mainContent;

    @BindImage(KEY_LOGO)
    @InjectView(R.id.image_replace)
    ImageView imageView;

    Themeable.Theme redTheme, blueTheme, greenTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        Themeable.bind(this, mainContent);

        ThemeableFonts.registerTypeface(ROBOTO_BOLD, "fonts/Roboto-Bold.ttf");
        ThemeableFonts.registerTypeface(ROBOTO_THIN, "fonts/Roboto-Thin.ttf");
        ThemeableFonts.registerTypeface(ROBOTO_LIGHT, "fonts/Roboto-Light.ttf");
        ThemeableFonts.registerTypeface(INDIE_FLOWER, "fonts/IndieFlower.ttf");

        MaterialPalette bluePalette = MaterialPalette.build("#ff2196f3", "#ff1976D2", "#ffBBDEFB",
                "#ffFF4081", "#ff212121", "#ff727272", "#ffffffff", "#fff1f1ff");

        MaterialPalette greenPalette = MaterialPalette.build("#ff4CAF50", "#ff388E3C", "#ffC8E6C9",
                "#ff795548", "#ff212121", "#ff727272", "#ffffffff", "#ffcccccc");

        try {
            String rjson = string(Okio.buffer(Okio.source(getAssets().open("redtheme.json"))));
            redTheme = ThemeableParser.fromJSON(this, rjson);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read theme file", e);
        } catch (ThemeableParseException e) {
            Log.e(TAG, "Failed to parse theme file", e);
        }

        blueTheme = Themeable.Theme.newInstance("MyBlueTheme")
                .setPalette(bluePalette)
                .addStyle(new StyleBuilder(this, R.style.Title)
                    .setBackgroundColor(Color.TRANSPARENT)
                    .setTypeface(ROBOTO_BOLD)
                    .setTextColor(null, bluePalette.getPrimaryColor())
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
                    .setTextAllCaps(true)
                    .setPadding(TypedValue.COMPLEX_UNIT_SP, 0, 100, 0, 0)
                        .build())
                .addStyle(new StyleBuilder(this, R.style.ButtonFull)
                    .setTextColor(null, bluePalette.getTextIconsColor())
                    .setBackground(new StateListColourDrawableBuilder(bluePalette.getPrimaryColor())
                            .addStateColour(new int[]{android.R.attr.state_pressed}, bluePalette.getPrimaryDarkColor())
                            .addStateColour(new int[]{-android.R.attr.state_enabled}, bluePalette.getPrimaryLightColor()))
                    .setTypeface(ROBOTO_BOLD)
                    .setTextAllCaps(true)
                        .build())
                .addStyle(new StyleBuilder(this, R.style.ButtonPrimary)
                        .setTextColor(null, bluePalette.getTextIconsColor())
                        .setBackground(new StateListColourDrawableBuilder(bluePalette.getAccentColor())
                                .addStateColour(new int[]{android.R.attr.state_pressed}, bluePalette.getPrimaryDarkColor()))
                        .setTypeface(ROBOTO_BOLD)
                        .setTextAllCaps(true)
                        .build());

        greenTheme = Themeable.Theme.newInstance("MyGreenTheme")
                .setPalette(greenPalette)
                .addStyle(new StyleBuilder(this, R.style.Title)
                    .setTextColor(null, greenPalette.getTextIconsColor())
                    .setBackgroundColor(Color.TRANSPARENT)
                    .setTypeface(ROBOTO_THIN)
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30)
                    .setTextAllCaps(false)
                    .build())
                .addStyle(new StyleBuilder(this, R.style.ButtonFull)
                    .setTextColor(null, greenPalette.getTextIconsColor())
                    .setBackground(new StateListColourDrawableBuilder(greenPalette.getPrimaryColor())
                            .addStateColour(new int[]{android.R.attr.state_pressed}, greenPalette.getPrimaryLightColor())
                            .addStateColour(new int[]{-android.R.attr.state_enabled}, greenPalette.getPrimaryLightColor()))
                    .setTypeface(ROBOTO_LIGHT)
                    .build())
                .addStyle(new StyleBuilder(this, R.style.ButtonPrimary)
                    .setTextColor(null, greenPalette.getTextIconsColor())
                    .setBackground(new StateListColourDrawableBuilder(greenPalette.getAccentColor())
                                    .addStateColour(new int[]{android.R.attr.state_pressed}, greenPalette.getPrimaryDarkColor()))
                    .setTypeface(ROBOTO_LIGHT)
                    .setTextAllCaps(true)
                    .build());

    }

    public final byte[] bytes(BufferedSource source) throws IOException {
        byte[] bytes;
        try {
            bytes = source.readByteArray();
        } finally {
            Util.closeQuietly(source);
        }
        return bytes;
    }

    public final String string(BufferedSource source) throws IOException {
        return new String(bytes(source), "UTF-8");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Themeable.unbind(this);
    }

    @OnClick({R.id.button_red, R.id.button_blue, R.id.button_green, R.id.button_restore})
    public void setColour(Button b) {
        switch(b.getId()) {
            case R.id.button_red:
                Themeable.applyTheme(redTheme);
                break;
            case R.id.button_blue:
                Themeable.applyTheme(blueTheme);
                break;
            case R.id.button_green:
                Themeable.applyTheme(greenTheme);
                break;
            case R.id.button_restore:
                Themeable.removeTheme();
                break;
        }
    }
}
