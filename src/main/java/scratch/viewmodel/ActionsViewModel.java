package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.ActionList;
import scratch.model.Commande;
import scratch.model.Programme;

public class ActionsViewModel {

    private final Programme choosenActions;

    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1),
            programIndex = new SimpleIntegerProperty(-1);

    public ActionsViewModel(Programme choosenActions) {
        this.choosenActions = choosenActions;
    }

    public BooleanBinding canAdd() {
        return actionIndex.greaterThanOrEqualTo(0);
    }

    public IntegerProperty actionIndexProperty() {
        return actionIndex;
    }
    public IntegerProperty programIndexProperty() {
        return programIndex;
    }

    public void addAction() {
        int idx = actionIndex.get();
        choosenActions.addAction(idx);
    }
    public ObservableList<Commande> getActions() {
        return ActionList.getCommandeList();
    }

    public ObservableList<Commande> getProgramActions() {
        return choosenActions.getProgram();
    }

}
