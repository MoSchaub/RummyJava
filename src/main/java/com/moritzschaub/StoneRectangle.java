package com.moritzschaub;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class StoneRectangle extends Rectangle {
    private Stone stone;
    private Image image;

    public StoneRectangle(Stone stone, double width, double height) {
        super(width, height);
        this.stone = stone;
    }

    public Stone getStone() {
        return this.stone;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }
}
