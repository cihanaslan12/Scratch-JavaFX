package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Move extends Action {

    // private int distance;
    private final IntegerProperty distance = new SimpleIntegerProperty();
    private static int DEFAULT_DISTANCE = 30;

    public Move(int distance) {
        this.distance.set(distance);
    }
    @Override
    public void execute() {

    }

    @Override
    public boolean isEditable() {
        return true;
    }
    public void setDistance(int distance) {
        if (distance >= 1 && distance <= 100) {
            this.distance.set(distance);
        } else {
            throw new IllegalArgumentException("Distance is not valid !");
        }
    }
    public int getDistance() {
        return distance.get();
    }
    @Override
    public IntegerProperty parametreProperty() {
        return this.distance;
    }

    @Override
    public String unite() {
        return "pixels";
    }

    @Override
    public Commande copyCommande() {
        return new Move(DEFAULT_DISTANCE);
    }

    @Override
    public String toString() {
        return "Avancer de ";
    }

}
