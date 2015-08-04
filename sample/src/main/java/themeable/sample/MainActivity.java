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

        redTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.RED)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(INDIE_FLOWER)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 36)
                .build();

        redButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, Color.WHITE)
                .setBackground(getResources().getDrawable(R.drawable.primary_button))
                .setTypeface(INDIE_FLOWER)
                .build();

        blueTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.BLUE)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(ROBOTO_BOLD)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
                .build();

        greenTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.GREEN)
                .setBackgroundColor(Color.TRANSPARENT)
                .setTypeface(ROBOTO_THIN)
                .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30)
                .build();

        greenButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, Color.GREEN)
                .setBackground(getResources().getDrawable(R.drawable.primary_button))
                .setTypeface(ROBOTO_LIGHT)
                .build();

    }


    @OnClick({R.id.button_red, R.id.button_blue, R.id.button_green})
    public void setColour(Button b) {
        switch(b.getId()) {
            case R.id.button_red:
                Themeable.applyStyles(redTheme, redButtonTheme);
                break;
            case R.id.button_blue:
                Themeable.applyStyles(blueTheme);
                Themeable.removeCustomStyles(R.style.ButtonFull);
                break;
            case R.id.button_green:
                Themeable.applyStyles(greenTheme, greenButtonTheme);
                break;
        }
    }
}
