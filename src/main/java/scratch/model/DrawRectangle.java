package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DrawRectangle extends Action {
    private final static String MIN_SIZE = "20";
    private final static String DEFAULT_HEIGHT = "20";
    private final static String DEFAULT_WIDTH = "50";
    private final StringProperty height = new SimpleStringProperty("");
    private final StringProperty width = new SimpleStringProperty("");

    public DrawRectangle() {
        this.height.set(DEFAULT_HEIGHT);
        this.width.set(DEFAULT_WIDTH);
    }

    public DrawRectangle(String height, String width) {
        this.height.set(height);
        this.width.set(width);
    }

    @Override
    public Type getType() {
        return Type.DRAW_RECTANGLE;
    }

    @Override
    public Action copyActionForProgram() {
        DrawRectangle copy = new DrawRectangle();
        copy.setRawParameter(getRawParameter());
        copy.setSecondParameter(getSecondParameter());
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return height.get();
    }

    @Override
    public void setRawParameter(String text) {
        height.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        if (text == null) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            if (checkSize(text)) {
                return true;
            }
        }

        return VarDeclaration.isValidName(text);
    }

    @Override
    public boolean hasTwoParameters() {
        return true;
    }

    public String getSecondParameter() {
        return width.get();
    }

    public void setSecondParameter(String text) {
        width.set(text);
    }

    public boolean isValidSecondParameter(String text) {
        if (text == null) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            if (checkSize(text)) {
                return true;
            }
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
        return "Affiche un rectangle de hauteur ";
    }

    @Override
    public String detailSecondActionLabel() {
        return "et de largeur ";
    }

    @Override
    public String stringForSave() {
        return "DRAW_RECTANGLE" + width.get() + "," + height.get();
    }

    @Override
    public String toString() {
        if (!actionForProgram()) {
            return "Rectangle";
        }
        return "Rectangle : " + height.get() + "," + width.get();
    }

    private boolean checkSize(String text) {
        int param = Integer.parseInt(text);
        return param >= 20;
    }

    @Override
    public void execute(Monde monde) {
        Personnage p = monde.getPersonnage();
        int rect_height = monde.resolveValue(height.get());
        int rect_width = monde.resolveValue(width.get());
        if (rect_width < 20 || rect_height < 20) {
            throw new RuntimeException("Runtime Error");
        }

        double x_start = p.getX().get();
        double y_start = p.getY().get();
        double x_end = x_start + rect_width;
        double y_end = y_start + rect_height;

        Segment s_1 = new Segment(new Point(x_start, y_start), new Point(x_start, y_end));
        Segment s_2 = new Segment(new Point(x_start, y_end), new Point(x_end, y_end));
        Segment s_3 = new Segment(new Point(x_end, y_end), new Point(x_end, y_start));
        Segment s_4 = new Segment(new Point(x_end, y_start), new Point(x_start, y_start));

        monde.addSegment(s_1);
        monde.addSegment(s_2);
        monde.addSegment(s_3);
        monde.addSegment(s_4);
    }
}