package app.nwrfc.saprfc.command;

import app.nwrfc.saprfc.jco.DestinationUtilities;
import app.nwrfc.saprfc.util.ErrorUtility;
import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.ext.DestinationDataProvider;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.*;
import java.util.Base64;
import java.util.Properties;

@Command(name = "login", description = "SAP Logon")
public class Login implements Runnable{

    private final File folder = new File(new File(System.getProperty("user.home")),File.separator+".saprfc");

    @Option(names = {"-h","--host"}, description = "SAP Netweaver host address", required = true)
    String hostAddress;
    @Option(names = {"-n","--system-number"}, description = "System Number", required = true)
    String systemNum;
    @Option(names = {"-c","--client"}, description = "Client Number", required = true)
    String client;
    @Option(names = {"-u","--username"}, description = "SAP username", required = true)
    String username;
    @Option(names = {"-p","--password"}, description = "SAP Password", interactive = true, required = true)
    String password;

    private final Properties properties = new Properties();
    private final DestinationUtilities destinationUtilities = DestinationUtilities.getInstance();

    @Override
    public void run() {
        initConnectionProperties();
        try {
            saveConnectionProperties();
            JCoAttributes attributes = destinationUtilities.ping();
            System.out.println(Ansi.AUTO.string("@|bold,yellow Login Successful !|@"));
            System.out.println(attributes);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void saveConnectionProperties() throws IOException {
        File file = new File(folder,File.separator+"properties");
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
    }

    private void initConnectionProperties() {
        properties.setProperty(DestinationDataProvider.JCO_ASHOST,hostAddress);
        properties.setProperty(DestinationDataProvider.JCO_SYSNR,systemNum);
        properties.setProperty(DestinationDataProvider.JCO_CLIENT,client);
        properties.setProperty(DestinationDataProvider.JCO_USER,username);
        properties.setProperty(DestinationDataProvider.JCO_PASSWD,password);
        properties.setProperty(DestinationDataProvider.JCO_REPOSITORY_ROUNDTRIP_OPTIMIZATION,"1");
        properties.setProperty(DestinationDataProvider.JCO_SERIALIZATION_FORMAT,"columnBased");
        properties.setProperty(DestinationDataProvider.JCO_NETWORK,"lan");
        properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,"10");
        properties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME,"200000");
        properties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD,"120000");
    }
}
