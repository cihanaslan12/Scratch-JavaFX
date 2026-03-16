package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Variable extends Action {
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();
    private static final int DEFAULT_VALUE = 0;

    public Variable(String name) {
        this.name.set(name);

    }
    public Variable(String name,int value) {
        this.value.set(value);
        this.name.set(name);
    }


    @Override
    public Action copyActionForProgram() {
        return new Variable(this.name.get());
    }

    @Override
    public IntegerProperty parameterProperty() {
        return null;
    }

    @Override
    public Boolean isValidparametre(int param) {
        return null;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public String unite() {
        return "";
    }

    @Override
    public String detailActionLabel() {
        return "";
    }

    @Override
    public String stringForSave() {
        return "";
    }

    @Override
    public void execute(Monde monde) {

    }
    @Override
    public String toString() {
        return "Déclaration variable" + name.get();
    }
}
