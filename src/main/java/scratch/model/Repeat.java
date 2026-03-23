package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Repeat extends Action {

    private String value = "" ;
    private boolean loop;

    public Repeat(boolean loop) {
        this.loop = loop;
    }

    public boolean isLoopStart() {
        return loop;
    }
    @Override
    public Type getType() {
        return loop ? Type.REPEAT : Type.END_REPEAT;
    }

    @Override
    public Action copyActionForProgram() {
        Repeat copy = new Repeat(loop);
        copy.setRawParameter(value);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return value;
    }

    @Override
    public void setRawParameter(String text) {
        this.value = text;
    }

    @Override
    public boolean isValidParameter(String text) {
        if (text == null ) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            int val = Integer.parseInt(text);
            return val > 0;
        }

        return VarDeclaration.isValidName(text);
    }
    @Override
    public boolean isEditable() {
        return loop;
    }

    @Override
    public String unite() {
        return loop ? "fois" : "";
    }

    @Override
    public String detailActionLabel() {
        return loop ? "Répeter" : "Fin répéter";
    }

    @Override
    public String stringForSave() {
        return loop ? "REPEAT;" + value : "END_REPEAT";
    }

    @Override
    public void execute(Monde monde) {

    }
    @Override
    public String toString() {
        if(!actionForProgram()) {
            return loop ? "Répéter" : "Fin répéter";
        }
        String s = value.isEmpty()  ? String.valueOf(4) : value.toString(); // Par défaut c'est 4
        return loop ? "Répéter " + s + " fois" : "Fin répéter";
    }
}
