package com.chenguangl.androidgameframework;

import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Graphics;

public abstract class Screen {

    protected final Game game;
    protected final Graphics graphics;

    public Screen(Game game) {
        this.game = game;
        this.graphics = game.getGraphics();
    }

    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

    public abstract void backButton();
}
