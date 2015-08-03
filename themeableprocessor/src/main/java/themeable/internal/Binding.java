package themeable.internal;

import javax.lang.model.element.Element;

import themeable.BindStyle;

/**
 * Created by brett on 30/07/15.
 */
public class Binding {

    private BindStyle annotation;
    private Element element;

    public BindStyle getAnnotation() {
        return annotation;
    }

    public void setAnnotation(BindStyle annotation) {
        this.annotation = annotation;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
