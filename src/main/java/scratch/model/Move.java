package scratch.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Move extends Action {

    private final StringProperty value = new SimpleStringProperty("");

    private static final String DEFAULT_DISTANCE = "30";

    public Move(String value) {
        this.value.set(value);
    }
    public Move(int value) {
        this.value.set(String.valueOf(value));
    }
    public Move() {
        this.value.set(DEFAULT_DISTANCE);
    }

    @Override
    public void execute(Monde monde) {
        int distance = monde.resolveValue(value.get());
        Segment s = monde.getPersonnage().moveForward(distance);
        monde.addSegment(s);

    }

    @Override
    public boolean isEditable() {
        return true;
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
        return "Avancer de";
    }

    @Override
    public Type getType() {
        return Type.MOVE;
    }

    @Override
    public Action copyActionForProgram() {
        Move copy = new Move(this.value.get());
        copy.setInProgram(true);
        return copy;
    }
    @Override
    public String toString() {
        if(!this.actionForProgram()) {
            return "Avancer de ";
        }
        return "Avancer de " + value.get();
    }
    @Override
    public String stringForSave() {
        return "MOVE_FORWARD;" + value.get();
    }

}
