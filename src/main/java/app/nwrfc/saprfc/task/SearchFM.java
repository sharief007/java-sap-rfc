package app.nwrfc.saprfc.task;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import javafx.concurrent.Task;

import java.util.Objects;

public class SearchFM extends Task<JCoFunction> {
    private final String fmName;
    private final JCoDestination destination;

    public SearchFM(String fmName) {
        this.fmName = fmName;
        this.destination = DestinationUtilities.getInstance().getDestination();
    }

    @Override
    protected JCoFunction call() throws Exception {
        JCoFunction function = destination.getRepository().getFunction(fmName);
        if (Objects.isNull(function)) {
            throw new NullPointerException(fmName+" not found !");
        }
        return function;
    }
}
