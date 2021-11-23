package app.nwrfc.saprfc.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ParameterTableModel {
    SimpleStringProperty fieldName, fieldType, dataType;
    SimpleBooleanProperty isTable, isStructure;

    public ParameterTableModel() {
        this.fieldName = new SimpleStringProperty();
        this.fieldType = new SimpleStringProperty();
        this.dataType = new SimpleStringProperty();
        this.isTable = new SimpleBooleanProperty();
        this.isStructure = new SimpleBooleanProperty();
    }

    public String getFieldName() {
        return fieldName.get();
    }

    public SimpleStringProperty fieldNameProperty() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName.set(fieldName);
    }

    public String getFieldType() {
        return fieldType.get();
    }

    public SimpleStringProperty fieldTypeProperty() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType.set(fieldType);
    }

    public String getDataType() {
        return dataType.get();
    }

    public SimpleStringProperty dataTypeProperty() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType.set(dataType);
    }

    public boolean isIsTable() {
        return isTable.get();
    }

    public SimpleBooleanProperty isTableProperty() {
        return isTable;
    }

    public void setIsTable(boolean isTable) {
        this.isTable.set(isTable);
    }

    public boolean isIsStructure() {
        return isStructure.get();
    }

    public SimpleBooleanProperty isStructureProperty() {
        return isStructure;
    }

    public void setIsStructure(boolean isStructure) {
        this.isStructure.set(isStructure);
    }
}
