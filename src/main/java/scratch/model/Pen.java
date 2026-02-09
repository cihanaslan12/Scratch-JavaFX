package scratch.model;

public class Pen extends Action {

    private final boolean stylo;

    public Pen(boolean stylo) {
        this.stylo = stylo;
    }

    @Override
    public void execute() {

    }

    @Override
    public String toString() {
        return stylo ? "Abaisser le stylo" : "Lever le stylo";
    }
}
