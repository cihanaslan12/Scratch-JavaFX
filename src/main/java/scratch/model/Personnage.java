package scratch.model;

public class Personnage {

    private int angle;
    private Point position;

    public Personnage(int angle, Point position) {
        this.angle = angle;
        this.position = position;
    }

    public boolean pen(boolean ecrit) {
        return ecrit;
    }

    public void turnLeft(int angle) {
        this.angle = angle;
    }

    public void turnRight(int angle) {
        this.angle = angle;

    }

    public void moveForward(int distance) {


    }
}
