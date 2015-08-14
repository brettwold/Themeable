/*
 * Copyright (C)2015 Brett Cherrington
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package themeable.internal;

import java.util.HashSet;
import java.util.Set;

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

    public void addBinding(Binding binding) {
        bindings.add(binding);
    }

    public Set<Binding> getStyleBindings() {
        return bindings;
    }
}
