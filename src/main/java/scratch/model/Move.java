package scratch.model;

public class Move extends Action {

    private int distance;
    private static int DEFAULT_DISTANCE = 30;

    public Move(int distance) {
        this.distance = distance;
    }
    @Override
    public void execute() {

    }

    @Override
    public String toString() {
        return "Avancer de " + distance;
    }
}
