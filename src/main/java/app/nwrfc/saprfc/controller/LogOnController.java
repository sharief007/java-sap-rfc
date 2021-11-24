package app.nwrfc.saprfc.controller;

import app.nwrfc.saprfc.jco.AttributesProvider;
import app.nwrfc.saprfc.task.ConnectSAP;
import app.nwrfc.saprfc.util.ErrorUtility;
import app.nwrfc.saprfc.util.StageUtilities;
import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.ext.DestinationDataProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogOnController implements Initializable {

    @FXML private TextField client;
    @FXML private TextField hostAddress;
    @FXML private Button loginButton;
    @FXML private PasswordField password;
    @FXML private TextField systemNumber;
    @FXML private TextField username;

    private final Properties properties = new Properties();

    @FXML
    void login() {
        properties.setProperty(DestinationDataProvider.JCO_ASHOST,hostAddress.getText().trim());
        properties.setProperty(DestinationDataProvider.JCO_SYSNR,systemNumber.getText().trim());
        properties.setProperty(DestinationDataProvider.JCO_CLIENT,client.getText().trim());
        properties.setProperty(DestinationDataProvider.JCO_USER,username.getText().trim());
        properties.setProperty(DestinationDataProvider.JCO_PASSWD,password.getText());

        ConnectSAP connectSAP = new ConnectSAP(properties);
        connectSAP.setOnSucceeded((e1)->{
            JCoAttributes attributes = connectSAP.getValue();
            AttributesProvider.getInstance().setAttributes(attributes);
            Platform.runLater(()->StageUtilities.getInstance().showApplication());
        });
        connectSAP.setOnRunning((e2)-> Platform.runLater(()->loginButton.setDisable(true)));
        connectSAP.setOnCancelled(e3-> Platform.runLater(()->loginButton.setDisable(false)));
        connectSAP.setOnFailed(e4->{
           Platform.runLater(()->loginButton.setDisable(false));
           if (connectSAP.getException() instanceof RuntimeException ex) {
               ErrorUtility.showDetailedError("Failed to login", null, ex);
           }
        });
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(connectSAP);
        service.shutdown();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initConnectionProperties();

        hostAddress.textProperty().addListener((listener) -> controlLogOn());
        username.textProperty().addListener(listener-> controlLogOn());
        password.textProperty().addListener(listener->controlLogOn());
        client.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                client.setText(t1.replaceAll("[^\\d]", ""));
            }
            controlLogOn();
        });
        systemNumber.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                systemNumber.setText(t1.replaceAll("[^\\d]", ""));
            }
            controlLogOn();
        });
    }

    private void initConnectionProperties() {
        properties.setProperty(DestinationDataProvider.JCO_REPOSITORY_ROUNDTRIP_OPTIMIZATION,"1");
        properties.setProperty(DestinationDataProvider.JCO_SERIALIZATION_FORMAT,"columnBased");
        properties.setProperty(DestinationDataProvider.JCO_NETWORK,"lan");
        properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,"10");
        properties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME,"200000");
        properties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD,"120000");
    }

    private void controlLogOn() {
        boolean host = hostAddress.getText().trim().isEmpty();
        boolean uname = username.getText().trim().isEmpty();
        boolean pass = password.getText().trim().isEmpty();
        boolean cli = client.getText().trim().isEmpty();
        boolean num = systemNumber.getText().trim().isEmpty();
        loginButton.setDisable(host || uname || pass || cli || num);
    }

}
