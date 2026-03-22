package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IncrementVariable extends Action {

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();

    private  Action action;

    public IncrementVariable() {

    }

   /* public ChangeValueVariable(Action action1, Action action2) {

    }
    public ChangeValueVariable(Action action, int value) {
        this.name.set(action);
        this.value.set(value);
    }*/


    @Override
    public Type getType() {
        return Type.VAR_INCREMENT;
    }

    @Override
    public Action copyActionForProgram() {
        return new IncrementVariable();
    }

    @Override
    public String getRawParameter() {
        return "";
    }

    @Override
    public void setRawParameter(String text) {

    }

    @Override
    public boolean isValidParameter(String text) {
        return false;
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
        String valString = value.get() != 0 ? value.toString() + "de" : "";
        String nameString = name.get() != null ? name.get() : "";
        return "Inc/Dec varaible : " + nameString + valString;

    }
}
