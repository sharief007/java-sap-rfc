module app.nwrfc.saprfc {
    requires javafx.controls;
    requires javafx.fxml;

    requires de.jensd.fx.glyphs.fontawesome;
    requires sapjco3;

    requires info.picocli;
    requires com.google.gson;

    opens app.nwrfc.saprfc to javafx.fxml;
    opens app.nwrfc.saprfc.controller to javafx.fxml;
    opens app.nwrfc.saprfc.model to javafx.base, com.google.gson;
    opens app.nwrfc.saprfc.command to info.picocli;

    exports app.nwrfc.saprfc;
}