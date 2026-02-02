package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActionList {

    private final ObservableList<Commande> commandeList = FXCollections.observableList(
      new Move();
      new Turn();
      new Turn();
      new Pen(false);
      new Pen(true);
    );

    public ObservableList<Commande> getCommandeList() {
        return FXCollections.unmodifiableObservableList(commandeList);
    }

    public Commande getCommande(int idx) {
        // si l'index est dans les bornes, on récupère la commande
        return (idx >= 0 && idx <= commandeList.size()) ? commandeList.get(idx) : null;
    }
}
