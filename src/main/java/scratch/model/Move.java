package scratch.model;

public class Move extends Action {

    private int distance;
    private static int DEFAULT_DISTANCE = 30;

    public Move(int distance) {
        this.distance = distance;
    }
    @Override
    public void execute(Monde monde) {
        Segment s = monde.getPersonnage().moveForward(distance);
        monde.addSegment(s);

    }

    @Override
    public String toString() {
        return "Avancer de " + distance;
    }
}
