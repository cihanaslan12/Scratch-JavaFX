package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {

    private final DoubleProperty xProperty = new SimpleDoubleProperty();
    private final DoubleProperty yProperty = new SimpleDoubleProperty();

    public Point() {
    }

    public Point(double x, double y) {
        this.xProperty.set(x);
        this.yProperty.set(y);
    }

    public DoubleProperty getX() {
        return xProperty;
    }

    public DoubleProperty getY() {
        return yProperty;
    }

}
