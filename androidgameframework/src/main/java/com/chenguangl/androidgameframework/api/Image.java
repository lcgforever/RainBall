package com.chenguangl.androidgameframework.api;

import android.graphics.Bitmap;

import com.chenguangl.androidgameframework.api.Graphics.ImageFormat;

public interface Image {

    int getWidth();

    int getHeight();

    Bitmap getBitmap();

    ImageFormat getFormat();

    void dispose();
}
