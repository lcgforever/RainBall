package com.chenguangl.rainball.data;

public class Ball {

    public static final int NUM_COLORS = 6;

    public enum BallColor {
        RED(0),
        GREEN(1),
        BLUE(2),
        YELLOW(3),
        PURPLE(4),
        CYAN(5),
        GREY(6);

        int value;

        BallColor(int value) {
            this.value = value;
        }

        public static BallColor from(int value) {
            for (BallColor ballColor : values()) {
                if (ballColor.value == value) {
                    return ballColor;
                }
            }
            return RED;
        }

        public int getValue() {
            return value;
        }
    }

    private int row;
    private int column;
    private BallColor color;
    private boolean selected;
    private boolean changing;

    public Ball() {
    }

    public Ball(int row, int column, BallColor color) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.selected = false;
        this.changing = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BallColor getColor() {
        return color;
    }

    public void setColor(BallColor color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isChanging() {
        return changing;
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }
}
