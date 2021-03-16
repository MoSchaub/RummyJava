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
import javafx.scene.media.*;

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

        public void initialize() {
                draggingStone = new Stone(Stone.Color.GREEN, 666);
                draggingRectangle = new Rectangle();
                setUpTischGrid();
        }

        public void setUpStones() {
                this.rectangles = new ArrayList<StoneRectangle>();

                // empty the hstack
                hstack.getChildren().clear();
                hstack.setSpacing(0);

                hstack2.getChildren().clear();
                hstack2.setSpacing(0);

                new Thread(() -> {
                        this.updateStones();
                }).run();
        }

        private void setUpTischGrid() {

                tableGrid.setGridLinesVisible(true);

                tableGrid.setOnDragOver(new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                                /* data is dragged over the target */
                                /*
                                 * accept it only if it is not dragged from the same node and if it has a string
                                 * data
                                 */
                                if (event.getGestureSource() != tableGrid && event.getDragboard().hasImage()
                                                && event.getTarget() == tableGrid) {
                                        /* allow for moving */
                                        event.acceptTransferModes(TransferMode.MOVE);

                                        ArrayList<Stone> steine = new ArrayList<Stone>();
                                        steine.add(draggingStone);
                                        game.neueReiheMitSteinen(steine);

                                        for (int i = 0; i < game.getTisch().size(); i++) {
                                                for (int j = 0; j < game.getTisch().get(i).getStones().size(); i++) {
                                                        if (hstack.getChildren().remove(draggingRectangle)) {
                                                                tableGrid.setConstraints(draggingRectangle, i, j);
                                                                tableGrid.addRow(j, draggingRectangle);
                                                                updateStones();
                                                        }
                                                }
                                        }
                                        // remove rect from hstack and add it to the grid
                                        // hstack.getChildren().remove(draggingRectangle);
                                        // tableGrid.getChildren().add(draggingRectangle);

                                        // draggingRectangle = null;

                                        // TODO: tell the game to remove it from the players hand and add it to the
                                        // table
                                        // TODO: update the grid view

                                }

                                event.consume();
                        }
                });
        }

        private void updateStones() {

                // get the player
                Player player = game.getPlayer(playerId);

                // calculate optimal width and height
                double width;
                double height;

                if (player.getHand().size() < 30) {
                        width = 1033 / 15;
                } else {
                        width = 2 * 1033 / (player.getHand().size() + 1);
                }

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

                        rect.setOnDragDetected(new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent event) {
                                        if (event.getSource() != tableGrid) {
                                                /* drag was detected, start a drag-and-drop gesture */
                                                /* allow any transfer mode */
                                                Dragboard db = rect.startDragAndDrop(TransferMode.ANY);

                                                /* Put a string on a dragboard */
                                                ClipboardContent content = new ClipboardContent();

                                                Image clipImage = new Image(stone.getFilePath(), width, height, true,
                                                                true);
                                                draggingStone = stone;
                                                draggingRectangle = rect;

                                                content.putImage(clipImage);
                                                db.setContent(content);

                                                event.consume();
                                        }
                                }
                        });

                }

        }

        @FXML
        public void ziehen(ActionEvent actionEvent) {
                Player player = game.getPlayer(playerId);
                game.zieheStein(player);
                new Thread(() -> {
                        this.updateStones();
                }).run();

                new Thread(() -> {
                        final Media media = new Media(
                                        getClass().getResource("/SpieleSound/KarteziehenSound.mp3").toString());

                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.play();
                }).start();

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
                System.out.println("toggleSound");
                Image image = new Image(getClass().getResource("/SoundOff.png").toString());
                ImageView imageView = (ImageView) this.soundButton.getGraphic();// getChildren().get(0)
                imageView.setImage(image);
                sound = !sound;
        }

        public void openFirstScene(ActionEvent actionEvent) {
                Stage primaryStage = (Stage) ((Node) ziehenButton).getScene().getWindow();
                primaryStage.setScene(firstScene);
        }

}
