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

package themeable.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import themeable.Themeable;
import themeable.res.ImageOverride;

/**
 * Created by brett on 06/08/15.
 */
public class ImageCache {

    private static final String TAG = ImageCache.class.getSimpleName();
    private static final String DOWNLOADS_FOLDER = Environment.getExternalStorageDirectory() + "/Downloads/ThemeableImages";

    private static Map<String, Set<ImageView>> imageViewKeyMap = new HashMap<>();

    public static void applyImages(Themeable.Theme theme) {

        Set<ImageOverride> imageOverrides = theme.getImageOverrides();
        if(imageOverrides != null) {
            for (final ImageOverride imageOverride: imageOverrides) {
                String key = imageOverride.getKey();
                if (ImageCache.hasKey(key)) {
                    File imageFile = getImageFile(key, theme);
                    if (imageFile.exists()) {
                        setImageViews(imageOverride, imageFile);
                    } else {
                        AsyncFileDownloader downloader = new AsyncFileDownloader();
                        String url = imageOverride.getUrl();
                        if (url != null) {
                            downloader.downloadFile(url, imageFile.getAbsolutePath(), new AsyncFileDownloader.Callback() {
                                @Override
                                public void onCompleted(final File downloadedFile) {
                                    try {
                                        setImageViews(imageOverride, downloadedFile);
                                    } catch (Exception e) {
                                        Log.e(TAG, "Failed to set replacement image", e);
                                    }
                                }

                                @Override
                                public void onError(Exception e, int responseCode) {
                                    Log.e(TAG, "Failed to download replacement image file", e);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public static void restore(Themeable.Theme theme) {
        Set<ImageOverride> imageOverrides = theme.getImageOverrides();
        if(imageOverrides != null) {
            for (final ImageOverride imageOverride : imageOverrides) {
                final String key = imageOverride.getKey();
                if (imageOverride.hasRestoreId()) {
                    Set<ImageView> views = imageViewKeyMap.get(key);
                    for (final ImageView view : views) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageResource(imageOverride.getRestoreId());
                            }
                        });
                    }
                }
            }
        }
    }

    public static void clear() {
        File imagesDir = new File(DOWNLOADS_FOLDER);
        deleteContents(imagesDir);
    }

    private static File getImageFile(String key, Themeable.Theme theme) {
        File imagesDir = new File(DOWNLOADS_FOLDER, theme.getThemeName());
        if(!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        return new File(imagesDir, key);
    }

    private static boolean deleteContents(File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    Log.w(TAG, "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }

    @Nullable
    private static void setImageViews(ImageOverride imageOverride, File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            String key = imageOverride.getKey();
            int maxWidth = imageOverride.getMaxWidthPixels();
            int maxHeight = imageOverride.getMaxHeightPixels();
            Bitmap bitmap = decodeSampledBitmapFromResource(imageFile.getAbsolutePath(), maxWidth, maxHeight);

            int height = imageOverride.getHeightPixels();
            int width = imageOverride.getWidthPixels();
            setImageBitmap(key, bitmap, width, height);
        }
    }

    private static void setImageBitmap(String key, final Bitmap bitmap, final int width, final int height) {
        Set<ImageView> views = getImageViews(key);
        if(views != null) {
            for(final ImageView view : views) {
                view.post(new Runnable() {
                    public void run() {
                        view.setImageBitmap(bitmap);
                        if(width > 0 && height > 0) {
                            view.getLayoutParams().height = height;
                            view.getLayoutParams().width = width;
                            view.requestLayout();
                        }
                    }
                });
            }
        }
    }

    public static boolean hasKey(String key) {
        return imageViewKeyMap.containsKey(key);
    }

    public static Set<ImageView> getImageViews(String key) {
        return imageViewKeyMap.get(key);
    }

    public static void bind(ImageView imageView, String key) {
        Set<ImageView> images = imageViewKeyMap.get(key);
        if(images == null) {
            images = new HashSet<>();
        }
        images.add(imageView);
        imageViewKeyMap.put(key, images);
    }

    public static void unbind(ImageView imageView, String key) {
        Set<ImageView> images = imageViewKeyMap.get(key);
        if(images != null) {
            images.remove(imageView);
            if(images.size() == 0) {
                imageViewKeyMap.remove(key);
                return;
            }
            imageViewKeyMap.put(key, images);
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
