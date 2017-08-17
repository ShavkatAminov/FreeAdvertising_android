package ru.startandroid.freeadvertising.model;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by User on 16.05.2017.
 */

public class ImageList {
    private String imagepath;
    private ImageView imageView;
    private Button btndelete;


    public void ImageList(String imagepath, ImageView imageView, Button btndelete){
        this.imagepath = imagepath;
        this.imageView = imageView;
        this.btndelete = btndelete;
    };

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Button getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(Button btndelete) {
        this.btndelete = btndelete;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
