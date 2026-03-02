package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActionList {

    private static final ObservableList<Action> actionList = FXCollections.observableArrayList(
      new Move(30),
      new Turn(true),          // Tourner gauche
      new Turn(false),        // Tourner droite
      new Pen(false),   // Lever stylo
      new Pen(true)     // Abaisser le stylo
    );

    public static ObservableList<Action> getActionList() {
        return FXCollections.unmodifiableObservableList(actionList);
    }

    public static Action getAction(int idx) {
        // si l'index est dans les bornes, on récupère la commande
        return (idx >= 0 && idx < actionList.size()) ? actionList.get(idx) : null;
    }
}
