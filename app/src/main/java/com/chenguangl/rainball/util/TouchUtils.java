package com.chenguangl.rainball.util;

import com.chenguangl.androidgameframework.api.Input.TouchEvent;

public class TouchUtils {

    public static boolean eventInBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1) {
            return true;
        } else {
            return false;
        }
    }
}
