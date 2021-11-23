package app.nwrfc.saprfc.controller;

import app.nwrfc.saprfc.jco.AttributesProvider;
import app.nwrfc.saprfc.model.KeyValueModel;
import app.nwrfc.saprfc.util.MessageUtilities;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {
    private ObservableList<KeyValueModel> connectionDetailsList = null;
    private ObservableList<KeyValueModel> systemDetailsList = null;

    private final AttributesProvider attributesProvider = AttributesProvider.getInstance();
    private final MessageUtilities messageUtilities = MessageUtilities.getInstance();
    @FXML
    private TableView<KeyValueModel> connectionDetails;

    @FXML
    private TableColumn<KeyValueModel, String> keyColumn;

    @FXML
    private TableColumn<KeyValueModel, String> propNameColumn;

    @FXML
    private TableColumn<KeyValueModel, String> propValueColumn;

    @FXML
    private TableView<KeyValueModel> systemDetails;

    @FXML
    private TableColumn<KeyValueModel, String> valueColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        systemDetailsList = attributesProvider.getSystemDetails();
        connectionDetailsList= attributesProvider.getAsKeyValueModel();
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        propNameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        propValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        connectionDetails.setItems(connectionDetailsList);
        systemDetails.setItems(systemDetailsList);

        messageUtilities.showSuccessMessage("Successfully connected !");
    }
}
