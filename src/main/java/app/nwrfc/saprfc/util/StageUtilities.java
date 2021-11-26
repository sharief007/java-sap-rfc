package app.nwrfc.saprfc.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class StageUtilities {
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Stage stage;
    private static StageUtilities utilities = null;
    private final String title = "SAP RFC Client";

    private StageUtilities() {
        this.fxmlLoader = new FXMLLoader();
    }

    public static StageUtilities getInstance() {
        if (Objects.isNull(utilities)) {
            utilities = new StageUtilities();
        }
        return utilities;
    }

    private void showLogOn(String title) throws IOException {
        InputStream is = loadFile("LogOn.fxml");
        if (Objects.nonNull(is)){
            fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(is);
            scene = new Scene(root);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void showApplication()  {
        try {
            fxmlLoader = new FXMLLoader();
            BorderPane pane = fxmlLoader.load(loadFile("App.fxml"));
            scene = new Scene(pane);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            ErrorUtility.showDetailedError(e.getMessage(),null,e);
        }
    }
    public void initStage(Stage stage) {
        try {
            this.stage = stage;
            this.stage.getIcons().add(new Image(loadFile("logo.png")));
            this.stage.setOnCloseRequest(this::handleCloseRequest);
            showLogOn(title);
        } catch (IOException e) {
            ErrorUtility.showDetailedError(null,e.getMessage(),e);
        }
    }

    public void createNewRfcTab(TabPane tabPane) {
        try{
            fxmlLoader = new FXMLLoader();
            InputStream is = loadFile("RFC.fxml");
            BorderPane pane = fxmlLoader.load(is);
            Tab tab = new Tab("SAP RFC", pane);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }catch (Exception ex) {
            ErrorUtility.showDetailedError(null, ex.getMessage(),ex);
        }
    }

    private InputStream loadFile(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(fileName);
    }

    public void createNewDetailsTab(TabPane tabPane) {
        try{
            fxmlLoader = new FXMLLoader();
            InputStream is = loadFile("Details.fxml");
            HBox pane = fxmlLoader.load(is);
            Tab tab = new Tab("Connection", pane);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }catch (Exception ex) {
            ErrorUtility.showDetailedError(null,ex.getMessage(),ex);
        }
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(loadFile("logo.png")));
        alert.setHeaderText("Are you sure you want to exit the Application ?");Optional<ButtonType> button = alert.showAndWait();
        button.ifPresentOrElse(buttonType -> {
            if (buttonType.getButtonData().isCancelButton()){
                windowEvent.consume();
            } else {
                Platform.exit();
                System.exit(0);
            }
        }, windowEvent::consume);
    }

    public void logout() {
        try {
            File folder = new File(new File(System.getProperty("user.home")),File.separator+".saprfc");
            if (folder.exists()) {
                deleteDirectory(folder);
                folder.delete();
            }
            showLogOn(title);
        }catch (Exception e){
            ErrorUtility.showDetailedError(null,e.getMessage(),e);
        }
    }
    private void deleteDirectory(File file)
    {
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }
}
