package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Repeat extends Action {

    private boolean loop;
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();


    public Repeat(boolean loop) {
        this.loop = loop;
    }

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
        if(!actionForProgram()) {
            return loop ? "Répéter" : "Fin répéter";
        }
        String s = value.get() != 0 ? value.toString() : name.get();
        return loop ? "Répéter " + s + " fois" : "Fin répéter";
    }
}
