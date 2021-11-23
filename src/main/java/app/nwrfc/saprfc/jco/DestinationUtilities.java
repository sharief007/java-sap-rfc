package app.nwrfc.saprfc.jco;

import com.sap.conn.jco.*;

public class DestinationUtilities {
    private JCoDestination destination = null;
    private static DestinationUtilities destinationUtilities = null;

    private DestinationUtilities() {}

    public static DestinationUtilities getInstance(){
        if (destinationUtilities==null) {
            destinationUtilities = new DestinationUtilities();
        }
        return destinationUtilities;
    }
    public JCoAttributes ping() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS1");
        destination.ping();
        this.destination = destination;
        return destination.getAttributes();
    }

    public JCoDestination getDestination() {
        return destination;
    }

    public JCoFunction execute(JCoFunction function) throws JCoException {
        function.execute(destination);
        return function;
    }

}
