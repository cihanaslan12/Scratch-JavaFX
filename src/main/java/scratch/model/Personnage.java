package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Personnage {
    private final double DEFAULT_ANGLE = 0.0;
    private final DoubleProperty angle = new SimpleDoubleProperty();
    private Point position;
    private boolean penDown;

    public Personnage() {
        this.angle.set(DEFAULT_ANGLE);  // angle par défaut à 0°
        this.position = new Point();    // position par défaut à x=0,y=0
        this.penDown = true;            // stylo abaissé par défaut
    }

    public DoubleProperty angleProperty() {
        return angle;
    }
    public double getAngle() {
        return angle.get();
    }
    public Point getPosition() {
        return position;
    }
    public DoubleProperty getX() {
        return position.getX();
    }
    public DoubleProperty getY() {
        return position.getY();
    }

    public boolean isPenDown() {
        return penDown;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setAngle(double angle) {
        this.angle.set(angle);
    }

    public void penUp() {
        penDown = false;
    }
    public void penDown() {
        penDown = true;
    }

    public void turnLeft(int degrees) {
        angle.set(getAngle() + degrees);
    }

    public void turnRight(int degrees) {
        angle.set(getAngle() - degrees);
    }

    public Segment moveForward(int distance) {
        // angle étant exprimé en degré, on le transforme en radians et on ajoute 90° (Math.PI / 2)
        double radians = Math.toRadians(getAngle()) + Math.PI / 2;
        // Par rapport à la position actuelle de la tortue, on calcule alors les différences en X et en Y de la manière suivante :
        double diffX = distance * Math.cos(radians);
        double diffY = distance * Math.sin(radians);

        double newX = position.getX().get() + diffX;
        double newY = position.getY().get() - diffY;

        Point start = new Point(position.getX().get(), position.getY().get());
        position.getX().set(newX);
        position.getY().set(newY);
        Point end = new Point(newX,newY);

        if(penDown) {
            return new Segment(start, end);
        }

        return null; // pas de segment si stylo levé
    }
}
