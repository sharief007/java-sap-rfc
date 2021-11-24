package app.nwrfc.saprfc.jco;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.*;
import java.util.Base64;
import java.util.Properties;

public class CustomDestinationDataProvider implements DestinationDataProvider {
    private final File folder = new File(new File(System.getProperty("user.home")),File.separator+".saprfc");

    private InputStream readProperties() throws FileNotFoundException {
        if (!folder.exists()){
            throw new FileNotFoundException("No user data found");
        } else {
            File file = new File(folder,File.separator+"properties");
            return new FileInputStream(file);
        }
    }

    @Override
    public Properties getDestinationProperties(String connectionKey) throws DataProviderException {
        try {
            Properties properties = new Properties();
            InputStream is = readProperties();
            byte[] data = Base64.getDecoder().decode(is.readAllBytes());
            properties.load(new ByteArrayInputStream(data));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener destinationDataEventListener) {

    }
}
