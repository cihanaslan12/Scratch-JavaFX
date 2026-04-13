package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Turn extends Action{

    private StringProperty value = new SimpleStringProperty("");
    private static final String DEFAULT_ANGLE = "90";
    private final boolean left;

    public Turn( boolean left) {
            this.left = left;
            this.value.set(DEFAULT_ANGLE);
    }
    public Turn(int angle, boolean left) {
        this.value.set(String.valueOf(angle));
        this.left = left;
    }

    public Turn(String value, boolean left) {
        this.value.set(value);
        this.left = left;
    }
    private void checkAngle(int angle) {
        if(angle < 1 || angle > 180) {
            throw new RuntimeException("Valeur invalide pour " + (left ? "TURN_LEFT" : "TURN_RIGHT") + " : " + angle + " (doit être comprise entre 1 et 180)");
        }
    }

    @Override
    public void execute(Monde monde) {
        int angle = monde.resolveValue(value.get());
        checkAngle(angle);
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
        return left ? "Angle vers la gauche " : "Angle vers la droite";
    }

    @Override
    public Type getType() {
        return left ? Type.TURN_LEFT : Type.TURN_RIGHT;
    }

    @Override
    public Action copyActionForProgram() {
        Turn copy = new Turn(this.value.get(), this.left);
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return value.get();
    }

    @Override
    public void setRawParameter(String text) {
        this.value.set(text);
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

            return left ? "Tourner à gauche de " + value.get() : "Tourner à droite de " + value.get();
        }
    }
    @Override
    public String stringForSave() {
        return "TURN_" + (left ? "LEFT" : "RIGHT") + ";" + value.get();
    }
}
