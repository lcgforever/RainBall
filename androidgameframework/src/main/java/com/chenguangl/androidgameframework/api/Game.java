package com.chenguangl.androidgameframework.api;

import com.chenguangl.androidgameframework.Screen;

public interface Game {

    Audio getAudio();

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    void setScreen(Screen screen);

    void quit();

    Screen getCurrentScreen();

    Screen getInitScreen();
}
