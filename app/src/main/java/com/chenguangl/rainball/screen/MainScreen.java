package com.chenguangl.rainball.screen;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Input.TouchEvent;
import com.chenguangl.rainball.asset.Assets;
import com.chenguangl.rainball.util.TouchUtils;

import java.util.List;

public class MainScreen extends Screen {

    private static final int MENU_LEFT_POS = 340;
    private static final int MENU_START_TOP_POS = 960;
    private static final int MENU_OPTION_TOP_POS = 1260;
    private static final int MENU_EXIT_TOP_POS = 1560;
    private static final int MENU_WIDTH = 400;
    private static final int MENU_HEIGHT = 200;

    private boolean startMenuPressed = false;
    private boolean optionMenuPressed = false;
    private boolean exitMenuPressed = false;
    private boolean shouldExit = false;

    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        if (shouldExit) {
            quit();
            return;
        }

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        for (int i = 0; i < size; i++) {
            TouchEvent event = touchEvents.get(i);
            if (TouchUtils.eventInBounds(event, MENU_LEFT_POS, MENU_START_TOP_POS, MENU_WIDTH, MENU_HEIGHT)) {
                // Event in start menu bound
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                        startMenuPressed = true;
                        break;

                    case TouchEvent.TOUCH_UP:
                        startMenuPressed = false;
                        game.setScreen(new GameScreen(game));
                        break;

                    case TouchEvent.TOUCH_DRAGGED:
                    default:
                        break;
                }
            } else if (TouchUtils.eventInBounds(event, MENU_LEFT_POS, MENU_OPTION_TOP_POS, MENU_WIDTH, MENU_HEIGHT)) {
                // Event in option menu bound
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                        optionMenuPressed = true;
                        break;

                    case TouchEvent.TOUCH_UP:
                        optionMenuPressed = false;
                        // TODO: show option menu screen
                        break;

                    case TouchEvent.TOUCH_DRAGGED:
                    default:
                        break;
                }
            } else if (TouchUtils.eventInBounds(event, MENU_LEFT_POS, MENU_EXIT_TOP_POS, MENU_WIDTH, MENU_HEIGHT)) {
                // Event in exit menu bound
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                        exitMenuPressed = true;
                        break;

                    case TouchEvent.TOUCH_UP:
                        exitMenuPressed = false;
                        shouldExit = true;
                        break;

                    case TouchEvent.TOUCH_DRAGGED:
                    default:
                        break;
                }
            } else {
                // Event not in any bound
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                    case TouchEvent.TOUCH_UP:
                    case TouchEvent.TOUCH_DRAGGED:
                    default:
                        startMenuPressed = false;
                        optionMenuPressed = false;
                        exitMenuPressed = false;
                        shouldExit = false;
                        break;
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        if (shouldExit) {
            return;
        }

        graphics.drawImage(Assets.mainBackground, 0, 0);
        graphics.drawImage(startMenuPressed ? Assets.startMenuPressed : Assets.startMenu,
                MENU_LEFT_POS, MENU_START_TOP_POS);
        graphics.drawImage(optionMenuPressed ? Assets.optionMenuPressed : Assets.optionMenu,
                MENU_LEFT_POS, MENU_OPTION_TOP_POS);
        graphics.drawImage(exitMenuPressed ? Assets.exitMenuPressed : Assets.exitMenu,
                MENU_LEFT_POS, MENU_EXIT_TOP_POS);
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
        shouldExit = true;
    }

    private void quit() {
        Assets.mainBackground.dispose();
        Assets.gameBackground.dispose();
        Assets.tileBackground.dispose();
        Assets.startMenu.dispose();
        Assets.optionMenu.dispose();
        Assets.exitMenu.dispose();
        Assets.resumeMenu.dispose();
        Assets.quitMenu.dispose();
        Assets.startMenuPressed.dispose();
        Assets.optionMenuPressed.dispose();
        Assets.exitMenuPressed.dispose();
        Assets.resumeMenuPressed.dispose();
        Assets.quitMenuPressed.dispose();
        Assets.tapToStartText.dispose();
        Assets.gameOverText.dispose();
        Assets.redBall.dispose();
        Assets.greenBall.dispose();
        Assets.blueBall.dispose();
        Assets.yellowBall.dispose();
        Assets.purpleBall.dispose();
        Assets.greyBall.dispose();
        Assets.redBallLarge.dispose();
        Assets.greenBallLarge.dispose();
        Assets.blueBallLarge.dispose();
        Assets.yellowBallLarge.dispose();
        Assets.purpleBallLarge.dispose();
        Assets.greyBallLarge.dispose();
        game.quit();
    }
}
