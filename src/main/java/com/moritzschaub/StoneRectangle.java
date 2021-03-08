package com.moritzschaub;

import javafx.scene.shape.Rectangle;

public class StoneRectangle extends Rectangle {
    Stone stone;

    public StoneRectangle(Stone stone, double width, double height) {
        super(width, height);
        this.stone = stone;
    }

    public Stone getStone() {
        return this.stone;
    }
}
