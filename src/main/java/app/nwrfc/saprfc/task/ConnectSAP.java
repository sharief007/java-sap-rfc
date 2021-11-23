package app.nwrfc.saprfc.task;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import app.nwrfc.saprfc.util.ErrorUtility;
import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.JCoException;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConnectSAP extends Task<JCoAttributes> {
    private final Properties properties;
    private final DestinationUtilities destinationUtilities;

    public ConnectSAP(Properties properties) {
        this.properties = properties;
        this.destinationUtilities = DestinationUtilities.getInstance();
    }

    @Override
    protected JCoAttributes call() {
        try {
            saveConnectionProperties();
            System.out.println("properties saved");
            return destinationUtilities.ping();
        }catch (JCoException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveConnectionProperties(){
        try {
            File destinationProps = new File("./lib/properties.xml");
            FileOutputStream os = new FileOutputStream(destinationProps);
            properties.store(os,"ABAP_AS1");
        } catch (Exception ex) {
            ErrorUtility.showDetailedError("Failed to save connection details",ex.getMessage(),ex);
        }
    }
}
