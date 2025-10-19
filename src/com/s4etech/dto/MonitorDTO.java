package com.s4etech.dto;

public class MonitorDTO {
    private int number;
    private int x;
    private int y;
    private int width;
    private int height;

    public MonitorDTO(int number, int x, int y, int width, int height) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getNumber() {
        return number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("Monitor %d: (%d,%d) %dx%d", number, x, y, width, height);
    }
}
