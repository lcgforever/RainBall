package com.chenguangl.rainball;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.implementation.AndroidGame;
import com.chenguangl.rainball.screen.SplashLoadingScreen;

public class MainGame extends AndroidGame {

    @Override
    public Screen getInitScreen() {
        return new SplashLoadingScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }
}
