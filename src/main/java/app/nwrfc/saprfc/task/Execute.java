package app.nwrfc.saprfc.task;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import javafx.concurrent.Task;

public class Execute extends Task<JCoFunction> {
    private JCoFunction function;
    private final DestinationUtilities utilities;

    public Execute(JCoFunction function) {
        this.function = function;
        this.utilities = DestinationUtilities.getInstance();
    }

    @Override
    protected JCoFunction call() throws Exception {
        try {
            utilities.execute(function);
            return function;
        }catch (JCoException ex) {
            throw new RuntimeException(ex);
        }
    }
}
