package scratch.model;

import javafx.beans.property.IntegerProperty;

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
    public abstract IntegerProperty parameterProperty();
    public abstract Boolean isValidparametre(int param);
    public abstract boolean isEditable();
    public abstract String unite();
    public abstract String detailActionLabel();
}
