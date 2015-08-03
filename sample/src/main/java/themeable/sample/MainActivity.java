package themeable.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import themeable.BindStyle;
import themeable.Themeable;
import themeable.res.StyleBuilder;
import themeable.res.StyleOverride;

/**
 * Created by brett on 03/08/15.
 */
public class MainActivity extends AppCompatActivity {

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

    StyleOverride redTheme;
    StyleOverride greenTheme;
    StyleOverride blueTheme;
    StyleOverride greenButtonTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Themeable.bind(this, getWindow().getDecorView());

        redTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.RED)
                .setBackgroundColor(Color.YELLOW)
                .build();

        blueTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.BLUE)
                .setBackgroundColor(Color.TRANSPARENT)
                .build();

        greenTheme = new StyleBuilder(this, R.style.Title)
                .setTextColor(null, Color.GREEN)
                .setBackgroundColor(Color.TRANSPARENT)
                .build();

        greenButtonTheme = new StyleBuilder(this, R.style.ButtonFull)
                .setTextColor(null, Color.GREEN)
                .setBackgroundColor(Color.MAGENTA)
                .build();

    }


    @OnClick({R.id.button_red, R.id.button_blue, R.id.button_green})
    public void setColour(Button b) {
        switch(b.getId()) {
            case R.id.button_red:
                Themeable.setStyle(redTheme);
                break;
            case R.id.button_blue:
                Themeable.setStyle(blueTheme);
                break;
            case R.id.button_green:
                Themeable.setStyle(greenTheme);
                Themeable.setStyle(greenButtonTheme);
                break;
        }
    }
}
