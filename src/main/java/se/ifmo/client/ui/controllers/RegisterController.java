package se.ifmo.client.ui.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import se.ifmo.client.ClientManager;
import se.ifmo.client.chat.Response;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginLabel;


    @FXML
    void onRegisterButtonClick() throws InterruptedException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        Task<Response> regTask = new Task<>() {
            @Override
            protected Response call() throws Exception {
                return ClientManager.getInstance().register(username, password);
            }
        };
        regTask.setOnSucceeded(event -> {
            Response response = regTask.getValue();
            if (response.success()) {
                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText("Success enter!");
                SceneManager.switchTo("/views/LoginView.fxml");
            } else {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Entering error: " + response.message());
            }
            registerButton.setDisable(false);
        });
        regTask.setOnFailed(event -> {
            Throwable e = regTask.getException();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Ошибка сети: " + e.getMessage());
            registerButton.setDisable(false);
        });
        new Thread(regTask).start();
    }

    @FXML
    void onLoginLabelClick(MouseEvent event) {
        SceneManager.switchTo("/views/LoginView.fxml");
    }
}

