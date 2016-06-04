package com.fmi.evelina.unimobileapp.model;

import android.graphics.Bitmap;

import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.helper.ImageHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * A Calendar News model object.
 */
public class News {

    @Expose
    @SerializedName("id")
    public int Id;

    @Expose
    @SerializedName("title")
    public String Title;

    @Expose
    @SerializedName("creation_datetime")
    public Date Date;

    @Expose
    @SerializedName("text")
    public String Text;

    @Expose
    @SerializedName("image")
    public String ImageName;

    public Bitmap Image;

    public File getImageFile() {
        if (this.Image != null) {
            try {
                Bitmap largeBitmap = ImageHelper.resizeImage(this.Image, 300, 300);
                File outputDir = ApplicationController.getFileDir(); // context being the Activity pointer
                File largeImage = File.createTempFile("News_" + this.Id + "_LargeImage", "", outputDir);
                FileOutputStream outStream = new FileOutputStream(largeImage);
                largeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

                return  largeImage;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


}
