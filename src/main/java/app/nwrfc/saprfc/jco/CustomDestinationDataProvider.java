package app.nwrfc.saprfc.jco;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CustomDestinationDataProvider implements DestinationDataProvider {
    private final File propsFile = new File("./lib/properties.xml");
    @Override
    public Properties getDestinationProperties(String s) throws DataProviderException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propsFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener destinationDataEventListener) {

    }
}
