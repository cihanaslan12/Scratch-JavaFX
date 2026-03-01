package scratch.model;

import javafx.beans.property.IntegerProperty;

public class Pen extends Action {

    private final boolean stylo;

    public Pen(boolean stylo) {
        this.stylo = stylo;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public IntegerProperty parametreProperty() {
        return null;
    }

    @Override
    public String unite() {
        return "";
    }

    @Override
    public Commande copyCommande() {
        return new Pen(stylo);
    }

    @Override
    public String toString() {
        return stylo ? "Abaisser le stylo" : "Lever le stylo";
    }
}
