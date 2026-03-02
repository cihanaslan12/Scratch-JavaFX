package scratch.model;

public abstract class Action implements Commande {

    private Personnage personnage;

    private boolean inProgram = false;

    public boolean actionForProgram() {
        return inProgram;
    }
    public void setInProgram(boolean inProgram) {
        this.inProgram = inProgram;
    }
    public abstract  Action copyActionForProgram();

}
