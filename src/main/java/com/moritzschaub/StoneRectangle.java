package com.moritzschaub;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
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

    // set the rectangles content to this image
    ImagePattern imagePattern = new ImagePattern(image, 0, 0, this.getWidth(), this.getHeight(), false);

    this.setFill(imagePattern);
  }

  public Image getImage() {
    return this.image;
  }
}
