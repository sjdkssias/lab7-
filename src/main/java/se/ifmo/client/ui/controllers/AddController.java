package se.ifmo.client.ui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.ifmo.client.ClientManager;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.commands.AddCommand;
import se.ifmo.server.models.classes.Coordinates;
import se.ifmo.server.models.classes.Dragon;
import se.ifmo.server.models.classes.DragonHead;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;

import java.io.IOException;

public class AddController {

    @FXML private TextField nameField;
    @FXML private TextField coordinateXField;
    @FXML private TextField coordinateYField;
    @FXML private TextField headToothCountField;
    @FXML private ChoiceBox<Color> colorBox;
    @FXML private ChoiceBox<DragonCharacter> characterBox;
    @FXML private CheckBox speakingCheckBox;
    @FXML private Label statusLabel;

    private Stage dialogStage;
    private final ClientManager clientManager = ClientManager.getInstance();

    @FXML
    private void initialize() {
        colorBox.setItems(FXCollections.observableArrayList(Color.values()));
        colorBox.getSelectionModel().selectFirst();

        characterBox.setItems(FXCollections.observableArrayList(DragonCharacter.values()));
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Обработчик для кнопки "Сохранить".
     * Валидирует ввод, создает объект Dragon и отправляет на сервер.
     */
    @FXML
    private void handleSave() throws IOException {
        Dragon dragon = createDragon();
        Response response = ClientManager.getInstance().addDragon(dragon);
        if (response.success()){
            System.out.println("sss");
            dialogStage.close();
        } else {
            System.out.println("неправильно");
        }
    }

    /**
     * Обработчик для кнопки "Отмена".
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private Dragon createDragon(){
        Dragon dragon = new Dragon();
        dragon.setName(nameField.getText().trim());
        Coordinates coordinates = new Coordinates();
        coordinates.setX((float) Double.parseDouble(coordinateXField.getText()));
        coordinates.setY(Long.parseLong(coordinateYField.getText()));
        dragon.setCoordinates(coordinates);
        DragonHead head = new DragonHead();
        head.setToothcount(Float.parseFloat(headToothCountField.getText()));
        dragon.setHead(head);
        dragon.setSpeaking(speakingCheckBox.isSelected());
        dragon.setColor(colorBox.getValue());
        if (characterBox.getValue() != null) {
            dragon.setCharacter(characterBox.getValue());
        }
        return dragon;
    }

}