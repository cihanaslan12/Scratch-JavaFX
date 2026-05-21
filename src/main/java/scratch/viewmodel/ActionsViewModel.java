package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;
import scratch.model.ActionList;

public class ActionsViewModel {
    private final ObservableList<Action> actionList;
    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<Action> actionForProgram = new SimpleObjectProperty<>(null);
    private final Action advancedModeAction = new DrawPolygon();

    public ActionsViewModel() {
        this.actionList = FXCollections.observableArrayList(ActionList.getActionList());
    }

    // Retourne la liste d'actions
    public ObservableList<Action> getActionList () {
        return this.actionList;
    }

    // Retourne l'action sélectionné dans la liste d'actions
    public Action getAction(int idx) {
        return (idx >= 0 && idx < actionList.size()) ? actionList.get(idx) : null;
    }

    // Property de l'index sélectionné dans la liste d'actions
    public IntegerProperty actionIndexProperty () {
        return actionIndex;
    }

    // Property de l'action sélectionné pour être ajouté au program
    public ObjectProperty<Action> actionForProgramProperty() {
        return actionForProgram;
    }

    public BooleanBinding canAdd () {
            return actionIndex.greaterThanOrEqualTo(0);
    }

    // Récupère l'action sélectionné à ajouter dans le program
    public void addAction() {
        int idxAction = actionIndex.get();
        if (idxAction < 0 || idxAction >= getActionList().size()) {
            return;
        }
        Action newAction = getAction(idxAction).copyActionForProgram();
        actionForProgram.set(newAction);
        // remettre le property à null pour rajouter encore si plusieurs click
        // sinon il n'y aurait pas de changement visible pour le listener de la mVm
        actionForProgram.set(null);
    }

    public void updateActionList(boolean bool) {
        if (bool) {
            if (!actionList.contains(advancedModeAction)) {
                actionList.add(advancedModeAction);
            }
        } else {
            actionList.remove(advancedModeAction);
        }
    }
}


