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