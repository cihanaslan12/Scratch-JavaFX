package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import org.controlsfx.control.decoration.GraphicDecoration;

public class Teleportation extends Action {
    private final static String MIN_DISTANCE = "10";
    private final static String DEFAULT_TELE_X = "0";
    private final static String DEFAULT_TELE_Y = "0";
    private final StringProperty tele_x = new SimpleStringProperty("");
    private final StringProperty tele_y = new SimpleStringProperty("");

    public Teleportation() {
        this.tele_x.set(DEFAULT_TELE_X);
        this.tele_y.set(DEFAULT_TELE_Y);
    }

    public Teleportation(String x, String y) {
        this.tele_x.set(x);
        this.tele_y.set(y);
    }


    @Override
    public Type getType() {
        return Type.TELEPORTATION;
    }

    @Override
    public Action copyActionForProgram() {
        Teleportation copy = new Teleportation();
        copy.setRawParameter(getRawParameter());
        copy.setSecondParameter(getSecondParameter());
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return tele_x.get();
    }

    @Override
    public void setRawParameter(String text) {
        tele_x.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        if (text == null) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            return true;
        }

        return VarDeclaration.isValidName(text);
    }

    @Override
    public boolean hasTwoParameters() {
        return true;
    }

    public String getSecondParameter() {
        return tele_y.get();
    }

    public void setSecondParameter(String text) {
        tele_y.set(text);
    }

    public boolean isValidSecondParameter(String text) {
        if (text == null) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            return true;
        }

        return VarDeclaration.isValidName(text);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String unite() {
        return null;
    }

    @Override
    public String detailActionLabel() {
        return "Téléporter vers (x,y)";
    }

    @Override
    public String detailSecondActionLabel() {
        return ",";
    }

    @Override
    public String stringForSave() {
        return "TELEPORTATION;" + tele_x.get() + ";" + tele_y.get();
    }

    @Override
    public String toString() {
        if (!actionForProgram()) {
            return "Téléportation";
        }
        return "Téléportation : " + tele_x.get() + ", " + tele_y.get();
    }

    private void checkDistance(Point origin, Point destination) {
        double distance_x = Math.abs(origin.getX().get() - destination.getX().get());
        double distance_y = Math.abs(origin.getY().get() - destination.getY().get());
        if (distance_x < 10 || distance_y < 10) {
            throw new RuntimeException("Runtime Error");
        }
    }

    @Override
    public void execute(Monde monde) {
        Personnage p = monde.getPersonnage();

        int param_x = monde.resolveValue(tele_x.get());
        int param_y = monde.resolveValue(tele_y.get());

        double destination_x = monde.getWorldOriginX() + param_x;
        double destination_y = monde.getWorldOriginY() - param_y;

        Point start = p.getPosition();
        Point destination = new Point(destination_x, destination_y);

        checkDistance(start, destination);

        Point seg_start = new Point(p.getX().get(), p.getY().get());
        Point seg_destination = new Point(destination_x, destination_y);
        SegmentTeleportation telep_segment = new SegmentTeleportation(seg_start, seg_destination);
        monde.addSegment(telep_segment);
        p.moveTo(destination);
    }
}
