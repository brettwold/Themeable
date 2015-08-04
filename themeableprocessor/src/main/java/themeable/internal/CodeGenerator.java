package themeable.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import themeable.BindStyle;
import themeable.StyleBinder;
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

        MethodSpec.Builder notify = methodBuilder("notifyStyleChange")
                        .addModifiers(PUBLIC);

        builder.addField(builder(targetCls, "target", PRIVATE).build());
        bind.addStatement(addSetTarget());

        Set<Binding> bindings = bindingClass.getStyleBindings();
        System.out.println("Got bindings: " + bindings.size());
        for (Binding binding : bindings) {
            int[] resIds = binding.getAnnotation().resourceIds();
            if(resIds != null && resIds.length > 0) {
                builder.addField(builder(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(ViewOverride.class)),
                        binding.getElement().getSimpleName() + "ViewOverride", PRIVATE).build());

                bind.addStatement(binding.getElement().getSimpleName() + "ViewOverride = new $T<>()", ClassName.get("java.util", "ArrayList"));

                for(int resId : resIds) {
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

        builder.addMethod(bind.build());
        builder.addMethod(notify.build());

        return builder.build();
    }

    private static String addAddViewOverride(Binding binding, int resid) {
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

    private static String addFindView(Binding binding, int resid) {
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

    private static String addSetupViewOverride(Binding binding) {

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

    private static String addSetResourceId(Binding binding) {
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

    private static String addNotify(Binding binding) {

        Element element = binding.getElement();

        StringBuilder statement = new StringBuilder();
        statement.append(element.getSimpleName())
                .append("ViewOverride.overrideAppearance()");
        return statement.toString();
    }

    private static String addLoopNotify(Binding binding) {
        Element element = binding.getElement();

        StringBuilder statement = new StringBuilder();
        statement.append("for(ViewOverride vo : ")
                .append(element.getSimpleName())
                .append("ViewOverride) { vo.overrideAppearance(); }");

        return statement.toString();
    }
}
