package com.moritzschaub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Rummykub extends Application {
    public static void main(String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource("/screen1.fxml"));
        Parent firstPane = firstPaneLoader.load();
        Scene firstScene = new Scene(firstPane, 1920, 1080);

        FirstController firstPaneController = firstPaneLoader.getController();
        firstPaneController.setFirstScene(firstScene);

        stage.setScene(firstScene);
        stage.show();
    }
}
