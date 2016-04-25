package com.chenguangl.androidgameframework.api;

import android.view.View.OnTouchListener;

import com.chenguangl.androidgameframework.api.Input.TouchEvent;

import java.util.List;

public interface TouchHandler extends OnTouchListener {

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}
