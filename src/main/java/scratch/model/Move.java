package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Move extends Action {

    // private int distance;
    private final IntegerProperty distance = new SimpleIntegerProperty();
    private final  String detailActionLabel = "Avancer de";
    private static int DEFAULT_DISTANCE = 30;

    public Move(int distance) {
        this.distance.set(distance);
    }
    public Move() {
        this.distance.set(DEFAULT_DISTANCE);
    }
    @Override
    public void execute(Monde monde) {
        Segment s = monde.getPersonnage().moveForward(distance.get());
        monde.addSegment(s);

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
    public String detailActionLabel() {
        return detailActionLabel;
    }

    @Override
    public Action copyActionForProgram() {
        return new Move(this.getDistance());
    }
    @Override
    public String toString() {
        if(!this.actionForProgram()) {
            return "Avancer de ";
        }
        return "Avancer de " + getDistance();
    }

}
