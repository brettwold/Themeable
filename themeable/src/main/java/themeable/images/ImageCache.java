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
        for (final ImageOverride imageOverride: imageOverrides) {
            String key = imageOverride.getKey();
            if (ImageCache.hasKey(key)) {
                File imageFile = getImageFile(key, theme);
                if (imageFile.exists()) {
                    setImageViews(imageOverride, imageFile);
                } else {
                    AsyncFileDownloader downloader = new AsyncFileDownloader();
                    String url = imageOverride.getUrl();
                    if(url != null) {
                        downloader.downloadFile(url, imageFile.getAbsolutePath(), new AsyncFileDownloader.Callback() {
                            @Override
                            public void onCompleted(final File downloadedFile) {
                                setImageViews(imageOverride, downloadedFile);
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

    public static void restore(Themeable.Theme theme) {
        Set<ImageOverride> imageOverrides = theme.getImageOverrides();
        for (ImageOverride imageOverride: imageOverrides) {
            final String key = imageOverride.getKey();
            if(imageOverride.hasRestoreId()) {
                Set<ImageView> views = imageViewKeyMap.get(key);
                for (ImageView view : views) {
                    view.setImageResource(imageOverride.getRestoreId());
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
            setImageBitmap(key, bitmap);
        }
    }

    private static void setImageBitmap(String key, final Bitmap bitmap) {
        Set<ImageView> views = getImageViews(key);
        if(views != null) {
            for(final ImageView view : views) {
                view.post(new Runnable() {
                    public void run() {
                        view.setImageBitmap(bitmap);
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