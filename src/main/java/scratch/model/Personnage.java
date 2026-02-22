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

    public boolean isPenDown() {
        return penDown;
    }

    public void penUp() {
        penDown = false;
    }
    public void penDown() {
        penDown = true;
    }

    public void turnLeft(int degrees) {
        angle -= degrees;

    }

    public void turnRight(int degrees) {
        angle += degrees;

    }

    public Segment moveForward(int distance) {
        // angle étant exprimé en degré, on le transforme en radians et on ajoute 90° (Math.PI / 2)
        double radians = Math.toRadians(angle) + Math.PI / 2;
        // Par rapport à la position actuelle de la tortue, on calcule alors les différences en X et en Y de la manière suivante :
        double diffX = distance * Math.cos(radians);
        double diffY = distance * Math.sin(radians);

        Point start = position;
        Point end = new Point(start.getX() + diffX, start.getY() + diffY);

        position = end;

        if(penDown) {
            return new Segment(start,end);
        }

        return null; // pas de segment si stylo levé
    }
}
