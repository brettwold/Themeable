# Themeable

This library allows Android developers to programmatically set styles on View elements during runtime. Also the library aims to reduce the boilerplate code need to set styles.

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

The resulting `StyleOverride` object can then be used to dynamically set the theme whenever you need to.

```java
    Themeable.setStyle(blueTheme);
```

> Note: This library is *NOT* intended to replace the existing Android system for creating `Themes` as XML 
> definitions. It is intended that this library be used in scenarios where it is desireable to release only
> one instance of an application but still have it "Themed" dynamically. Usually this would entail the changing
> of the style via some kind of external (online) service. For example where a single application is distributed to > multiple end user clients each requiring their own branding.
> (online) service
