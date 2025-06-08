package se.ifmo.client.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneManager {
    public static Stage primaryStage;
    private static final HashMap<String, Scene> scenes = new HashMap<>();

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            Scene scene = scenes.get(fxmlPath);
            if (scene == null) {
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
                Parent root = loader.load();
                scene = new Scene(root);
                scenes.put(fxmlPath, scene);
            }
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

