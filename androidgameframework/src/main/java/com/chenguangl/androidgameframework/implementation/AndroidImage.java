package com.chenguangl.androidgameframework.implementation;

import android.graphics.Bitmap;

import com.chenguangl.androidgameframework.api.Graphics.ImageFormat;
import com.chenguangl.androidgameframework.api.Image;

public class AndroidImage implements Image {

    private Bitmap bitmap;
    private ImageFormat format;

    public AndroidImage(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
