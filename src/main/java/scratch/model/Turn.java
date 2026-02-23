package scratch.model;

public class Turn extends Action{
    private final int angle;


    public Turn(int angle) {
        this.angle = angle;
    }

    @Override
    public void execute(Monde monde) {

    }

    @Override
    public String toString() {
        // juste pour le test
        return (angle >= 91 && angle <= 179) ? "Tourner a droite de " + angle : "Tourner a gauche de " + angle;
        // return res + " " + angle;
    }
}
