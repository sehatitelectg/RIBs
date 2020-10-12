package com.uber.rib.core.screenstack.transition;

public enum Direction {
    FORWARD(1),
    BACKWARD(-1);

    private final int sign;

    Direction(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }
}
