package se.ifmo.client.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class StartController {
    @FXML
    private ImageView background;
    @FXML
    private Button playButton;

    @FXML
    void initialize() {
        playButton.setOnAction(event -> SceneManager.switchTo("/views/LoginView.fxml"));
    }
}