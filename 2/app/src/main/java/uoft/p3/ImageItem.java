package uoft.p3;

/**
 * Created by wuyue on 2/3/15.
 */

import android.graphics.Bitmap;




public class ImageItem {
    private Bitmap image;
    private String title;
    private String latitude;
    private String longitude;

    // add longtidue and latitude in to this image.
    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
