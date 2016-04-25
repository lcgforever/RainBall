package com.chenguangl.rainball.screen;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Input.TouchEvent;
import com.chenguangl.rainball.asset.Assets;
import com.chenguangl.rainball.model.GameModel;
import com.chenguangl.rainball.util.TouchUtils;

import java.util.List;

public class GameScreen extends Screen {

    private static final int TEXT_TAP_TO_START_LEFT_POS = 60;
    private static final int TEXT_TAP_TO_START_TOP_POS = 835;
    private static final int TEXT_GAME_OVER_LEFT_POS = 60;
    private static final int TEXT_GAME_OVER_TOP_POS = 835;
    private static final int MENU_RESUME_LEFT_POS = 290;
    private static final int MENU_RESUME_TOP_POS = 560;
    private static final int MENU_RESUME_WIDTH = 500;
    private static final int MENU_RESUME_HEIGHT = 200;
    private static final int MENU_QUIT_LEFT_POS = 340;
    private static final int MENU_QUIT_TOP_POS = 1160;
    private static final int MENU_QUIT_WIDTH = 400;
    private static final int MENU_QUIT_HEIGHT = 200;
    private static final int TILE_LEFT_POS = 60;
    private static final int TILE_TOP_POS = 200;
    private static final int TILE_WIDTH = 80;
    private static final int TILE_HEIGHT = 80;

    private enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState gameState = GameState.Ready;
//    private Paint paint, paint2;

    private boolean resumeMenuPressed = false;
    private boolean quitMenuPressed = false;

    public GameScreen(Game game) {
        super(game);

//        // Defining a paint object
//        paint = new Paint();
//        paint.setTextSize(30);
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.WHITE);
//
//        paint2 = new Paint();
//        paint2.setTextSize(100);
//        paint2.setTextAlign(Paint.Align.CENTER);
//        paint2.setAntiAlias(true);
//        paint2.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // Depending on the state of the game, we call different update methods.
        switch (gameState) {
            case Ready:
                updateReady(touchEvents);
                break;

            case Running:
                updateRunning(touchEvents, deltaTime);
                break;

            case Paused:
                updatePaused(touchEvents);
                break;

            case GameOver:
            default:
                updateGameOver(touchEvents);
                break;
        }
    }

    private void updateReady(List touchEvents) {
        // This game starts with a "Tap To Start" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!
        if (touchEvents.size() > 0) {
            gameState = GameState.Running;
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int size = touchEvents.size();
        for (int i = 0; i < size; i++) {
            TouchEvent event = touchEvents.get(i);
            if (TouchUtils.eventInBounds(event, MENU_RESUME_LEFT_POS, MENU_RESUME_TOP_POS, MENU_RESUME_WIDTH, MENU_RESUME_HEIGHT)) {
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                        resumeMenuPressed = true;
                        break;

                    case TouchEvent.TOUCH_UP:
                        resumeMenuPressed = false;
                        // Resume game
                        gameState = GameState.Running;
                        break;

                    case TouchEvent.TOUCH_DRAGGED:
                    default:
                        break;
                }
            } else if (TouchUtils.eventInBounds(event, MENU_QUIT_LEFT_POS, MENU_QUIT_TOP_POS, MENU_QUIT_WIDTH, MENU_QUIT_HEIGHT)) {
                switch (event.type) {
                    case TouchEvent.TOUCH_DOWN:
                    case TouchEvent.TOUCH_HOLD:
                        quitMenuPressed = true;
                        break;

                    case TouchEvent.TOUCH_UP:
                        quitMenuPressed = false;
                        // Quit game
                        dispose();
                        game.setScreen(new MainScreen(game));
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
                        resumeMenuPressed = false;
                        quitMenuPressed = false;
                        break;
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        // This game ends with a "Game Over" screen.
        // When the user touches the screen, go back to main screen.
        if (touchEvents.size() > 0) {
            dispose();
            game.setScreen(new MainScreen(game));
        }
    }

    @Override
    public void paint(float deltaTime) {
        graphics.drawImage(Assets.gameBackground, 0, 0);
        for (int i = 0; i < GameModel.NUM_ROW; i++) {
            for (int j = 0; j < GameModel.NUM_COLUMN; j++) {
                graphics.drawImage(Assets.tileBackground, TILE_LEFT_POS + j * TILE_WIDTH, TILE_TOP_POS + i * TILE_HEIGHT);
            }
        }

        // TODO: draw score text

        // Secondly, draw the UI above the game elements.
        switch (gameState) {
            case Ready:
                drawReadyUI();
                break;

            case Running:
                drawRunningUI();
                break;

            case Paused:
                drawPausedUI();
                break;

            case GameOver:
            default:
                drawGameOverUI();
                break;
        }
    }

    private void drawReadyUI() {
        graphics.drawImage(Assets.tapToStartText, TEXT_TAP_TO_START_LEFT_POS, TEXT_TAP_TO_START_TOP_POS);
    }

    private void drawRunningUI() {
        // TODO: draw any running related UI
    }

    private void drawPausedUI() {
        graphics.drawImage(resumeMenuPressed ? Assets.resumeMenuPressed : Assets.resumeMenu,
                MENU_RESUME_LEFT_POS, MENU_RESUME_TOP_POS);
        graphics.drawImage(quitMenuPressed ? Assets.quitMenuPressed : Assets.quitMenu,
                MENU_QUIT_LEFT_POS, MENU_QUIT_TOP_POS);
    }

    private void drawGameOverUI() {
        graphics.drawImage(Assets.gameOverText, TEXT_GAME_OVER_LEFT_POS, TEXT_GAME_OVER_TOP_POS);
    }

    @Override
    public void pause() {
        if (gameState == GameState.Running) {
            gameState = GameState.Paused;
        }
    }

    @Override
    public void resume() {
        if (gameState == GameState.Paused) {
            gameState = GameState.Running;
        }
    }

    @Override
    public void dispose() {
        // TODO: dispose any resources
    }

    @Override
    public void backButton() {
        pause();
    }
}
