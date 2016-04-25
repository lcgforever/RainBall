package com.chenguangl.rainball.screen;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Graphics.ImageFormat;
import com.chenguangl.rainball.asset.Assets;

public class SplashLoadingScreen extends Screen {

    public SplashLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Assets.splashBackground = graphics.newImage("img_splash_background.jpg", ImageFormat.ARGB8888);

        game.setScreen(new LoadingScreen(game));
    }

    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
