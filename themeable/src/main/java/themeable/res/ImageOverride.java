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

package themeable.res;

/**
 * Created by brett on 06/08/15.
 */
public class ImageOverride {

    private String key;
    private String url;
    private int restoreId;
    private int maxWidthPixels;
    private int maxHeightPixels;
    private int heightPixels;
    private int widthPixels;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRestoreId() {
        return restoreId;
    }

    public void setRestoreId(int restoreId) {
        this.restoreId = restoreId;
    }

    public boolean hasRestoreId() {
        return restoreId > 0;
    }

    public int getMaxWidthPixels() {
        return maxWidthPixels;
    }

    public void setMaxWidthPixels(int maxWidthPixels) {
        this.maxWidthPixels = maxWidthPixels;
    }

    public int getMaxHeightPixels() {
        return maxHeightPixels;
    }

    public void setMaxHeightPixels(int maxHeightPixels) {
        this.maxHeightPixels = maxHeightPixels;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

    public void setHeightPixels(int heightPixels) {
        this.heightPixels = heightPixels;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }
}