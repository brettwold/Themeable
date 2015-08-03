package themeable.internal;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

import themeable.BindStyle;

/**
 * Created by brett on 30/07/15.
 */
public class BindingClass {

    private final String classPackage;
    private final String className;
    private final String targetClass;

    private Set<Binding> bindings = new HashSet<>();

    BindingClass(String classPackage, String className, String targetClass) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
    }

    public String getFqcn() {
        return classPackage + "." + className;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return classPackage;
    }

    public String getTargetClass() { return targetClass; }

    public void addStyleBinding(BindStyle annotation, Element element) {
        Binding binding = new Binding();
        binding.setAnnotation(annotation);
        binding.setElement(element);
        bindings.add(binding);
    }

    public Set<Binding> getStyleBindings() {
        return bindings;
    }
}
