package app.nwrfc.saprfc.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class MessageUtilities {
    private Label messageLabel;
    private static MessageUtilities utilities = null;

    private MessageUtilities() {}
    public static MessageUtilities getInstance() {
        if (utilities== null) {
            utilities = new MessageUtilities();
        }
        return utilities;
    }

    public void setMessage(Label messageLabel) {
       this.messageLabel = messageLabel;
    }

    public void showSuccessMessage(String message) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        iconView.setFill(Paint.valueOf("#03fc41"));
        messageLabel.setText(message);
        messageLabel.setGraphic(iconView);
    }

    public void showErrorMessage(String message) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);
        iconView.setFill(Paint.valueOf("#fc0303"));
        messageLabel.setText(message);
        messageLabel.setGraphic(iconView);
    }

    public void showAlertMessage(String message) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE);
        iconView.setFill(Paint.valueOf("#fcd303"));
        messageLabel.setText(message);
        messageLabel.setGraphic(iconView);
    }
}
