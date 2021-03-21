package com.moritzschaub;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.*;
import javafx.fxml.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import java.util.ArrayList;

public class SecondController {

        private Scene firstScene;
        private Game game;
        private int playerId;
        private ArrayList<StoneRectangle> rectangles;
        private boolean sound;
        private Stone draggingStone;
        private Rectangle draggingRectangle;

        @FXML
        private HBox hstack, hstack2;

        @FXML
        private Button soundButton;

        @FXML
        private Button ziehenButton;

        @FXML
        private GridPane tableGrid;

        @FXML
        private ImageView avatarView;

        @FXML
        private Circle avatarCircle;

        public void initialize() {
                sound = true;
                draggingStone = new Stone(Stone.Color.GREEN, 666);
                draggingRectangle = new Rectangle();
                setUpTischGrid();
        }

        public void setUpStones() {
                this.rectangles = new ArrayList<>();

                // empty the hstack
                hstack.getChildren().clear();
                hstack.setSpacing(0);

                hstack2.getChildren().clear();
                hstack2.setSpacing(0);

                new Thread(this::updateStones).run();

                Image image = new Image("StockCharaktere/Figur1.png", 216, 216, true, true);
                avatarView.setImage(image);
                avatarView.setClip(avatarCircle);
        }

        private void setUpTischGrid() {

                tableGrid.setGridLinesVisible(true);

                tableGrid.setOnDragDropped(this::handleDropForGrid);

                tableGrid.setOnDragOver(this::allowForMoving);
        }

        private void handleDropForGrid(DragEvent event) {
                if (event.getDragboard().hasImage()) {
                        //get drop location
                        double positionX = event.getX();
                        double positionY = event.getY();

                        //get cell for location
                        double cellWidth = tableGrid.getWidth() / (2 * tableGrid.getRowCount());
                        double cellHeight = 2 * tableGrid.getHeight() / tableGrid.getColumnCount();

                        int columnNumber = roundedUp(positionX/cellWidth) -1;
                        int rowNumber = roundedUp(positionY/cellHeight) -1 ;

                        // remove rect from hstack and add it to the grid
                        if (hstack.getChildren().contains(draggingRectangle)) {
                                hstack.getChildren().remove(draggingRectangle);

                        } else if (hstack2.getChildren().contains(draggingRectangle)) {
                                hstack2.getChildren().remove(draggingRectangle);

                        } else tableGrid.getChildren().remove(draggingRectangle);

                        //add the stone
                        game.legeStein(draggingStone, game.getPlayer(playerId), rowNumber, columnNumber);
                        draggingStone = null;

                        GridPane.setRowIndex(draggingRectangle, rowNumber);
                        GridPane.setColumnIndex(draggingRectangle, columnNumber);
                        tableGrid.getChildren().add(draggingRectangle);

                        draggingRectangle = null;
                }
        }

        private void allowForMoving(DragEvent event) {
                if (event.getGestureSource() != tableGrid && event.getDragboard().hasImage()
                        && event.getTarget() == tableGrid) {
                        /* allow for moving */
                        event.acceptTransferModes(TransferMode.MOVE);
                }
        }

        //rounds double to an int to the next highest int
        // e. g. 0.1 returns 1; 5.9 -> 6
        private int roundedUp(double d) {
                int i = 0;
                while (d > 0) {
                       d--;
                       i++;
                }
                return i;
        }

        private void updateStones() {

                // get the player
                Player player = game.getPlayer(playerId);

                // calculate optimal width and height
                double width;
                double height;

                if (player.getHand().size() < 30) {
                        width = 68;
                } else width = (2 * 1033) / (player.getHand().size() + 1);

                height = width / 0.74;

                // loop through the players stones
                for (int i = 0; i < player.getHand().size(); i++) {

                        Stone stone = player.getHand().get(i); // stone

                        StoneRectangle rect;

                        if (rectangles.size() > i) { // check if a rect exists already

                                // old rect update it
                                rect = rectangles.get(i);
                                if (player.getHand().contains(rect.stone)) {
                                        rect.setWidth(width);
                                        rect.setHeight(height);

                                }
                        } else {

                                rect = new StoneRectangle(stone, width, height); // create new rect for the stone
                                rectangles.add(rect);

                                //add rectangles to the view
                                if (hstack.getChildren().size() < 15) {
                                        hstack.getChildren().add(rect);
                                } else {
                                        if (hstack.getChildren().size() > hstack2.getChildren().size()) {
                                                hstack2.getChildren().add(rect);
                                        } else {
                                                hstack.getChildren().add(rect);
                                        }
                                }

                                // set the image for the rect
                                Image image = new Image(stone.getFilePath());
                                rect.setFill(new ImagePattern(image));
                        }

                        rect.setOnDragDetected( (event) -> {
                                handleRectDrag(event, rect);
                        });

                }

        }

        private void handleRectDrag(MouseEvent event, StoneRectangle rect) {
                if (event.getSource() != tableGrid) {
                        /* drag was detected, start a drag-and-drop gesture */
                        /* allow any transfer mode */
                        Dragboard db = rect.startDragAndDrop(TransferMode.ANY);

                        /* Put a string on a dragboard */
                        ClipboardContent content = new ClipboardContent();

                        Image clipImage = new Image(rect.stone.getFilePath(), rect.getWidth(), rect.getHeight(), true, true);
                        draggingStone = rect.stone;
                        draggingRectangle = rect;

                        content.putImage(clipImage);
                        db.setContent(content);

                        event.consume();
                }
        }

        @FXML
        public void ziehen(ActionEvent actionEvent) {
                Player player = game.getPlayer(playerId);
                game.zieheStein(player);
                this.updateStones();

                if (this.sound) {
                        new Thread(() -> {
                                final Media media = new Media(
                                                getClass().getResource("/SpieleSound/KarteziehenSound.mp3").toString());

                                MediaPlayer mediaPlayer = new MediaPlayer(media);
                                mediaPlayer.play();
                        }).start();
                }
        }

        public void setGame(Game game) {
                this.game = game;
        }

        public void setPlayerId(int playerId) {
                this.playerId = playerId;
        }

        public void setFirstScene(Scene scene) {
                firstScene = scene;
        }

        public void toggleSoundHandler(ActionEvent actionEvent) {
                // toggleSound
                sound = !sound;

                // change the image
                Image soundOffImage = new Image(getClass().getResource("/SoundOff.png").toString());
                Image soundOnImage = new Image(getClass().getResource("/SoundOn.png").toString());
                if (!sound) {
                        ImageView imageView = (ImageView) this.soundButton.getGraphic();// getChildren().get(0)
                        imageView.setImage(soundOffImage);
                } else {
                        ImageView imageView = (ImageView) this.soundButton.getGraphic();// getChildren().get(0)
                        imageView.setImage(soundOnImage);
                }

        }

        public void openFirstScene(ActionEvent actionEvent) {
                Stage primaryStage = (Stage) ziehenButton.getScene().getWindow();
                primaryStage.setScene(firstScene);
        }

}
