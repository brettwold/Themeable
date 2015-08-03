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
