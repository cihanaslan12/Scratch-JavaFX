package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActionList {

    private static final ObservableList<Commande> commandeList = FXCollections.observableArrayList(
      new Move(30),
      new Turn(90,true),  // Tourner gauche
      new Turn(90,false), // Tourner droite
      new Pen(false),          // Lever stylo
      new Pen(true)            // Abaisser le stylo
    );

    public static ObservableList<Commande> getCommandeList() {
        return FXCollections.unmodifiableObservableList(commandeList);
    }

    public static Commande getCommande(int idx) {
        // si l'index est dans les bornes, on récupère la commande
        return (idx >= 0 && idx < commandeList.size()) ? commandeList.get(idx) : null;
    }
}
