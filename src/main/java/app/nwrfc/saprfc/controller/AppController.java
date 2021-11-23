package app.nwrfc.saprfc.controller;

import app.nwrfc.saprfc.util.MessageUtilities;
import app.nwrfc.saprfc.util.StageUtilities;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private final StageUtilities utilities;
    private final MessageUtilities messageUtilities = MessageUtilities.getInstance();

//    @FXML
//    private FontAwesomeIconView messageIcon;

    @FXML
    private Label messageLabel;

    @FXML
    private Label systemLabel;

    @FXML
    private TabPane tabPane;


    public AppController() {
        this.utilities = StageUtilities.getInstance();
    }

    @FXML
    void about(ActionEvent event) {

    }

    @FXML
    void closeAllTabs(ActionEvent event) {
        tabPane.getTabs().clear();
    }

    @FXML
    void closeConnection(ActionEvent event) {

    }

    @FXML
    void closeTab(ActionEvent event) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab!=null) {
            tabPane.getTabs().remove(tab);
        }
    }

    @FXML
    void newRfcClient(ActionEvent event) {
        utilities.createNewRfcTab(tabPane);
    }

    @FXML
    void preferences(ActionEvent event) {

    }

    @FXML
    void quit(ActionEvent event) {

    }

    @FXML
    void showDetails(ActionEvent event) {
        utilities.createNewDetailsTab(tabPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageUtilities.setMessage(messageLabel);
        utilities.createNewDetailsTab(tabPane);
    }
}
