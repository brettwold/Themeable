package themeable.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import themeable.BindChrome;
import themeable.BindStyle;
import themeable.StyleBinder;
import themeable.widget.ChromeOverride;
import themeable.widget.ChromedViewFactory;
import themeable.widget.ThemeableViewFactory;
import themeable.widget.ViewOverride;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by brett on 30/07/15.
 */
public class CodeGenerator {


    public static TypeSpec generateClass(BindingClass bindingClass) throws ClassNotFoundException {

        ClassName targetCls = ClassName.get(bindingClass.getPackageName(), bindingClass.getTargetClass());

        TypeSpec.Builder builder = classBuilder(bindingClass.getClassName())
                .superclass(ParameterizedTypeName.get(ClassName.get(StyleBinder.class), targetCls))
                .addModifiers(PUBLIC, FINAL);


        MethodSpec.Builder bind = methodBuilder("bind")
                        .addModifiers(PUBLIC)
                        .addParameter(targetCls, "target")
                        .addParameter(ClassName.get("android.view", "View"), "view");

        MethodSpec.Builder notifyStyle = methodBuilder("notifyStyleChange")
                        .addModifiers(PUBLIC);

        MethodSpec.Builder notifyChrome = methodBuilder("notifyChromeChange")
                .addModifiers(PUBLIC);

        ParameterizedTypeName arList = ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get(ChromeOverride.class));
        builder.addField(builder(targetCls, "target", PRIVATE).build());
        builder.addField(builder(arList, "chromeBindings", PRIVATE).build());

        bind.addStatement(addSetTarget());
        bind.addStatement(addCreateChromeBindings(), arList);

        notifyChrome.addStatement(addChromeLoopNotify());

        Set<Binding> bindings = bindingClass.getStyleBindings();
        for (Binding binding : bindings) {

            Class bindingCls = binding.getType();
            if(BindStyle.class.isAssignableFrom(bindingCls)) {
                addStyleBinding(builder, bind, notifyStyle, (Binding<BindStyle>)binding);
            } else if(BindChrome.class.isAssignableFrom(bindingCls)) {
                addChromeBinding(builder, bind, (Binding<BindChrome>)binding);
            }
        }

        builder.addMethod(bind.build());
        builder.addMethod(notifyStyle.build());
        builder.addMethod(notifyChrome.build());

        return builder.build();
    }

    private static void addChromeBinding(TypeSpec.Builder builder, MethodSpec.Builder bind, Binding<BindChrome> binding) {
        bind.addStatement(addChrome(binding), ClassName.get(ChromedViewFactory.class));
    }

    private static void addStyleBinding(TypeSpec.Builder builder, MethodSpec.Builder bind, MethodSpec.Builder notify, Binding<BindStyle> binding) {
        int[] resIds = binding.getAnnotation().resourceIds();
        if (resIds != null && resIds.length > 0) {
            builder.addField(builder(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(ViewOverride.class)),
                    binding.getElement().getSimpleName() + "ViewOverride", PRIVATE).build());

            bind.addStatement(binding.getElement().getSimpleName() + "ViewOverride = new $T<>()", ClassName.get("java.util", "ArrayList"));

            for (int resId : resIds) {
                bind.addStatement(addFindView(binding, resId));
                bind.addStatement(addAddViewOverride(binding, resId), ClassName.get(ThemeableViewFactory.class));
            }
            notify.addStatement(addLoopNotify(binding));
        } else {
            builder.addField(builder(ClassName.get(ViewOverride.class), binding.getElement().getSimpleName() + "ViewOverride", PRIVATE).build());

            bind.addStatement(addSetupViewOverride(binding), ClassName.get(ThemeableViewFactory.class));
            notify.addStatement(addNotify(binding));
        }
    }

    private static String addAddViewOverride(Binding<BindStyle> binding, int resid) {
        BindStyle annotation = binding.getAnnotation();
        Element element = binding.getElement();

        int value = annotation.value();

        StringBuilder statement = new StringBuilder();
        statement
                .append(element.getSimpleName())
                .append("ViewOverride.add(")
                .append("$T.getViewOverride(")
                .append(element.getSimpleName())
                .append(resid)
                .append(",")
                .append(value)
                .append("))");
        return statement.toString();
    }

    private static String addFindView(Binding<BindStyle> binding, int resid) {
        Element element = binding.getElement();
        TypeMirror elementType = element.asType();
        DeclaredType declaredType = (DeclaredType) elementType;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        TypeMirror viewType = typeArguments.get(0);

        StringBuilder statement = new StringBuilder();
        statement.append(viewType.toString())
                .append(" ")
                .append(element.getSimpleName())
                .append(resid)
                .append(" = (").append(viewType).append(")view.findViewById(")
                .append(resid)
                .append(")");

        return statement.toString();
    }

    private static String addSetupViewOverride(Binding<BindStyle> binding) {

        BindStyle annotation = binding.getAnnotation();
        Element element = binding.getElement();

        int value = annotation.value();

        StringBuilder statement = new StringBuilder();
        statement.append(element.getSimpleName())
                .append("ViewOverride = ")
                .append("$T.getViewOverride(target.")
                .append(element.getSimpleName())
                .append(",")
                .append(value)
                .append(")");
        return statement.toString();
    }

    private static String addSetResourceId(Binding<BindStyle> binding) {
        BindStyle annotation = binding.getAnnotation();

        int styleResId = annotation.value();

        StringBuilder statement = new StringBuilder();
        statement.append("styleResId = ").append(styleResId);
        return statement.toString();
    }

    private static String addSetTarget() {
        StringBuilder statement = new StringBuilder();
        statement.append("this.target = target");
        return statement.toString();
    }

    private static String addCreateChromeBindings() {
        StringBuilder statement = new StringBuilder();
        statement.append("this.chromeBindings = new $T();");
        return statement.toString();
    }

    private static String addNotify(Binding<BindStyle> binding) {

        Element element = binding.getElement();

        StringBuilder statement = new StringBuilder();
        statement.append(element.getSimpleName())
                .append("ViewOverride.overrideAppearance()");
        return statement.toString();
    }

    private static String addLoopNotify(Binding<BindStyle> binding) {
        Element element = binding.getElement();

        StringBuilder statement = new StringBuilder();
        statement.append("for(ViewOverride vo : ")
                .append(element.getSimpleName())
                .append("ViewOverride) { vo.overrideAppearance(); }");

        return statement.toString();
    }

    private static String addChromeLoopNotify() {
        StringBuilder statement = new StringBuilder();
        statement.append("for(ChromeOverride co : chromeBindings )")
                .append("{ co.overrideAppearance(); }");

        return statement.toString();
    }

    private static String addChrome(Binding<BindChrome> binding) {
        Element element = binding.getElement();

        StringBuilder statement = new StringBuilder();
        statement.append("chromeBindings.add($T.getChromeOverride(target.")
                .append(element.getSimpleName())
                .append("))");

        return statement.toString();
    }

}
