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

package themeable.widget;

import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by brett on 05/08/15.
 */
public class ChromedViewFactory {

    public static ChromeOverride getChromeOverride(View target) {
        Class cls = target.getClass();
        if (Toolbar.class.isAssignableFrom(cls)) {
            return new ChromedToolbar((Toolbar)target);
        }

        return new ChromedView(target);
    }
}
