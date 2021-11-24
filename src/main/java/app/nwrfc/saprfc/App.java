package app.nwrfc.saprfc;

import app.nwrfc.saprfc.command.Get;
import app.nwrfc.saprfc.command.Login;
import app.nwrfc.saprfc.command.Run;
import app.nwrfc.saprfc.jco.CustomDestinationDataProvider;
import app.nwrfc.saprfc.util.StageUtilities;
import com.sap.conn.jco.ext.Environment;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import picocli.CommandLine;

@CommandLine.Command(name = "sap-rfc", description = "SAP RFC Client",
        mixinStandardHelpOptions = true, subcommands = {Login.class, Get.class, Run.class})
public class App extends Application implements Runnable{

    static {
        final CustomDestinationDataProvider dataProvider = new CustomDestinationDataProvider();
        Environment.registerDestinationDataProvider(dataProvider);
    }

    private StageUtilities utilities;
    @Override
    public void init() throws Exception {
        super.init();
        utilities = StageUtilities.getInstance();
    }

    @Override
    public void start(Stage stage) {
        utilities.initStage(stage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        CommandLine cli = new CommandLine(new App());
        int exitCode = cli.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        launch();
    }
}