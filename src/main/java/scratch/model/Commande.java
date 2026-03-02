package scratch.model;

import javafx.beans.property.IntegerProperty;

public interface Commande {

    void execute(Monde monde);
    boolean isEditable();
    IntegerProperty parametreProperty();
    String unite();
    String detailActionLabel();
}
