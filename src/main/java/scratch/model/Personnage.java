package scratch.model;

public class Personnage {

    private int angle;
    private Point position;
    private boolean penDown;

    public Personnage(int angle, Point position) {
        this.angle = angle;
        this.position = position;
        this.penDown = true; // stylo abaissé par défaut
    }
    public int getAngle() {
        return angle;
    }
    public Point getPosition() {
        return position;
    }
    public double getX() {
        return position.getX();
    }
    public double getY() {
        return position.getY();
    }

    public boolean isPenDown() {
        return penDown;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void penUp() {
        penDown = false;
    }
    public void penDown() {
        penDown = true;
    }

    public void turnLeft(int degrees) {
        angle += degrees;

    }

    public void turnRight(int degrees) {
        angle -= degrees;

    }

    public Segment moveForward(int distance) {
        // angle étant exprimé en degré, on le transforme en radians et on ajoute 90° (Math.PI / 2)
        double radians = Math.toRadians(angle) + Math.PI / 2;
        // Par rapport à la position actuelle de la tortue, on calcule alors les différences en X et en Y de la manière suivante :
        double diffX = distance * Math.cos(radians);
        double diffY = distance * Math.sin(radians);

        double newX = position.getX() + diffX;
        double newY = position.getY() - diffY;

        Point start = position;
        Point newPos = new Point(newX,newY);

        position = newPos;

        if(penDown) {
            return new Segment(start, newPos);
        }

        return null; // pas de segment si stylo levé
    }
}
