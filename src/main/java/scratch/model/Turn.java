package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Turn extends Action{

    private final IntegerProperty angle = new SimpleIntegerProperty();
    private static final int DEFAULT_ANGLE = 90;
    private final boolean left;
    private  String detailActionLabel = "";

    public Turn( boolean left) {
            this.left = left;
            this.angle.set(DEFAULT_ANGLE);
    }
    public Turn(int angle, boolean left) {
        this.angle.set(angle);
        this.left = left;
    }

    @Override
    public void execute(Monde monde) {
        if(left) {
            monde.getPersonnage().turnLeft(angle.get());
        } else {
            monde.getPersonnage().turnRight(angle.get());
        }

    }
    @Override
    public boolean isEditable() {
        return true;
    }
    public void setAngle(int angle) {
        this.angle.set(angle);
    }
    public int getAngle() {
        return angle.get();
    }
    @Override
    public IntegerProperty parameterProperty() {
        return angle;
    }

    @Override
    public Boolean isValidparametre(int param) {
        return param >= 1 && param <= 180;
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
        return new Turn(this.getAngle(), this.left);
    }
    @Override
    public String toString() {
        if(!actionForProgram()) {
            return left ? "Tourner à gauche de " : "Tourner à droite de ";
        }
        else {

            return left ? "Tourner à gauche de " + getAngle() : "Tourner à droite de " + getAngle();
        }
    }
    @Override
    public String stringForSave() {
        return "TURN_" + (left ? "LEFT" : "RIGHT") + ";" + getAngle();
    }
}
