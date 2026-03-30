package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {

    private final DoubleProperty xProperty = new SimpleDoubleProperty();
    private final DoubleProperty yProperty = new SimpleDoubleProperty();

    // constructeur par défault -> position centrale
    public Point() {
        this.xProperty.set(250);
        this.yProperty.set(250);
    }

    // constructeur pour le moveForward pour pouvoir ajouter une distance en double
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
