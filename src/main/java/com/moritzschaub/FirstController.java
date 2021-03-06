package com.moritzschaub;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class FirstController {

    @FXML
    private TextField textFieldName;

    @FXML
    private Label label;

    @FXML
    private Button buttonstart, buttonregeln;

    private Scene firstScene;

    public void setFirstScene(Scene scene) {
        this.firstScene = scene;
    }

    @FXML
    public void openSecondScene(ActionEvent actionEvent) throws Exception {

        // load the second screen from fxml
        FXMLLoader secondPageLoader = new FXMLLoader(getClass().getResource("/screen2.fxml"));
        Parent secondPane = secondPageLoader.load();
        Scene secondScene = new Scene(secondPane, 1920, 1080);

        // get the controller for the second screen
        SecondController secondPaneController = (SecondController) secondPageLoader.getController();

        // create the game for the second screen
        Game spiel = new Game();

        // add the player to the game and get its id
        int playerId = spiel.addNewPlayer(textFieldName.getText());

        // inject data into the second screen
        secondPaneController.setPlayerId(playerId);
        secondPaneController.setGame(spiel);
        secondPaneController.setFirstScene(firstScene);

        // switch to the second scene
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);

        secondPaneController.setUpStones();
    }

    @FXML
    public void showRegeln(ActionEvent actionEvent) {
        // TODO: move to regeln scene
    }

    public ImageView customImageView(String url) {
        int width = 155;
        int height = 80;

        // create image
        Image img = new Image(url, width, height, true, true);

        // create the rect
        Rectangle rect = new Rectangle(width, height);
        // rounded corners
        rect.setArcHeight(10.0d);
        rect.setArcWidth(10.0d);

        // create ImageView
        ImageView view = new ImageView(img);
        view.setClip(rect);

        return view;
    }

    public void initialize() {

        buttonregeln.setGraphic(customImageView("/Regeln.png"));
        buttonstart.setGraphic(customImageView("/Start.png"));

    }

}
