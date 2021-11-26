package app.nwrfc.saprfc.controller;

import app.nwrfc.saprfc.util.ErrorUtility;
import app.nwrfc.saprfc.util.StageUtilities;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private final StageUtilities utilities;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @FXML private TabPane tabPane;
    @FXML private MenuItem exportMenu;
    @FXML private Menu formatMenu;


    public AppController() {
        this.utilities = StageUtilities.getInstance();
    }

    @FXML
    void about() {

    }

    @FXML
    void closeAllTabs() {
        tabPane.getTabs().clear();
    }

    @FXML
    void closeConnection() {
        utilities.logout();
    }

    @FXML
    void closeTab() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab!=null) {
            tabPane.getTabs().remove(tab);
        }
    }

    @FXML
    void newRfcClient() {
        utilities.createNewRfcTab(tabPane);
    }

    @FXML
    void preferences() {

    }

    @FXML
    void quit() {
        Window window = tabPane.getScene().getWindow();
        window.fireEvent(new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void showDetails() {
        utilities.createNewDetailsTab(tabPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilities.createNewDetailsTab(tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs,old,tab) -> {
            if(tab.getText().trim().equals("Connection")) {
                Platform.runLater(()->{
                    exportMenu.setDisable(true);
                    formatMenu.setDisable(true);
                });
            } else {
                Platform.runLater(()->{
                    exportMenu.setDisable(false);
                    formatMenu.setDisable(false);
                });
            }
        });
    }

    @FXML
    public void formatImport() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(0);
        TextArea importInput = (TextArea) inputSide.getTabs().get(1).getContent();
        if (!importInput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(importInput.getText().trim());
            Platform.runLater(()->importInput.setText(gson.toJson(element)));
        }
    }

    @FXML
    public void formatChangingInput() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(0);
        TextArea changingInput = (TextArea) inputSide.getTabs().get(2).getContent();
        if (!changingInput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(changingInput.getText().trim());
            Platform.runLater(()->changingInput.setText(gson.toJson(element)));
        }
    }

    @FXML
    public void formatTablesInput() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(0);
        TextArea tablesInput = (TextArea) inputSide.getTabs().get(3).getContent();
        if (!tablesInput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(tablesInput.getText().trim());
            Platform.runLater(()->tablesInput.setText(gson.toJson(element)));
        }
    }

    @FXML
    public void formatExport() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(1);
        TextArea exportOutput = (TextArea) inputSide.getTabs().get(0).getContent();
        if (!exportOutput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(exportOutput.getText().trim());
            Platform.runLater(()->exportOutput.setText(gson.toJson(element)));
        }
    }

    @FXML
    public void formatChangingOutput() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(1);
        TextArea changingOutput = (TextArea) inputSide.getTabs().get(1).getContent();
        if (!changingOutput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(changingOutput.getText().trim());
            Platform.runLater(()->changingOutput.setText(gson.toJson(element)));
        }
    }

    @FXML
    public void formatTablesOutput() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        BorderPane pane  = (BorderPane) tab.getContent();
        SplitPane splitPane = (SplitPane) pane.getCenter();
        TabPane inputSide = (TabPane) splitPane.getItems().get(1);
        TextArea tablesOutput = (TextArea) inputSide.getTabs().get(2).getContent();
        if (!tablesOutput.getText().trim().isEmpty()) {
            JsonElement element = JsonParser.parseString(tablesOutput.getText().trim());
            Platform.runLater(()->tablesOutput.setText(gson.toJson(element)));
        }
    }
    
    private File getFileToSave(String type) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                type.equals("xml") ? new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
                        :new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Save");
        return chooser.showSaveDialog(tabPane.getScene().getWindow());
    }

    public void save() {
        File file = getFileToSave("json");
        if (file!=null) {
            JsonObject object = new JsonObject();
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            BorderPane pane  = (BorderPane) tab.getContent();
            HBox hBox = (HBox) pane.getTop();
            TextField fmField = (TextField) hBox.getChildren().get(0);
            object.addProperty("function_module",fmField.getText());
            SplitPane splitPane = (SplitPane) pane.getCenter();
            TabPane inputSide = (TabPane) splitPane.getItems().get(0);
            TabPane outputSide = (TabPane) splitPane.getItems().get(1);
            JsonObject inputs = new JsonObject();
            TextArea imports = (TextArea) inputSide.getTabs().get(1).getContent();
            if (!imports.getText().trim().isEmpty()) {
                inputs.add("import",JsonParser.parseString(imports.getText()));
            }
            TextArea changingInput = (TextArea) inputSide.getTabs().get(2).getContent();
            if (!changingInput.getText().trim().isEmpty()) {
                inputs.add("changing",JsonParser.parseString(changingInput.getText()));
            }
            TextArea tablesInput = (TextArea) inputSide.getTabs().get(3).getContent();
            if (!tablesInput.getText().trim().isEmpty()) {
                inputs.add("tables",JsonParser.parseString(tablesInput.getText()));
            }
            JsonObject outputs = new JsonObject();
            TextArea exports = (TextArea) outputSide.getTabs().get(0).getContent();
            if (!exports.getText().trim().isEmpty()) {
                outputs.add("exports",JsonParser.parseString(exports.getText()));
            }
            TextArea changingOutput = (TextArea) outputSide.getTabs().get(1).getContent();
            if (!changingOutput.getText().trim().isEmpty()) {
                outputs.add("changing",JsonParser.parseString(changingOutput.getText()));
            }
            TextArea tablesOutput = (TextArea) outputSide.getTabs().get(2).getContent();
            if (!tablesOutput.getText().trim().isEmpty()) {
                outputs.add("tables",JsonParser.parseString(tablesOutput.getText()));
            }
            object.add("input",JsonParser.parseString(inputs.toString()));
            object.add("output",JsonParser.parseString(outputs.toString()));

           try {
               FileWriter writer = new FileWriter(file);
               writer.write(gson.toJson(JsonParser.parseString(object.toString())));
               writer.flush();
               writer.close();
           } catch (Exception ex) {
               ErrorUtility.showDetailedError(null,ex.getMessage(),ex);
           }
        }
    }
}
