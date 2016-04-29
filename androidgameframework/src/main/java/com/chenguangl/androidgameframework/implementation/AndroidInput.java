package com.chenguangl.androidgameframework.implementation;

import android.content.Context;
import android.view.View;

import com.chenguangl.androidgameframework.api.Input;
import com.chenguangl.androidgameframework.api.TouchHandler;

import java.util.List;

public class AndroidInput implements Input {

    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
//        if (VERSION.SDK_INT < 5) {
//            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
//        } else {
//            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
//        }
        touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
