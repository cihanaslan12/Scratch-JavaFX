package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChangeValueVariable extends Action {

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();

    private  Action action;

    public ChangeValueVariable() {

    }

   /* public ChangeValueVariable(Action action1, Action action2) {

    }
    public ChangeValueVariable(Action action, int value) {
        this.name.set(action);
        this.value.set(value);
    }*/


    @Override
    public Action copyActionForProgram() {
        return null;
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
            return "Inc/Dec variable";
        }
        String s = value.get() != 0 ? value.toString() : name.get();
        return "Inc/Dec varaible : " + name.get() + " de " + s;

    }
}
