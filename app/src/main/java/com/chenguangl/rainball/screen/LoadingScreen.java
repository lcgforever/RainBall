package com.chenguangl.rainball.screen;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Graphics.ImageFormat;
import com.chenguangl.rainball.asset.Assets;

public class LoadingScreen extends Screen {

    private long startTime;

    public LoadingScreen(Game game) {
        super(game);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update(float deltaTime) {
        Assets.mainBackground = graphics.newImage("img_main_background.jpg", ImageFormat.ARGB8888);
        Assets.gameBackground = graphics.newImage("img_game_background.jpg", ImageFormat.ARGB8888);
        Assets.tileBackground = graphics.newImage("img_tile_background.png", ImageFormat.ARGB8888);
        Assets.startMenu = graphics.newImage("img_start_menu.png", ImageFormat.ARGB8888);
        Assets.optionMenu = graphics.newImage("img_option_menu.png", ImageFormat.ARGB8888);
        Assets.exitMenu = graphics.newImage("img_exit_menu.png", ImageFormat.ARGB8888);
        Assets.resumeMenu = graphics.newImage("img_resume_menu.png", ImageFormat.ARGB8888);
        Assets.quitMenu = graphics.newImage("img_quit_menu.png", ImageFormat.ARGB8888);
        Assets.startMenuPressed = graphics.newImage("img_start_menu_pressed.png", ImageFormat.ARGB8888);
        Assets.optionMenuPressed = graphics.newImage("img_option_menu_pressed.png", ImageFormat.ARGB8888);
        Assets.exitMenuPressed = graphics.newImage("img_exit_menu_pressed.png", ImageFormat.ARGB8888);
        Assets.resumeMenuPressed = graphics.newImage("img_resume_menu_pressed.png", ImageFormat.ARGB8888);
        Assets.quitMenuPressed = graphics.newImage("img_quit_menu_pressed.png", ImageFormat.ARGB8888);
        Assets.tapToStartText = graphics.newImage("img_tap_to_start_text.png", ImageFormat.ARGB8888);
        Assets.gameOverText = graphics.newImage("img_game_over_text.png", ImageFormat.ARGB8888);
        Assets.redBall = graphics.newImage("img_red_ball.png", ImageFormat.ARGB8888);
        Assets.greenBall = graphics.newImage("img_green_ball.png", ImageFormat.ARGB8888);
        Assets.blueBall = graphics.newImage("img_blue_ball.png", ImageFormat.ARGB8888);
        Assets.yellowBall = graphics.newImage("img_yellow_ball.png", ImageFormat.ARGB8888);
        Assets.purpleBall = graphics.newImage("img_purple_ball.png", ImageFormat.ARGB8888);
        Assets.greyBall = graphics.newImage("img_grey_ball.png", ImageFormat.ARGB8888);
        Assets.redBallLarge = graphics.newImage("img_red_ball_large.png", ImageFormat.ARGB8888);
        Assets.greenBallLarge = graphics.newImage("img_green_ball_large.png", ImageFormat.ARGB8888);
        Assets.blueBallLarge = graphics.newImage("img_blue_ball_large.png", ImageFormat.ARGB8888);
        Assets.yellowBallLarge = graphics.newImage("img_yellow_ball_large.png", ImageFormat.ARGB8888);
        Assets.purpleBallLarge = graphics.newImage("img_purple_ball_large.png", ImageFormat.ARGB8888);
        Assets.greyBallLarge = graphics.newImage("img_grey_ball_large.png", ImageFormat.ARGB8888);

        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime >= 2000) {
            game.setScreen(new MainScreen(game));
        }
    }

    @Override
    public void paint(float deltaTime) {
        graphics.drawImage(Assets.splashBackground, 0, 0);
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
