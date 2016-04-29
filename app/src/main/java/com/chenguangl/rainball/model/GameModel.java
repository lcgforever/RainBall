package com.chenguangl.rainball.model;

import com.chenguangl.rainball.data.Ball;
import com.chenguangl.rainball.util.pathfinding.PathFindingContext;
import com.chenguangl.rainball.util.pathfinding.TileBasedMap;

import java.util.HashMap;
import java.util.Map;

public class GameModel implements TileBasedMap {

    public static final int NUM_ROW = 15;
    public static final int NUM_COLUMN = 10;
    public static final int TOTAL_COUNT = NUM_ROW * NUM_COLUMN;
    public static final int INITIAL_BALL_COUNT = 4;
    public static final int GENERATE_BALL_COUNT = 3;
    public static final int STATE_CLEAR = 0;
    public static final int STATE_BLOCKED = 1;

    private final int[][] tileMap;
    private Map<Integer, Ball> ballMap;

    public GameModel() {
        tileMap = new int[NUM_ROW][NUM_COLUMN];
        ballMap = new HashMap<>();
    }

    @Override
    public int getWidthInTiles() {
        return NUM_COLUMN;
    }

    @Override
    public int getHeightInTiles() {
        return NUM_ROW;
    }

    @Override
    public boolean blocked(PathFindingContext context, int tx, int ty) {
        return tileMap[ty][tx] != 0;
    }

    @Override
    public float getCost(PathFindingContext context, int tx, int ty) {
        return 1.0f;
    }

    @Override
    public void pathFinderVisited(int x, int y) {

    }

    public void updateTileMapAtPosition(int row, int col, int value) {
        tileMap[row][col] = value;
    }

    public Map<Integer, Ball> getBallMap() {
        return ballMap;
    }
}
