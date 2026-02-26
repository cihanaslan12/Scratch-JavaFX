package scratch.model;

public class Turn extends Action{
    private final int angle;
    private final boolean left;


    public Turn(int angle, boolean left) {
        this.angle = angle;
        this.left = left;
    }

    @Override
    public void execute(Monde monde) {
        if(left) {
            monde.getPersonnage().turnLeft(angle);
        } else {
            monde.getPersonnage().turnRight(angle);
        }

    }

    @Override
    public String toString() {
        // juste pour le test
        return left ? "Tourner à gauche de " + angle  : "Tourner droite " + angle;

    }
}
