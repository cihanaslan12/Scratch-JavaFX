package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class DrawPolygon extends Action {
    private final static String DEFAULT_SIZE = "20";
    private final static String DEFAULT_NUM = "6";
    private final StringProperty size = new SimpleStringProperty("");
    private final StringProperty num = new SimpleStringProperty("");

    public DrawPolygon() {
        this.size.set(DEFAULT_SIZE);
        this.num.set(DEFAULT_NUM);
    }

    public DrawPolygon(String size, String num) {
        this.size.set(size);
        this.num.set(num);
    }

    @Override
    public Type getType() {
        return Type.DRAW_POLYGON;
    }

    @Override
    public Action copyActionForProgram() {
        DrawPolygon copy = new DrawPolygon();
        copy.setRawParameter(getRawParameter());
        copy.setSecondParameter(getSecondParameter());
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return size.get();
    }

    @Override
    public void setRawParameter(String text) {
        size.set(text);
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

    @Override
    public String getSecondParameter() {
        return num.get();
    }

    @Override
    public void setSecondParameter(String text) {
        num.set(text);
    }

    @Override
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
        return "Affiche un polygone de côté ";
    }

    @Override
    public String detailSecondActionLabel() {
        return "nombre de côtés ";
    }

    @Override
    public String stringForSave() {
        return "DRAW_POLYGON;" + size.get() + ";" + num.get();
    }

    @Override
    public String toString() {
        if (!this.actionForProgram()) {
            return "Polygone";
        }
        return "Polygone: " + size.get() + ";" + num.get();
    }

    private boolean checkSize(String text) {
        int param = Integer.parseInt(text);
        return param >= 3;
    }

    @Override
    public void execute(Monde monde) {
        int pol_size = monde.resolveValue(size.get());
        int pol_num = monde.resolveValue(num.get());
        if (pol_size < 3 || pol_num < 3) {
            throw new RuntimeException("Runtime Error");
        }

        Personnage p = monde.getPersonnage();
        List<double[]> poly = getPolygonLines(p.getX().get(), p.getY().get(), p.getAngle(), pol_size, pol_num);
        for(var l : poly) {
            var x1 = l[0];
            var y1 = l[1];
            var x2 = l[2];
            var y2 = l[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            monde.addSegment(new Segment(start, end));
        }
    }

    private static List<double[]> getPolygonLines(double x, double y, double angle, int size, int num) {
        List<double[]> lines = new ArrayList<>();

        double currentAngle = Math.toRadians(angle);
        double stepAngle = 2 * Math.PI / num;

        double currentX = x;
        double currentY = y;

        for (int i = 0; i < num; i++) {
            double nextX = currentX + size * Math.cos(currentAngle);
            double nextY = currentY + size * Math.sin(currentAngle);

            lines.add(new double[]{ currentX, currentY, nextX, nextY });

            currentX = nextX;
            currentY = nextY;

            currentAngle += stepAngle;
        }

        return lines;
    }
}
