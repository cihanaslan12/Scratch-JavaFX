package scratch.viewmodel;

import javafx.beans.binding.Bindings;
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

    public BooleanBinding canUp() {
        return programIndex.greaterThan(0);
    }

    public BooleanBinding canDown() {
        return Bindings.createBooleanBinding(() -> {
            int idx = programIndex.get();
            return idx >= 0 && idx < choosenActions.getProgram().size() - 1;
        }, programIndex, choosenActions.getProgram());
    }
    public BooleanBinding canDuplicate() {
        return programIndex.greaterThanOrEqualTo(0);
    }

    public void up() {
        int idx = programIndex.get();
        if (idx > 0 && idx < choosenActions.getProgram().size()) {
            choosenActions.up(idx);
            programIndex.set(idx - 1);
        }
    }

    public void down() {
        int idx = programIndex.get();
        if (idx < choosenActions.getProgram().size()) {
            choosenActions.down(idx);
            programIndex.set(idx + 1);
        }
    }

    public void duplicate() {
        int idx = programIndex.get();
        choosenActions.duplicate(idx);
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
