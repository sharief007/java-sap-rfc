package app.nwrfc.saprfc.jco;

import app.nwrfc.saprfc.model.KeyValueModel;
import com.sap.conn.jco.JCoAttributes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AttributesProvider {
    private JCoAttributes attributes = null;
    private static AttributesProvider provider = null;
    private  AttributesProvider(){}

    public static AttributesProvider getInstance() {
        if(provider==null){
            provider = new AttributesProvider();
        }
        return provider;
    }

    public void setAttributes(JCoAttributes attributes) {
        this.attributes = attributes;
    }

    public ObservableList<KeyValueModel> getAsKeyValueModel() {
        Properties properties = new Properties();
        properties.setProperty("Host",attributes.getHost());
        properties.setProperty("SAP Netweaver Host",attributes.getPartnerHost());
        properties.setProperty("Inet address", String.valueOf(attributes.getPartnerInetAddress()));
        properties.setProperty("System Number",attributes.getSystemNumber());
        properties.setProperty("System Id",attributes.getSystemID());
        properties.setProperty("Client",attributes.getClient());
        properties.setProperty("User",attributes.getUser());
        properties.setProperty("Language",attributes.getLanguage());
        properties.setProperty("ISO Language",attributes.getISOLanguage());
        properties.setProperty("System code page",attributes.getOwnCodepage());
        properties.setProperty("System charset",attributes.getOwnCharset());
        properties.setProperty("System Encoding",attributes.getOwnEncoding());
        properties.setProperty("Bytes per char (System)", String.valueOf(attributes.getOwnBytesPerChar()));
        properties.setProperty("Partner Code page",attributes.getPartnerCodepage());
        properties.setProperty("Partner charset",attributes.getPartnerCharset());
        properties.setProperty("Partner Encoding",attributes.getPartnerEncoding());
        properties.setProperty("Partner Bytes per char", String.valueOf(attributes.getPartnerBytesPerChar()));
        properties.setProperty("Kernel Release",attributes.getKernelRelease());
        properties.setProperty("RFC role", String.valueOf(attributes.getRfcRole()));
        properties.setProperty("CPIC Conversation Id",attributes.getCPICConversationID());
        return toObservableList(properties);
    }

    public ObservableList<KeyValueModel> getSystemDetails() {
        return toObservableList(System.getProperties());
    }

    private ObservableList<KeyValueModel> toObservableList(Properties properties) {
        List<KeyValueModel> modelList = new ArrayList<>();
        properties.forEach((k,v)->{
            KeyValueModel model = new KeyValueModel((String) k,(String) v);
            modelList.add(model);
        });
        return FXCollections.observableList(modelList);
    }
}
