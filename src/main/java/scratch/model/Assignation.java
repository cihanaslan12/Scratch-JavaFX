package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Assignation extends Action {

    private StringProperty nameVariable = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();

    public Assignation() {
        this.nameVariable.set("");
        this.value.set(0);
    }
    public Assignation(String name,int value) {
        this.nameVariable.set(name);
        this.value.set(value);

    }
    public Assignation(String name, Action action) {
        this.nameVariable.set(name);
        this.value.set(action.parameterProperty().get());
    }

    @Override
    public Action copyActionForProgram() {
        return new Assignation(this.nameVariable.get(),this.value.get());
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
        if(!this.actionForProgram()) {
            return "Assignation";
        }
        String s = value.get() != 0 ? value.toString() : nameVariable.get();
        String symbol = nameVariable.toString().isEmpty() ? "" : " = ";
        return "Assignation : " + nameVariable.get() + symbol + s;
    }
}
