package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Turn extends Action{
    // private final int angle;
    private final IntegerProperty angle = new SimpleIntegerProperty();

    public Turn(int angle) {
        this.angle.set(angle);
    }

    @Override
    public void execute() {

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
    public IntegerProperty parametreProperty() {
        return angle;
    }
    @Override
    public String unite() {
        return "degrés";
    }

    @Override
    public Commande copyCommande() {
        return new Turn(angle.get());
    }

    @Override
    public String toString() {
        // juste pour le test
        return (angle.get() >= 91 && angle.get() <= 179) ? "Tourner a droite de " : "Tourner a gauche de ";
        // return res + " " + angle;
    }
}
