package themeable.internal;

import javax.lang.model.element.Element;

/**
 * Created by brett on 30/07/15.
 */
public class Binding<T> {

    private T annotation;
    private Element element;
    private Class<T> type;

    public Binding(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
