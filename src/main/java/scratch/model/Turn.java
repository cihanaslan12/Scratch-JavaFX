package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Turn extends Action{

   // private final IntegerProperty angle = new SimpleIntegerProperty();
    private String value;
    private static final String DEFAULT_ANGLE = "90";
    private final boolean left;
    private  String detailActionLabel = "";

    public Turn( boolean left) {
            this.left = left;
            this.value = DEFAULT_ANGLE;
    }
    public Turn(int angle, boolean left) {
        this.value = String.valueOf(angle);
        this.left = left;
    }

    public Turn(String value, boolean left) {
        this.value = value;
        this.left = left;
    }

    @Override
    public void execute(Monde monde) {
        int angle = monde.resolveValue(value);
        if(left) {
            monde.getPersonnage().turnLeft(angle);
        } else {
            monde.getPersonnage().turnRight(angle);
        }

    }
    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String unite() {
        return "degrés";
    }

    @Override
    public String detailActionLabel() {
        detailActionLabel = left ? "Angle vers la gauche " : "Angle vers la droite";
        return detailActionLabel;
    }

    @Override
    public Type getType() {
        return Type.TURN;
    }

    @Override
    public Action copyActionForProgram() {
        return new Turn(value, this.left);
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
        if (text == null || text.isBlank()) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            int val = Integer.parseInt(text);
            return val >= 1 && val <= 180;
        }

        return VarDeclaration.isValidName(text);
    }


    @Override
    public String toString() {
        if(!actionForProgram()) {
            return left ? "Tourner à gauche de " : "Tourner à droite de ";
        }
        else {

            return left ? "Tourner à gauche de " + value : "Tourner à droite de " + value;
        }
    }
    @Override
    public String stringForSave() {
        return "TURN_" + (left ? "LEFT" : "RIGHT") + ";" + value;
    }
}
