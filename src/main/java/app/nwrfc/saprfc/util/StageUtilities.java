package app.nwrfc.saprfc.util;

import app.nwrfc.saprfc.controller.AppController;
import com.sap.conn.jco.JCoAttributes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class StageUtilities {
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Stage stage;
    private static StageUtilities utilities = null;

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
            Parent root = fxmlLoader.load(is);
            scene = new Scene(root);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void showApplication(String title)  {
        try {
            if (Objects.isNull(title)) {
                title = stage.getTitle();
            }
            fxmlLoader = new FXMLLoader();
            BorderPane pane = fxmlLoader.load(loadFile("App.fxml"));
            scene = new Scene(pane);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            ErrorUtility.showDetailedError(e.getMessage(),null,e);
        }
    }
    public void initStage(Stage stage) {
        try {
            this.stage = stage;
            String title = "SAP RFC Client";
            showLogOn(title);
        } catch (IOException e) {
            ErrorUtility.showDetailedError(e.getMessage(),null,e);
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
}
