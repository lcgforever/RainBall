package com.chenguangl.rainball.screen;

import com.chenguangl.androidgameframework.Screen;
import com.chenguangl.androidgameframework.api.Game;
import com.chenguangl.androidgameframework.api.Image;
import com.chenguangl.androidgameframework.api.Input.TouchEvent;
import com.chenguangl.rainball.asset.Assets;
import com.chenguangl.rainball.data.Ball;
import com.chenguangl.rainball.model.GameModel;
import com.chenguangl.rainball.util.RandomUtils;
import com.chenguangl.rainball.util.TouchUtils;
import com.chenguangl.rainball.util.pathfinding.AStarPathFinder;
import com.chenguangl.rainball.util.pathfinding.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameScreen extends Screen {

    private static final int TEXT_TAP_TO_START_LEFT_POS = 60;
    private static final int TEXT_TAP_TO_START_TOP_POS = 835;
    private static final int TEXT_GAME_OVER_LEFT_POS = 60;
    private static final int TEXT_GAME_OVER_TOP_POS = 835;
    private static final int MENU_RESUME_LEFT_POS = 290;
    private static final int MENU_RESUME_TOP_POS = 660;
    private static final int MENU_RESUME_WIDTH = 500;
    private static final int MENU_RESUME_HEIGHT = 200;
    private static final int MENU_QUIT_LEFT_POS = 340;
    private static final int MENU_QUIT_TOP_POS = 1060;
    private static final int MENU_QUIT_WIDTH = 400;
    private static final int MENU_QUIT_HEIGHT = 200;
    private static final int TILE_WIDTH = 96;
    private static final int TILE_HEIGHT = 96;
    private static final int BOARD_LEFT_POS = 60;
    private static final int BOARD_TOP_POS = 320;
    private static final int BOARD_WIDTH = TILE_WIDTH * GameModel.NUM_COLUMN;
    private static final int BOARD_HEIGHT = TILE_HEIGHT * GameModel.NUM_ROW;
    private static final int BALL_POS_OFFSET = 12;
    private static final int BALL_SELECTED_POS_OFFSET = 0;
    private static final int TOTAL_BOARD_TILE_COUNT = GameModel.TOTAL_COUNT;
    private static final long UPDATE_PATH_TIME_INTERVAL = 50;
    private static final long UPDATE_BALL_TIME_INTERVAL = 80;

    private enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState gameState = GameState.Ready;

    private GameModel gameModel;
    private Map<Integer, Ball> ballMap;
    private Set<Ball> fiveInRowBallSet;

    private AStarPathFinder pathFinder;
    private Path currentPath;
    private int currentPathIndex;
    private long prevPathUpdateTime;
    private int selectedPosition;

    private int totalBallCount = 0;
    private int step = 0;
    private long prevBallUpdateTime;

    private boolean touchLocked = false;
    private boolean resumeMenuPressed = false;
    private boolean quitMenuPressed = false;

    public GameScreen(Game game) {
        super(game);

        gameModel = new GameModel();
        ballMap = gameModel.getBallMap();
        fiveInRowBallSet = new HashSet<>();
        pathFinder = new AStarPathFinder(gameModel, TOTAL_BOARD_TILE_COUNT, false);
        addNewBalls(new HashSet<Integer>(), GameModel.INITIAL_BALL_COUNT);
        selectedPosition = -1;
        currentPath = null;
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
        // First check if game is over
        if (totalBallCount == TOTAL_BOARD_TILE_COUNT) {
            gameState = GameState.GameOver;
            return;
        }

        // Touch locked means we are moving balls
        if (touchLocked) {
            long currentTime = System.currentTimeMillis();
            if (currentPath != null) {
                if (currentTime - prevPathUpdateTime >= UPDATE_PATH_TIME_INTERVAL) {
                    prevPathUpdateTime = currentTime;
                    int currentRow = currentPath.getY(currentPathIndex);
                    int currentCol = currentPath.getX(currentPathIndex);
                    int currentPosition = currentRow * GameModel.NUM_COLUMN + currentCol;
                    Ball ball = ballMap.get(currentPosition);
                    ballMap.remove(currentPosition);
                    int newRow = currentPath.getY(currentPathIndex + 1);
                    int newCol = currentPath.getX(currentPathIndex + 1);
                    int newPosition = newRow * GameModel.NUM_COLUMN + newCol;
                    ball.setRow(newRow);
                    ball.setColumn(newCol);
                    ballMap.put(newPosition, ball);
                    currentPathIndex++;
                    if (currentPathIndex == currentPath.getLength() - 1) {
                        // The ball is at its destination
                        gameModel.updateTileMapAtPosition(newRow, newCol, GameModel.STATE_BLOCKED);
                        touchLocked = false;
                        currentPath = null;
                        currentPathIndex = 0;
                        if (!checkFiveInRow()) {
                            addNewBalls(ballMap.keySet(), GameModel.GENERATE_BALL_COUNT);
                        }
                    }
                }
            } else if (!fiveInRowBallSet.isEmpty()) {
                if (currentTime - prevBallUpdateTime >= UPDATE_BALL_TIME_INTERVAL) {
                    prevBallUpdateTime = currentTime;
                    if (step < 5) {
                        for (Ball ball : fiveInRowBallSet) {
                            ball.setChanging(step % 2 == 0);
                        }
                        step++;
                    } else {
                        for (Ball ball : fiveInRowBallSet) {
                            int position = ball.getRow() * GameModel.NUM_COLUMN + ball.getColumn();
                            gameModel.updateTileMapAtPosition(ball.getRow(), ball.getColumn(), GameModel.STATE_CLEAR);
                            ballMap.remove(position);
                        }
                        fiveInRowBallSet.clear();
                        step = 0;
                        touchLocked = false;
                    }
                }
            }
        } else {
            int size = touchEvents.size();
            for (int i = 0; i < size; i++) {
                TouchEvent event = touchEvents.get(i);
                boolean touchBall = false;
                switch (event.type) {
                    case TouchEvent.TOUCH_UP:
                        if (TouchUtils.eventInBounds(event,
                                BOARD_LEFT_POS,
                                BOARD_TOP_POS,
                                BOARD_WIDTH,
                                BOARD_HEIGHT)) {
                            // In board bound
                            for (int position : ballMap.keySet()) {
                                // Touch on ball
                                int row = position / GameModel.NUM_COLUMN;
                                int col = position % GameModel.NUM_COLUMN;
                                if (TouchUtils.eventInBounds(event,
                                        BOARD_LEFT_POS + col * TILE_WIDTH,
                                        BOARD_TOP_POS + row * TILE_HEIGHT,
                                        TILE_WIDTH,
                                        TILE_HEIGHT)) {
                                    touchBall = true;
                                    if (selectedPosition == -1) {
                                        selectedPosition = position;
                                        ballMap.get(position).setSelected(true);
                                    } else {
                                        if (selectedPosition == position) {
                                            ballMap.get(selectedPosition).setSelected(false);
                                            selectedPosition = -1;
                                        } else {
                                            ballMap.get(selectedPosition).setSelected(false);
                                            ballMap.get(position).setSelected(true);
                                            selectedPosition = position;
                                        }
                                    }
                                    break;
                                }
                            }

                            // If the event is not touchBall and previous selected position is not null,
                            // player is trying to move the ball to this position
                            if (!touchBall && selectedPosition != -1) {
                                int startRow = selectedPosition / GameModel.NUM_COLUMN;
                                int startCol = selectedPosition % GameModel.NUM_COLUMN;
                                int endRow = (event.y - BOARD_TOP_POS) / TILE_HEIGHT;
                                int endCol = (event.x - BOARD_LEFT_POS) / TILE_WIDTH;
                                currentPath = pathFinder.findPath(null,
                                        startCol,
                                        startRow,
                                        endCol,
                                        endRow);
                                if (currentPath != null) {
                                    ballMap.get(selectedPosition).setSelected(false);
                                    touchLocked = true;
                                    gameModel.updateTileMapAtPosition(startRow, startCol, GameModel.STATE_CLEAR);
                                    selectedPosition = -1;
                                    currentPathIndex = 0;
                                    prevPathUpdateTime = System.currentTimeMillis();
                                }
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void addNewBalls(Set<Integer> blockedPositon, int number) {
        Set<Integer> newPositionSet = RandomUtils.generateRandomPositions(blockedPositon, number);
        for (Integer position : newPositionSet) {
            int row = position / GameModel.NUM_COLUMN;
            int col = position % GameModel.NUM_COLUMN;
            Ball ball = new Ball(row, col, RandomUtils.generateRandomColor());
            gameModel.updateTileMapAtPosition(row, col, GameModel.STATE_BLOCKED);
            ballMap.put(row * GameModel.NUM_COLUMN + col, ball);
        }
        totalBallCount += newPositionSet.size();
    }

    private boolean checkFiveInRow() {
        Set<Integer> positionSet = new HashSet<>(ballMap.keySet());
        ArrayList<Integer> list = new ArrayList<>(positionSet);
        for (Integer position : list) {
            // Check horizontal
            int row = position / GameModel.NUM_COLUMN;
            int col = position % GameModel.NUM_COLUMN;
            int color = ballMap.get(position).getColor().getValue();
            int left = col, right = col + 1;
            while (left >= 0) {
                int newPos = row * GameModel.NUM_COLUMN + left;
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    left--;
                } else {
                    break;
                }
            }
            while (right < GameModel.NUM_COLUMN) {
                int newPos = row * GameModel.NUM_COLUMN + right;
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    right++;
                } else {
                    break;
                }
            }
            left++;
            right--;
            if (right - left >= 4) {
                for (int i = left; i <= right; i++) {
                    fiveInRowBallSet.add(ballMap.get(row * GameModel.NUM_COLUMN + i));
                }
            }

            // Check vertical
            int top = row, bottom = row + 1;
            while (top >= 0) {
                int newPos = top * GameModel.NUM_COLUMN + col;
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    top--;
                } else {
                    break;
                }
            }
            while (bottom < GameModel.NUM_ROW) {
                int newPos = bottom * GameModel.NUM_COLUMN + col;
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    bottom++;
                } else {
                    break;
                }
            }
            top++;
            bottom--;
            if (bottom - top >= 4) {
                for (int i = top; i <= bottom; i++) {
                    fiveInRowBallSet.add(ballMap.get(i * GameModel.NUM_COLUMN + col));
                }
            }

            // Check diagonal top-left to bottom-right
            int topLeft = 0, bottomRight = 1;
            while (row + topLeft >= 0
                    && col + topLeft >= 0) {
                int newPos = (row + topLeft) * GameModel.NUM_COLUMN + (col + topLeft);
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    topLeft--;
                } else {
                    break;
                }
            }
            while (row + bottomRight < GameModel.NUM_ROW
                    && col + bottomRight < GameModel.NUM_COLUMN) {
                int newPos = (row + bottomRight) * GameModel.NUM_COLUMN + (col + bottomRight);
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    bottomRight++;
                } else {
                    break;
                }
            }
            topLeft++;
            bottomRight--;
            if (bottomRight - topLeft >= 4) {
                for (int i = topLeft; i <= bottomRight; i++) {
                    fiveInRowBallSet.add(ballMap.get((row + i) * GameModel.NUM_COLUMN + (col + i)));
                }
            }

            // Check diagonal bottom-left to top-right
            int bottomLeft = 0, topRight = 1;
            while (row - bottomLeft < GameModel.NUM_ROW
                    && col + bottomLeft >= 0) {
                int newPos = (row - bottomLeft) * GameModel.NUM_COLUMN + (col + bottomLeft);
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    bottomLeft--;
                } else {
                    break;
                }
            }
            while (row - topRight >= 0
                    && col + topRight < GameModel.NUM_COLUMN) {
                int newPos = (row - topRight) * GameModel.NUM_COLUMN + (col + topRight);
                if (positionSet.contains(newPos) && ballMap.get(newPos).getColor().getValue() == color) {
                    topRight++;
                } else {
                    break;
                }
            }
            bottomLeft++;
            topRight--;
            if (topRight - bottomLeft >= 4) {
                for (int i = bottomLeft; i <= topRight; i++) {
                    fiveInRowBallSet.add(ballMap.get((row - i) * GameModel.NUM_COLUMN + (col + i)));
                }
            }

            positionSet.remove(position);
        }

        if (!fiveInRowBallSet.isEmpty()) {
            touchLocked = true;
        }
        return !fiveInRowBallSet.isEmpty();
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int size = touchEvents.size();
        for (int i = 0; i < size; i++) {
            TouchEvent event = touchEvents.get(i);
            if (TouchUtils.eventInBounds(event,
                    MENU_RESUME_LEFT_POS,
                    MENU_RESUME_TOP_POS,
                    MENU_RESUME_WIDTH,
                    MENU_RESUME_HEIGHT)) {
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
            } else if (TouchUtils.eventInBounds(event,
                    MENU_QUIT_LEFT_POS,
                    MENU_QUIT_TOP_POS,
                    MENU_QUIT_WIDTH,
                    MENU_QUIT_HEIGHT)) {
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
        // Draw background and tiles
        graphics.drawImage(Assets.gameBackground, 0, 0);
        for (int i = 0; i < GameModel.NUM_ROW; i++) {
            for (int j = 0; j < GameModel.NUM_COLUMN; j++) {
                graphics.drawImage(Assets.tileBackground,
                        BOARD_LEFT_POS + j * TILE_WIDTH,
                        BOARD_TOP_POS + i * TILE_HEIGHT);
            }
        }

        // Draw balls
        for (Ball ball : ballMap.values()) {
            Image ballImage;
            switch (ball.getColor()) {
                case RED:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.redBallLarge
                                    : Assets.redBall;
                    break;

                case GREEN:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.greenBallLarge
                                    : Assets.greenBall;
                    break;

                case BLUE:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.blueBallLarge
                                    : Assets.blueBall;
                    break;

                case YELLOW:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.yellowBallLarge
                                    : Assets.yellowBall;
                    break;

                case PURPLE:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.purpleBallLarge
                                    : Assets.purpleBall;
                    break;

                case CYAN:
                    ballImage = ball.isChanging()
                            ? Assets.greyBall :
                            ball.isSelected()
                                    ? Assets.cyanBallLarge
                                    : Assets.cyanBall;
                    break;

                case GREY:
                default:
                    ballImage = ball.isSelected() ? Assets.greyBallLarge : Assets.greyBall;
                    break;
            }
            int row = ball.getRow(), col = ball.getColumn();
            if (ball.isSelected()) {
                graphics.drawImage(ballImage,
                        BOARD_LEFT_POS + col * TILE_WIDTH + BALL_SELECTED_POS_OFFSET,
                        BOARD_TOP_POS + row * TILE_HEIGHT + BALL_SELECTED_POS_OFFSET);
            } else {
                graphics.drawImage(ballImage,
                        BOARD_LEFT_POS + col * TILE_WIDTH + BALL_POS_OFFSET,
                        BOARD_TOP_POS + row * TILE_HEIGHT + BALL_POS_OFFSET);
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
        graphics.drawImage(Assets.menuOverlay, 0, 0);
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
