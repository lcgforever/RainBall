package com.chenguangl.rainball.util;

import com.chenguangl.rainball.data.Ball;
import com.chenguangl.rainball.data.Ball.BallColor;
import com.chenguangl.rainball.model.GameModel;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtils {

    private static Random random = new Random();

    public static Set<Integer> generateRandomPositions(Set<Integer> blockedPositions, int n) {
        Set<Integer> set = new HashSet<>();
        int count = Math.min(n, GameModel.TOTAL_COUNT - blockedPositions.size());

        do {
            int row = random.nextInt(GameModel.NUM_ROW);
            int col = random.nextInt(GameModel.NUM_COLUMN);
            int position = row * GameModel.NUM_COLUMN + col;
            if (!blockedPositions.contains(position)) {
                set.add(position);
            }
        } while (set.size() < count);

        return set;
    }

    public static BallColor generateRandomColor() {
        int randomNum = random.nextInt(Ball.NUM_COLORS);
        return BallColor.from(randomNum);
    }
}
