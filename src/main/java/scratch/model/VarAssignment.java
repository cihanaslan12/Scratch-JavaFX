package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VarAssignment extends Action {

    private StringProperty nameVariable = new SimpleStringProperty();
    private StringProperty value = new SimpleStringProperty();

    public VarAssignment() {
        this.nameVariable.set("");
        this.value.set("0");
    }
    public VarAssignment(String name, String value) {
        this.nameVariable.set(name);
        this.value.set(value);

    }
    public VarAssignment(String name, Action action) {
        this.nameVariable.set(name);
        this.value.set(action.getRawParameter());
    }

    public StringProperty nameVariableProperty() {
        return nameVariable;
    }

    public StringProperty valueProperty() {
        return value;
    }

    @Override
    public boolean hasTwoParameters() {
        return true;
    }
    @Override
    public String getRawParameter() {
        return nameVariable.get();
    }

    @Override
    public void setRawParameter(String text) {
        nameVariable.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        return VarDeclaration.isValidName(text);
    }

    @Override
    public String detailActionLabel() {
        return "Assignation de la variable";
    }

    @Override
    public String stringForSave() {
        return "";
    }

    @Override
    public String getSecondParameter() {
        return value.get();
    }

    @Override
    public void setSecondParameter(String text) {
        value.set(text);
    }

    @Override
    public boolean isValidSecondParameter(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            return true;
        }

        return VarDeclaration.isValidName(text);
    }

    @Override
    public String detailSecondActionLabel() {
        return "valeur :";
    }

    @Override
    public Type getType() {
        return Type.VAR_ASSIGNMENT;
    }

    @Override
    public Action copyActionForProgram() {
        return new VarAssignment(this.nameVariable.get(),this.value.get());
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String unite() {
        return "";
    }

    @Override
    public void execute(Monde monde) {
        int val = monde.resolveValue(value.get());
        monde.setVariableValue(nameVariable.get(),val);
    }
    @Override
    public String toString() {
        if(!this.actionForProgram()) {
            return "Assignation";
        }

        return "Assignation : " + nameVariable.get() + " = " + value.get();
    }
}
