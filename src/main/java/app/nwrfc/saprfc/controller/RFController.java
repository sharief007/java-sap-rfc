package app.nwrfc.saprfc.controller;

import app.nwrfc.saprfc.model.ParameterTableModel;
import app.nwrfc.saprfc.task.Execute;
import app.nwrfc.saprfc.task.SearchFM;
import app.nwrfc.saprfc.util.ErrorUtility;
import app.nwrfc.saprfc.util.MessageUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RFController implements Initializable {

    @FXML private TableColumn<ParameterTableModel, String> dataTypeColumn;
    @FXML private TextField fmName;
    @FXML private TableColumn<ParameterTableModel, String> nameColumn;
    @FXML private TableView<ParameterTableModel> parametersTable;
    @FXML private Button searchBtn;
    @FXML private TableColumn<ParameterTableModel, Boolean> structureColumn;
    @FXML private TableColumn<ParameterTableModel, Boolean> tableColumn;
    @FXML private TableColumn<ParameterTableModel, String> typeColumn;
    @FXML private TextArea importsInput;
    @FXML private TextArea changingInput;
    @FXML private TextArea tablesInput;
    @FXML private TextArea exportsOutput;
    @FXML private TextArea changingOutput;
    @FXML private TextArea tablesOutput;
    @FXML private Button executeBtn;
//    @FXML private RadioButton json;
//    @FXML private RadioButton xml;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private ObservableList<ParameterTableModel> functionParameters = null;
    private JCoFunction function = null;
//    private final ToggleGroup group = new ToggleGroup();
    private final MessageUtilities messageUtilities = MessageUtilities.getInstance();

    @FXML
    public void searchFM() {
        String fm = fmName.getText().trim();
        if (!fm.isEmpty()) {
            SearchFM search = new SearchFM(fm);
            search.setOnRunning(e1-> searchBtn.setDisable(true));
            search.setOnCancelled(e2->searchBtn.setDisable(false));
            search.setOnFailed(e3-> {
                messageUtilities.showErrorMessage(fm+" does not exist");
                searchBtn.setDisable(false);
            });
            search.setOnSucceeded(e4-> onSearchSuccess(search));
            ExecutorService service = Executors.newCachedThreadPool();
            service.submit(search);
            service.shutdown();
        }
    }

    private void onSearchSuccess(Task<JCoFunction> task) {
        List<ParameterTableModel> params = new ArrayList<>();
        function = task.getValue();
        params.addAll(getImportParameters(function));
        params.addAll(getExportParameters(function));
        params.addAll(getChangingParameters(function));
        params.addAll(getTableParameters(function));
        functionParameters = FXCollections.observableList(params);
        Platform.runLater(()-> {
            setInputs(function);
            searchBtn.setDisable(false);
            parametersTable.setItems(functionParameters);
        });
    }

    private void setInputs(JCoFunction function) {
        JCoParameterList imports = function.getImportParameterList();
        JCoParameterList changing = function.getChangingParameterList();
        JCoParameterList tables = function.getTableParameterList();
        if (imports != null) {
//            if (json.isSelected()) {
                importsInput.setText(imports.toJSON());
//            } else {
//                importsInput.setText(imports.toXML());
//            }
        } else {
            importsInput.setText("");
        }
        if (changing != null) {
//            if (json.isSelected()) {
                changingInput.setText(changing.toJSON());
//            } else {
//                changingInput.setText(changing.toXML());
//            }
        } else {
            changingInput.setText("");
        }
        if (tables!=null){
//            if (json.isSelected()) {
                tablesInput.setText(tables.toJSON());
//            } else {
//                tablesInput.setText(tables.toXML());
//            }
        } else {
            tablesInput.setText("");
        }
    }

    private List<ParameterTableModel> getTableParameters(JCoFunction function) {
        List<ParameterTableModel> tableParams = new ArrayList<>();
        final String type = "TABLE";
        JCoParameterList tables = function.getTableParameterList();
        if (tables!=null) {
            for (JCoField table : tables) {
                ParameterTableModel model = new ParameterTableModel();
                model.setFieldName(table.getName());
                model.setFieldType(type);
                model.setDataType(table.getTypeAsString());
                model.setIsTable(table.isTable());
                model.setIsStructure(table.isStructure());
                tableParams.add(model);
            }
        }
        return tableParams;
    }

    private List<ParameterTableModel> getChangingParameters(JCoFunction function) {
        List<ParameterTableModel> changingParams = new ArrayList<>();
        final String type = "CHANGING";
        JCoParameterList changing = function.getChangingParameterList();
        if (changing!=null) {
            for (JCoField jCoField : changing) {
                ParameterTableModel model = new ParameterTableModel();
                model.setFieldName(jCoField.getName());
                model.setFieldType(type);
                model.setDataType(jCoField.getTypeAsString());
                model.setIsTable(jCoField.isTable());
                model.setIsStructure(jCoField.isStructure());
                changingParams.add(model);
            }
        }
        return changingParams;
    }

    private List<ParameterTableModel> getExportParameters(JCoFunction function) {
        List<ParameterTableModel> exportParams = new ArrayList<>();
        final String type = "EXPORT";
        JCoParameterList exports = function.getExportParameterList();
        if (exports!=null) {
            for (JCoField export : exports) {
                ParameterTableModel model = new ParameterTableModel();
                model.setFieldName(export.getName());
                model.setFieldType(type);
                model.setDataType(export.getTypeAsString());
                model.setIsTable(export.isTable());
                model.setIsStructure(export.isStructure());
                exportParams.add(model);
            }
        }
        return exportParams;
    }

    private List<ParameterTableModel> getImportParameters(JCoFunction function) {
        List<ParameterTableModel> importParams = new ArrayList<>();
        final String type = "IMPORT";
        JCoParameterList imports = function.getImportParameterList();
        if (imports!=null) {
            for (JCoField anImport : imports) {
                ParameterTableModel model = new ParameterTableModel();
                model.setFieldName(anImport.getName());
                model.setFieldType(type);
                model.setDataType(anImport.getTypeAsString());
                model.setIsTable(anImport.isTable());
                model.setIsStructure(anImport.isStructure());
                importParams.add(model);
            }
        }
        return importParams;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        initToggleGroup();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fieldName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("fieldType"));
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("dataType"));
        tableColumn.setCellValueFactory(f-> f.getValue().isTableProperty());
        structureColumn.setCellValueFactory(f-> f.getValue().isStructureProperty());
        tableColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
        structureColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

       ContextMenu menu = new ContextMenu();
       MenuItem item = new MenuItem("format");
       menu.getItems().add(item);
       
    }

    @FXML
    public void execute() {
        if (function!=null) {
            readInputs();
            Execute execute = new Execute(function);
            execute.setOnRunning(e1->executeBtn.setDisable(true));
            execute.setOnFailed(e2->{
                JCoException ex = (JCoException) execute.getException();
                Platform.runLater(()->{
                    ErrorUtility.showDetailedError(null,ex.getMessage(),ex);
                    executeBtn.setDisable(false);
                });
            });
            execute.setOnSucceeded(e3->{
                function = execute.getValue();
                setOutputs(function);
            });
            ExecutorService service = Executors.newCachedThreadPool();
            service.submit(execute);
            service.shutdown();
        }
    }

    private void readInputs() {
        if (!importsInput.getText().trim().isEmpty()){
//            if (json.isSelected()){
                function.getImportParameterList().fromJSON(importsInput.getText().trim());
//            }
        }
        if (!changingInput.getText().trim().isEmpty()){
            function.getChangingParameterList().fromJSON(changingInput.getText().trim());
        }if (!tablesInput.getText().trim().isEmpty()) {
            function.getTableParameterList().fromJSON(tablesInput.getText().trim());
        }
    }

    private void setOutputs(JCoFunction function) {
        JCoParameterList exports = function.getExportParameterList();
        JCoParameterList changing = function.getChangingParameterList();
        JCoParameterList tables = function.getTableParameterList();
        Platform.runLater(()->{
            executeBtn.setDisable(false);
            if (exports!=null){
//                if (xml.isSelected()) {
//                    exportsOutput.setText(exports.toXML());
//                } else {
                    exportsOutput.setText(gson.toJson(JsonParser.parseString(exports.toJSON())));
//                }
            }
            if (changing!=null) {
//                if (xml.isSelected()) {
//                    changingOutput.setText(changing.toXML());
//                } else {
                    changingOutput.setText(changing.toJSON());
//                }
            }
            if (tables!=null) {
//                if (xml.isSelected()) {
//                    tablesOutput.setText(tables.toXML());
//                } else {
                    tablesOutput.setText(tables.toJSON());
//                }
            }
        });
    }
//    private void initToggleGroup() {
//        json.setToggleGroup(group);
//        xml.setToggleGroup(group);
//        json.setSelected(true);
//    }
}
