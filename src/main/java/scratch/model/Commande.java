package scratch.model;

import javafx.beans.property.IntegerProperty;

public interface Commande {

    void execute();
    boolean isEditable();
    IntegerProperty parametreProperty();
    String unite();
    Commande copyCommande();
}
