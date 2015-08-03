package themeable;

/**
 * Created by brett on 30/07/15.
 */

public @interface BindStyle {

    int value();

    int[] resourceIds() default {};
}
