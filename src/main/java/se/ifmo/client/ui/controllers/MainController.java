package se.ifmo.client.ui.controllers;
import javafx.beans.property.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import se.ifmo.client.Client;
import se.ifmo.client.ClientManager;
import se.ifmo.client.chat.Response;
import se.ifmo.server.models.classes.Dragon;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {

    @FXML
    private TableView<Dragon> dragonTable;
    @FXML
    private TableColumn<Dragon, Long> idColumn;
    @FXML
    private TableColumn<Dragon, String> nameColumn;
    @FXML
    private TableColumn<Dragon, String> coordinatesColumn;
    @FXML
    private TableColumn<Dragon, LocalDateTime> creationDateColumn;
    @FXML
    private TableColumn<Dragon, Boolean> speakingColumn;
    @FXML
    private TableColumn<Dragon, String> colorColumn;
    @FXML
    private TableColumn<Dragon, String> characterColumn;
    @FXML
    private TableColumn<Dragon, Float> headToothcountColumn;
    @FXML
    private TableColumn<Dragon, String> ownerColumn;
    @FXML
    private Label statusLabel;

    private final ObservableList<Dragon> dragonData = FXCollections.observableArrayList();
    private final Client client = Client.getInstance();
    private final ClientManager clientManager = ClientManager.getInstance();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        coordinatesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoordinates().toString()));
        creationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
        speakingColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isSpeaking()).asObject());
        colorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor().name()));
        characterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCharacter() != null ? cellData.getValue().getCharacter().name() : "N/A"));
        headToothcountColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getHead().getToothcount()).asObject());
        ownerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));
        dragonTable.setItems(dragonData);
        updateCollection();
    }

    @FXML
    private void updateCollection() {
        try {
            Response response = clientManager.showAllDragons();
            dragonData.clear();
            if (response.success() && response.dragons() != null) {
                response.dragons().forEach(item -> {
                    if (item instanceof Dragon) {
                        dragonData.add((Dragon) item);
                }
            });
            } else {
                statusLabel.setText("Не удалось обновить коллекцию: " + response.message());
            }
        } catch (IOException e) {
                statusLabel.setText("Ошибка связи с сервером при обновлении: " + e.getMessage());
        }
    }

    @FXML
    private void addDragon() {
        statusLabel.setText("Функция 'Добавить дракона' пока не реализована.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление дракона");
        alert.setHeaderText(null);
        alert.setContentText("Здесь будет логика для добавления нового дракона.");
        alert.showAndWait();
    }

    @FXML
    private void editDragon() {
        Dragon selectedDragon = dragonTable.getSelectionModel().getSelectedItem();
        if (selectedDragon != null) {
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите дракона для изменения.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteDragon() {
        Dragon selectedDragon = dragonTable.getSelectionModel().getSelectedItem();
        if (selectedDragon != null) {
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите дракона для удаления.");
            alert.showAndWait();
        }
    }


    public void shutdown() {
        executor.shutdownNow();
    }
}