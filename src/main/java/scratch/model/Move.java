package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Move extends Action {

    private String value;
   // private final IntegerProperty distance = new SimpleIntegerProperty();
    private final  String detailActionLabel = "Avancer de";
    private static String DEFAULT_DISTANCE = "30";

    public Move(String value) {
        this.value = value;
    }
    public Move(int value) {
        this.value = String.valueOf(value);
    }
    public Move() {
        this.value = DEFAULT_DISTANCE;
    }
    public String getValue() {
        return value;
    }
    @Override
    public void execute(Monde monde) {
        int distance = monde.resolveValue(value);
        Personnage p = monde.getPersonnage();

        Point destination = p.calculDestination(distance);

        if(p.isPenDown()) {     // pas de segment si stylo levé
            Point start = new Point(p.getX().get(), p.getY().get());
            monde.addSegment(new Segment(start, destination));
        }
        p.moveTo(destination);
    }

    @Override
    public boolean isEditable() {
        return true;
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
            return val >= 1 && val <= 100;
        }

        return VarDeclaration.isValidName(text);
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
    public Type getType() {
        return Type.MOVE;
    }

    @Override
    public Action copyActionForProgram() {
        return new Move(this.value);
    }
    @Override
    public String toString() {
        if(!this.actionForProgram()) {
            return "Avancer de ";
        }
        return "Avancer de " + value;
    }
    @Override
    public String stringForSave() {
        return "MOVE_FORWARD;" + value;
    }

}
