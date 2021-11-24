package app.nwrfc.saprfc.task;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import app.nwrfc.saprfc.util.ErrorUtility;
import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.JCoException;
import javafx.concurrent.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Properties;

public class ConnectSAP extends Task<JCoAttributes> {
    private final File folder = new File(new File(System.getProperty("user.home")),File.separator+".saprfc");
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
            return destinationUtilities.ping();
        }catch (JCoException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveConnectionProperties(){
        File file = new File(folder,File.separator+"properties");
        try {
            if (!folder.exists()){
                folder.mkdirs();
            }
            FileOutputStream os = new FileOutputStream(file);
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            properties.store(data,"SAP_SYS");
            byte[] encoded = Base64.getEncoder().encode(data.toByteArray());
            os.write(encoded);
            os.flush();
            data.flush();
            data.close();
            os.close();
        } catch (Exception ex) {
            ErrorUtility.showDetailedError("Failed to save connection details",ex.getMessage(),ex);
        }
    }
}
