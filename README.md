# Themeable

This library allows Android developers to programmatically set styles on View elements during runtime. Also the library aims to reduce the boilerplate code need to set styles.

* Bind dynamic styles to view objects using annotations
* Boilerplate code is generated for you
* Group views into Lists set styles on all of them at once

> Currently this library is in development and subject to change without notice. Consider it in raw Alpha form and 
> *not* recommended for production use *at all*.

## Binding a style

Styles are bound to elements in your Activity/Fragment/Dialog using the `@BindStyle` annotation. The annotation can be used to directly bind a style to a single View element or to bind a style to an array of view elements given by there Android resource ids.

### Binding a single view element

Below the object `title` is bound to the style 'Title' defined in your styles file. This allows you to start from a known style and dynamically alter it at a later time.

```java
    @BindStyle(R.style.Title)
    @InjectView(R.id.title_text)
    TextView title;

```

> Note: The snippet above assumes you're also using ButterKnife to bind your view objects. The view objects must be 
> exist and therefore be bound (injected) before the main `Themeable.bind()` method is called. If you don't use
> Butterknife thenyou can of course do this manually using the standard `findViewById` methods.

### Binding a list of view elements

Below the object list `buttons` is populated with the given view elements which are then all bound to the style `ButtonFull`.

```java
    @BindStyle(value = R.style.ButtonFull, 
            resourceIds = { R.id.button_next, R.id.button_previous, R.id.button_cancel })
    List<Button> buttons;
```

The view elements must all be of the same Android `View` type. With this method there is no need to pre-populate the elements with the actual `View` objects as the library takes care of this for you.

## Binding

To bind the objects defined in your Activity/Fragment/Dialog you must call the bind method as shown below in an example for an `Activity`. Usually this call should take place in the create method of your lifecycle object and only needs to be called once.

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Themeable.bind(this, getWindow().getDecorView());
    }
```

## Dynamically changing styles

To change a style you create an instance of a `StyleBuilder` and use it to define your new style programmatically as in the example below.

```java
    StyleOverride blueTheme = new StyleBuilder(this, R.style.Title)
            .setTextColor(null, Color.BLUE)
            .setBackgroundColor(Color.TRANSPARENT)
            .build();
```

The resulting `StyleOverride` object can then be used to dynamically apply the theme whenever you need to.

```java
    Themeable.applyStyles(blueTheme);
```

Several styles can be passed to this method at once to setup more than one style resource at a time.

If you wish to restore the original style at any point then you can call `removeStyles` passing in a list of style ids you wish to restore.

```java
    Themeable.removeStyles(R.style.Title)
```

## Fonts and typefaces

Fonts can be registered with library and then used to style view elements within your application. The font files must exist within your application and be present in the Android assets folder. The fonts can then be resgistered with Themeable by calling `ThemeableFonts.registerTypeface` method. Each font registered should be given a unique name.

```java
    ThemeableFonts.registerTypeface("ROBOTO_BOLD", "fonts/Roboto-Bold.ttf");
```
In the example above the `Roboto-Bold.ttf` file has been placed in the assets folder under a sub folder called _fonts_.

Once the font has been registered it can be used in the style overriding, for example.

```java

    blueTheme = new StyleBuilder(this, R.style.Title)
            .setTextColor(null, Color.BLUE)
            .setBackgroundColor(Color.TRANSPARENT)
            .setTypeface("ROBOTO_BOLD")
            .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
            .build();
            
```

## Binding an entire Theme

Themable includes the ability for you to create an entire set of styles in one go using the `Themeable.Theme` object. The Theme should be created along with a `MaterialPalette` object which sets the main colours for the applications chrome. The `MaterialPalette` is created by simply defining 8 colours which will be used as the overall colours within your application. 

Below is an example of how your a theme can be constructed.

```java

    MaterialPalette bluePalette = MaterialPalette.build("#ff2196f3", "#ff1976D2", "#ffBBDEFB",
            "#ffFF4081", "#ff212121", "#ff727272", "#ffffffff", "#fff1f1ff");

    Themeable.Theme blueTheme = Themeable.Theme.newInstance("MyBlueTheme")
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
            .addStateColour(new int[]{android.R.attr.state_pressed}, bluePalette.getPrimaryDarkColor()))
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
```

As you can see the `MaterialPalette` object is used during the theme build as well to ensure the colours used are consistent. This theme can then be set at any time by calling

```java
    Themeable.applyTheme(blueTheme);
```

Setting a `MaterialPalette` on the theme also adds the ability for View elements to be set to the default application 'Chrome'. This means they will adopt the colours from the `MaterialPalette` object according to there type. For instance the following will ensure that the toolbar background is set to the 'primary' colour whereas the LinearLayout will have it's background set to the 'windowBackground' colour.

```java

    @BindChrome(R.id.toolbar)
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    
    @BindChrome(R.id.main_content)
    @InjectView(R.id.main_content)
    LinearLayout mainContent;

```   

## Loading theme from JSON

The entire theme can also be specified as a JSON file and loading either from your applications filesystem or from an online source.

```java

try {
    String rjson = string(Okio.buffer(Okio.source(getAssets().open("redtheme.json"))));
    redTheme = ThemeableParser.fromJSON(this, rjson);
} catch (IOException e) {
    Log.e(TAG, "Failed to read theme file", e);
} catch (ThemeableParseException e) {
    Log.e(TAG, "Failed to parse theme file", e);
}

```
For examples of the JSON systax see the sample application in this project.

## Binding Images

Images can also be bound using the `@BindImage` annotation. This allows them to be dynamically swapped out by a theme if required. The replacement image must be available on a publically accessibile URL and your application must have the `android.permission.INTERNET` permission. The image will be downloaded and cached automatically by the application. When binding to an image it must be given a unique String tag so that it can be identified by Themeable for replacement.

As you can see in the code below the image must also be given height and width parameters. The max height and width parameters determine the resolution at which the image is read into a `Bitmap` the main height and width parameters determine its final display size in your application. These are required so that when dynamically swapping images of different sizes you can ensure the image is displayed at the correct size.

```java

    private static final String KEY_LOGO = "logo";

    @BindImage(KEY_LOGO)
    @InjectView(R.id.image_replace)
    ImageView imageView;

    Themeable.Theme aTheme = Themeable.Theme.newInstance("AnImageTheme")
    ...
        .addImage(new ImageBuilder(this, KEY_LOGO)
            .setUrl("https://s3-eu-west-1.amazonaws.com/myimagestore/logo.png")
            .setRestoreResourceId(R.drawable.my_logo)
            .setMaxHeight(TypedValue.COMPLEX_UNIT_DP, 240)
            .setMaxWidth(TypedValue.COMPLEX_UNIT_DP, 240)
            .setHeight(TypedValue.COMPLEX_UNIT_SP, 120)
            .setWidth(TypedValue.COMPLEX_UNIT_SP, 120)
            .build());

```

# License

 Copyright (C)2015 Brett Cherrington

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.


# Warnings and limitations

This library is *NOT* intended to replace the existing Android system for creating `Themes` as XML 
definitions. It is intended that this library be used in scenarios where it is desireable to release only
one instance of an application but still have it "Themed" dynamically. Usually this would entail the changing
of the style via some kind of external (online) service. For example where a single application is distributed to
multiple end user clients each requiring their own branding.
