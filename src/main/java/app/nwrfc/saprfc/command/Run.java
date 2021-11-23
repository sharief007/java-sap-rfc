package app.nwrfc.saprfc.command;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import com.google.gson.*;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import picocli.CommandLine;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@CommandLine.Command(name = "run", description = "Execute function module", mixinStandardHelpOptions = true)
public class Run implements Runnable{

    @CommandLine.Option(names = {"-i","--input"}, description = "file with input params data", required = true)
    File inputParams;

    @CommandLine.Option(names = {"-o","--output"}, description = "file with output params data")
    File outputParams;

    @CommandLine.Parameters(description = "function module")
    String functionModule;

    private final DestinationUtilities destinationUtilities = DestinationUtilities.getInstance();
    private final Map<String, JsonElement> parameters = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void run() {
        try {
            destinationUtilities.ping();
            JCoDestination destination = destinationUtilities.getDestination();
            JCoFunction function = destination.getRepository().getFunction(functionModule);
            readInputs(function);
            function.execute(destination);
            writeOutputs(function);
        } catch (JCoException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void writeOutputs(JCoFunction function) throws IOException {
        JCoParameterList exports = function.getExportParameterList();
        if (exports!=null){
            parameters.put("Exports",JsonParser.parseString(exports.toJSON()));
        }
        JCoParameterList changing = function.getChangingParameterList();
        if (changing!=null) {
            parameters.put("Changing",JsonParser.parseString(changing.toJSON()));
        }
        JCoParameterList tables = function.getTableParameterList();
        if (tables!=null) {
            parameters.put("Tables",JsonParser.parseString(tables.toJSON()));
        }
        String output = gson.toJson(parameters);
        if (outputParams!=null){
            FileWriter writer = new FileWriter(outputParams);
            writer.write(output);
            writer.flush();
            writer.close();
        } else {
            System.out.println(output);
        }
    }

    private void readInputs(JCoFunction function) throws FileNotFoundException {
        FileReader reader = new FileReader(inputParams);
        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
        JsonElement imports = object.get("Imports");
        if (imports!=null){
            function.getImportParameterList().fromJSON(imports.toString());
        }
        JsonElement changing = object.get("Changing");
        if (changing!=null) {
            function.getChangingParameterList().fromJSON(changing.toString());
        }
        JsonElement tables = object.get("Tables");
        if (tables!=null) {
            function.getTableParameterList().fromJSON(tables.toString());
        }
    }
}
