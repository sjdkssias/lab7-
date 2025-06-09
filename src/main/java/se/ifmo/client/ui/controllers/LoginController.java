package se.ifmo.client.ui.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import se.ifmo.client.ClientManager;
import se.ifmo.client.chat.Response;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label registerLabel;

    private ClientManager clientManager;

    @FXML
    void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        Task<Response> loginTask = new Task<>() {
            @Override
            protected Response call() throws Exception {
                return ClientManager.getInstance().login(username, password);
            }
        };

        loginTask.setOnSucceeded(event -> {
            Response response = loginTask.getValue();
            if (response.success()) {
                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText("Success enter!");
                SceneManager.switchTo("/views/MainView.fxml");
            } else {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Entering error: " + response.message());
            }
        });

        loginTask.setOnFailed(event -> {
            Throwable e = loginTask.getException();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Ошибка сети: " + e.getMessage());
            loginButton.setDisable(false);
        });
        new Thread(loginTask).start();
    }

    @FXML
    void onRegisterLabelClick(MouseEvent event) {
        SceneManager.switchTo("/views/RegisterView.fxml");
    }
}