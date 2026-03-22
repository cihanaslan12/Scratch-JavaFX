package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VarDeclaration extends Action {


    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();
    private static final int DEFAULT_VALUE = 0;

    public VarDeclaration(String name) {
        this.name.set(name);
        this.value.set(DEFAULT_VALUE);

    }
    public VarDeclaration(String name, int value) {
        this.name.set(name);
        this.value.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return name.matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    @Override
    public Type getType() {
        return Type.VAR_DECLARATION;
    }

    @Override
    public Action copyActionForProgram() {
        return new VarDeclaration(this.name.get(), DEFAULT_VALUE);
    }

    @Override
    public String getRawParameter() {
        return name.get();
    }

    @Override
    public void setRawParameter(String text) {
        this.name.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        return isValidName(text);
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
    public String detailActionLabel() {
        return "Déclaration de la variable : ";
    }

    @Override
    public String stringForSave() {
        return "VAR_DECLARATION;" + name.get();
    }

    @Override
    public void execute(Monde monde) {
        monde.declareVariable(name.get());
    }
    @Override
    public String toString() {
        if (!this.actionForProgram()) {
            return "Déclaration variable";
        }
        return "Déclaration variable " + name.get();
    }
}
