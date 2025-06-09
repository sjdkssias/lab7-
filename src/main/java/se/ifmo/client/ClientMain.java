package se.ifmo.client;

import javafx.application.Application;
import javafx.stage.Stage;
import se.ifmo.client.ui.controllers.SceneManager;


public class ClientMain extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.switchTo("/views/StartView.fxml");
        stage.setTitle("Collection app");
        stage.show();

        new Thread(() -> {
            try {
                Client client = Client.getInstance();
                client.start();
            } catch (Exception e) {
                System.err.println("Ошибка связи с сервером: " + e.getMessage());
            }
        }).start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

