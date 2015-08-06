package themeable.internal;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;

import themeable.BindChrome;
import themeable.BindImage;
import themeable.BindStyle;

import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Created by brett on 30/07/15.
 */
@AutoService(Processor.class)
public class ThemeableProcessor extends AbstractProcessor {

    static final String VIEW_TYPE = "android.view.View";

    public static final String SUFFIX = "$$StyleBinder";

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        filer = env.getFiler();
        messager = env.getMessager();
        elementUtils = env.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindStyle.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        Map<TypeElement, BindingClass> targetClassMap = findAndParseTargets(env);
        if(targetClassMap != null) {
            for (Map.Entry<TypeElement, BindingClass> entry : targetClassMap.entrySet()) {
                TypeElement typeElement = entry.getKey();
                BindingClass bindingClass = entry.getValue();

                try {
                    String packageName = bindingClass.getPackageName();
                    TypeSpec generatedClass = CodeGenerator.generateClass(bindingClass);
                    JavaFile javaFile = JavaFile.builder(packageName, generatedClass).build();
                    javaFile.writeTo(processingEnv.getFiler());

                } catch (Exception e) {
                    messager.printMessage(ERROR, String.format("Unable to write view binder for type %s: %s", typeElement, e.getMessage()));
                }
            }
        }

        return true;
    }

    private Map<TypeElement, BindingClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();

        Set<? extends Element> styleElements = env.getElementsAnnotatedWith(BindStyle.class);
        for(Element element : styleElements) {
            if (bindStyle(targetClassMap, element)) return null;
        }

        Set<? extends Element> chromeElements = env.getElementsAnnotatedWith(BindChrome.class);
        for(Element element : chromeElements) {
            if (bindChrome(targetClassMap, element)) return null;
        }

        Set<? extends Element> imageElements = env.getElementsAnnotatedWith(BindImage.class);
        for(Element element : imageElements) {
            if (bindImage(targetClassMap, element)) return null;
        }

        return targetClassMap;
    }

    private boolean bindImage(Map<TypeElement, BindingClass> targetClassMap, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        if (verifyValidViewElement(element, hasError, enclosingElement, BindImage.class.getSimpleName())) return true;

        BindImage bindingType = element.getAnnotation(BindImage.class);
        Binding<BindImage> binding = new Binding<BindImage>(BindImage.class);
        binding.setAnnotation(bindingType);
        binding.setElement(element);

        BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
        bindingClass.addBinding(binding);
        return false;
    }

    private boolean bindChrome(Map<TypeElement, BindingClass> targetClassMap, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        if (verifyValidViewElement(element, hasError, enclosingElement, BindChrome.class.getSimpleName())) return true;

        BindChrome bindingType = element.getAnnotation(BindChrome.class);
        Binding<BindChrome> binding = new Binding<BindChrome>(BindChrome.class);
        binding.setAnnotation(bindingType);
        binding.setElement(element);

        BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
        bindingClass.addBinding(binding);
        return false;
    }

    private boolean bindStyle(Map<TypeElement, BindingClass> targetClassMap, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        if (verifyValidViewElement(element, hasError, enclosingElement, BindStyle.class.getSimpleName())) return true;

        BindStyle bindingType = element.getAnnotation(BindStyle.class);
        Binding<BindStyle> binding = new Binding<BindStyle>(BindStyle.class);
        binding.setAnnotation(bindingType);
        binding.setElement(element);

        BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
        bindingClass.addBinding(binding);
        return false;
    }

    private boolean verifyValidViewElement(Element element, boolean hasError, TypeElement enclosingElement, String className) {
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) elementType;
            elementType = typeVariable.getUpperBound();
        }

        if (!isSubtypeOfType(elementType, VIEW_TYPE) && !isInterface(elementType)) {
            messager.printMessage(ERROR, String.format("@%s fields must extend from View or be an interface. (%s.%s)",
                    className, enclosingElement.getQualifiedName(), element.getSimpleName()), element);
            hasError = true;
        }

        if(hasError) {
            return true;
        }
        return false;
    }

    private BindingClass getOrCreateTargetClass(Map<TypeElement, BindingClass> targetClassMap, TypeElement enclosingElement) {
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, classPackage) + SUFFIX;

            bindingClass = new BindingClass(classPackage, className, targetType);
            targetClassMap.put(enclosingElement, bindingClass);
        }
        return bindingClass;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private boolean isInterface(TypeMirror typeMirror) {
        if (!(typeMirror instanceof DeclaredType)) {
            return false;
        }
        return ((DeclaredType) typeMirror).asElement().getKind() == INTERFACE;
    }

    private boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (otherType.equals(typeMirror.toString())) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }

}
