package themeable.themeable;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.ActivityController;

import themeable.MaterialPalette;
import themeable.Themeable;
import themeable.ThemeableFonts;
import themeable.sample.MainActivity;
import themeable.sample.R;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Config(sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class ActivityTest {

    @Before
    public void before() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void can_start_sample_activity() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();
        assertNotNull(activity);

        controller.destroy();
    }

    @Test
    public void can_set_theme() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();

        Themeable.removeTheme();

        MaterialPalette palette = Themeable.getCurrentPalette();
        assertNull(palette);
        Themeable.Theme theme = Themeable.getCurrentTheme();
        assertNull(theme);

        Button button = (Button)activity.findViewById(R.id.button_blue);
        button.performClick();

        palette = Themeable.getCurrentPalette();
        assertNotNull(palette);
        theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyBlueTheme"));

        controller.destroy();
    }

    @Test
    public void can_clear_theme() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();

        Button button = (Button)activity.findViewById(R.id.button_red);
        button.performClick();
        MaterialPalette palette = Themeable.getCurrentPalette();
        assertNotNull(palette);
        Themeable.Theme theme = Themeable.getCurrentTheme();
        assertNotNull(theme);

        button = (Button)activity.findViewById(R.id.button_restore);
        button.performClick();

        palette = Themeable.getCurrentPalette();
        assertNull(palette);
        theme = Themeable.getCurrentTheme();
        assertNull(theme);

        controller.destroy();
    }

    @Test
    public void theme_is_retained_during_pause_resume() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();

        Button button = (Button)activity.findViewById(R.id.button_red);
        button.performClick();
        MaterialPalette palette = Themeable.getCurrentPalette();
        assertNotNull(palette);
        Themeable.Theme theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyRedTheme"));

        controller.pause().resume();
        palette = Themeable.getCurrentPalette();
        assertNotNull(palette);
        theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyRedTheme"));
    }

    @Test
    public void can_set_all_themes() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();

        Button red = (Button)activity.findViewById(R.id.button_red);
        red.performClick();
        Themeable.Theme theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyRedTheme"));

        Button green = (Button)activity.findViewById(R.id.button_green);
        green.performClick();
        theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyGreenTheme"));

        Button blue = (Button)activity.findViewById(R.id.button_blue);
        blue.performClick();
        theme = Themeable.getCurrentTheme();
        assertNotNull(theme);
        assertThat(theme.getThemeName(), is("MyBlueTheme"));

        controller.destroy();
    }

    @Test
    public void fonts_are_setup() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = controller.get();

        assertTrue(ThemeableFonts.hasFont("RobotoLight"));
        assertTrue(ThemeableFonts.hasFont("RobotoThin"));
        assertTrue(ThemeableFonts.hasFont("RobotoBold"));
        assertTrue(ThemeableFonts.hasFont("IndieFlower"));

        controller.destroy();
    }

}