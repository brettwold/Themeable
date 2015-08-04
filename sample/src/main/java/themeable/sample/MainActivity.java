package themeable.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import themeable.BindStyle;
import themeable.Themeable;
import themeable.ThemeableFonts;
import themeable.res.StateListColourDrawableBuilder;
import themeable.res.StyleBuilder;
import themeable.res.StyleOverride;

/**
 * Created by brett on 03/08/15.
 */
public class MainActivity extends AppCompatActivity {

    private static final String ROBOTO_BOLD = "RobotoBold";
    private static final String ROBOTO_LIGHT = "RobotoLight";
    private static final String ROBOTO_THIN = "RobotoThin";
    private static final String INDIE_FLOWER = "IndieFlower";

    @BindStyle(R.style.Title)
    @InjectView(R.id.title_text)
    TextView title;

    @BindStyle(R.style.PlainText)
    @InjectView(R.id.another_text_view)
    TextView anotherTextView;

    @BindStyle(value = R.style.ButtonFull, resourceIds = { R.id.button_red, R.id.button_blue, R.id.button_green })
    List<Button> buttons;

    @InjectView(R.id.button_red)
    Button buttonRed;

    StyleOverride redTheme, redButtonTheme;
    StyleOverride greenTheme,  greenButtonTheme;
    StyleOverride blueTheme, blueButtonTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Themeable.bind(this, getWindow().getDecorView());

        ThemeableFonts.registerTypeface(ROBOTO_BOLD, "fonts/Roboto-Bold.ttf");
        ThemeableFonts.registerTypeface(ROBOTO_THIN, "fonts/Roboto-Thin.ttf");
        ThemeableFonts.registerTypeface(ROBOTO_LIGHT, "fonts/Roboto-Light.ttf");
        ThemeableFonts.registerTypeface(INDIE_FLOWER, "fonts/IndieFlower.ttf");

        int red = Color.parseColor("#ffff3217");
        int redPress = Color.parseColor("#ffe2856e");

        int green = Color.parseColor("#ff6fe153");
        int greenPress = Color.parseColor("#ff5c6784");
        int greenButton = Color.parseColor("#ff157f1f");
        int greenButtonText = Color.parseColor("#ff1d263b");

        int blue = Color.parseColor("#ff62b0e0");
        int bluePress = Color.parseColor("#fff4f1bb");
        int blueButton = Color.parseColor("#ff9bc1bc");
        int blueButtonText = Color.parseColor("#ffe6ebe0");

        redTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, red)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(INDIE_FLOWER)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 48)
                .setTextAllCaps(false)
                .build();

        redButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, Color.WHITE)
                .setBackground(new StateListColourDrawableBuilder(red)
                        .addStateColour(new int[]{android.R.attr.state_pressed}, redPress))
                .setTypeface(INDIE_FLOWER)
                .build();

        blueTheme = new StyleBuilder(this, R.style.Title)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(ROBOTO_BOLD)
                .setTextColor(null, blue)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
                .setTextAllCaps(true)
                .build();

        blueButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, blueButtonText)
                .setBackground(new StateListColourDrawableBuilder(blueButton)
                        .addStateColour(new int[]{android.R.attr.state_pressed}, bluePress))
                .setTypeface(ROBOTO_BOLD)
                .setTextAllCaps(true)
                .build();

        greenTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, green)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(ROBOTO_THIN)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30)
                .setTextAllCaps(false)
                .build();

        greenButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, greenButtonText)
                .setBackground(new StateListColourDrawableBuilder(greenButton)
                        .addStateColour(new int[]{android.R.attr.state_pressed}, greenPress))
                .setTypeface(ROBOTO_LIGHT)
                .build();

    }


    @OnClick({R.id.button_red, R.id.button_blue, R.id.button_green, R.id.button_restore})
    public void setColour(Button b) {
        switch(b.getId()) {
            case R.id.button_red:
                Themeable.applyStyles(redTheme, redButtonTheme);
                break;
            case R.id.button_blue:
                Themeable.applyStyles(blueTheme, blueButtonTheme);
                break;
            case R.id.button_green:
                Themeable.applyStyles(greenTheme, greenButtonTheme);
                break;
            case R.id.button_restore:
                Themeable.removeStyles(R.style.ButtonFull, R.style.Title);
                break;
        }
    }
}
