package app.nwrfc.saprfc.command;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Command(name = "get", description = "Get SAP RFC parameters", mixinStandardHelpOptions = true)
public class Get implements Runnable{

    @Parameters(description = "function module")
    String functionModule;

    @CommandLine.Option(names = {"-f","--file"},description = "file name to store import parameters", defaultValue = "parameters.json")
    File file;

    private final DestinationUtilities destinationUtilities = DestinationUtilities.getInstance();
    private final Map<String, JsonElement> parameters = new HashMap<>();
    private final Gson gson = new Gson();
    @Override
    public void run() {
       try {
           destinationUtilities.ping();
           JCoDestination destination = destinationUtilities.getDestination();
           JCoFunction function = destination.getRepository().getFunction(functionModule);
           JCoParameterList imports = function.getImportParameterList(), changing = function.getChangingParameterList(), tables = function.getTableParameterList();
           if (imports!=null) {
               JsonElement element = JsonParser.parseString(imports.toJSON());
               parameters.put("Imports", element);
           } if (changing!=null){
               JsonElement element = JsonParser.parseString(changing.toJSON());
               parameters.put("Changing",element);
           } if (tables!=null) {
               JsonElement element = JsonParser.parseString(tables.toJSON());
               parameters.put("Tables",element);
           }
           FileWriter writer = new FileWriter(file);
           writer.write(gson.toJson(parameters));
           System.out.println("Parameters saved to "+ file.getPath());
           writer.flush();
           writer.close();
       } catch (JCoException | IOException ex){
           System.err.println(ex.getMessage());
       }
    }
}
