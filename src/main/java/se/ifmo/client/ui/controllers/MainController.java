package se.ifmo.client.ui.controllers;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.ifmo.client.Client;
import se.ifmo.client.ClientManager;
import se.ifmo.client.chat.Response;
import se.ifmo.server.models.classes.Dragon;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private ContextMenu rowCommands;

    private final ObservableList<Dragon> dragonData = FXCollections.observableArrayList();
    private final Client client = Client.getInstance();
    private final ClientManager clientManager = ClientManager.getInstance();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    public void initialize() {
        setTableColumns();
        initializeRowMenu();
        dragonTable.setRowFactory(tv -> {
            TableRow<Dragon> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    if ((event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) ||
                            (event.getButton() == MouseButton.SECONDARY)) {
                        if (!row.isSelected()) {
                            row.getTableView().getSelectionModel().clearAndSelect(row.getIndex());
                        }
                        rowCommands.show(row, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return row;
        });
        dragonTable.setItems(dragonData);
        updateCollection();
    }
    @FXML
    public void setTableColumns(){
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

    }
    @FXML
    public void initializeRowMenu(){
        rowCommands = new ContextMenu();
        rowCommands.getStyleClass().add("context-menu");
        MenuItem infoItem = new MenuItem("info");
        MenuItem updateItem = new MenuItem("update");
        MenuItem removeItem = new MenuItem("remove by id");
        MenuItem removeGreaterItem = new MenuItem("remove greater than ID");
        MenuItem removeLowerItem = new MenuItem("remove lower than ID");
        MenuItem filterHeadItem = new MenuItem("tooth filter");
        infoItem.setOnAction(e -> showDragonInfo());
        updateItem.setOnAction(e -> editDragon());
        removeItem.setOnAction(e -> {
            try {
                handleRemoveKey();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        removeGreaterItem.setOnAction(e -> {
            try {
                handleRemoveGreater();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        removeLowerItem.setOnAction(e -> {
            try {
                handleRemoveLower();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        filterHeadItem.setOnAction(e -> handleFilterGreaterThanHead());

        rowCommands.getItems().addAll(infoItem, updateItem, removeItem, removeGreaterItem, removeLowerItem, filterHeadItem);
    }

    private void showDragonInfo() {
        Dragon selected = dragonTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setContentText(selected.toString());
            infoAlert.showAndWait();
        }
    }

    private void handleRemoveGreater() throws IOException {
        Dragon selected = dragonTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        clientManager.removeGreater(selected.getId());
    }

    private void handleRemoveLower() throws IOException {
        Dragon selected = dragonTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        clientManager.removeLower(selected.getId());
    }

    private void handleRemoveKey() throws IOException {
        Dragon selected = dragonTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        clientManager.removeKey(selected.getId());
    }


    private void handleFilterGreaterThanHead() {
        Dragon selected = dragonTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
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
                statusLabel.setText("Fail to update: " + response.message());
            }
        } catch (IOException e) {
                statusLabel.setText("Network error " + e.getMessage());
        }
    }

    @FXML
    private void addDragon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddView.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add dragon");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(SceneManager.primaryStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            AddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            updateCollection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editDragon() {
        Dragon selectedDragon = dragonTable.getSelectionModel().getSelectedItem();
        if (selectedDragon != null) {
        } else {
            //хуй
        }
    }

    @FXML
    private void deleteDragon() {
        Dragon selectedDragon = dragonTable.getSelectionModel().getSelectedItem();
        if (selectedDragon != null) {
        } else {
           //хуй
        }
    }
}