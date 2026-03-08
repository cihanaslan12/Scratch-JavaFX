package scratch.model;

import javafx.beans.property.IntegerProperty;

public class Pen extends Action {

    private final boolean styloDown; // true = abaisser, false = lever
    private String detailActionLabel = "";


    public Pen(boolean styloDown) {
        this.styloDown = styloDown;
    }

    public boolean isStyloDown() {
        return styloDown;
    }

    @Override
    public void execute(Monde monde) {
        if(styloDown) {
            monde.getPersonnage().penDown();
        } else {
            monde.getPersonnage().penUp();
        }

    }

    @Override
    public boolean isEditable() {
        return false;
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
    public String unite() {
        return "";
    }

    @Override
    public String detailActionLabel() {
        detailActionLabel = this.toString();
        return detailActionLabel;
    }

    @Override
    public Action copyActionForProgram() {
        return new Pen(this.styloDown);
    }
    @Override
    public String toString() {
        return styloDown ? "Abaisser le stylo" : "Lever le stylo";
    }
}
