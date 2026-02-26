package scratch.model;

public class Pen extends Action {

    private final boolean styloDown; // true = abaisser, false = lever

    public Pen(boolean styloDown) {
        this.styloDown = styloDown;
    }
    public boolean isStyloDown() {
        return styloDown;
    }

    @Override
    public void execute(Monde monde) {
        if(styloDown) {
            monde.getPersonnage().penDown();
        } else {
            monde.getPersonnage().penUp();
        }

    }

    @Override
    public String toString() {
        return styloDown ? "Abaisser le stylo" : "Lever le stylo";
    }
}
