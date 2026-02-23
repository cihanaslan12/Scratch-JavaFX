package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.ActionList;
import scratch.model.Commande;
import scratch.model.Monde;
import scratch.model.Programme;

public class ActionsViewModel {

    private final Programme choosenActions;
    private final   Monde monde;

    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1),
            programIndex = new SimpleIntegerProperty(-1);
    private final IntegerProperty execIdx = new SimpleIntegerProperty(0);

    public ActionsViewModel(Programme choosenActions, Monde monde) {
        this.choosenActions = choosenActions;
        this.monde = monde;
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
    public BooleanBinding canDelete() {
        return programIndex.greaterThanOrEqualTo(0);
    }
    public BooleanBinding canClear() {
        return Bindings.size(choosenActions.getProgram()).greaterThan(0);
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

    public void delete() {
        int idx = programIndex.get();
        if(idx >= 0 && idx < choosenActions.getProgram().size()) {
            choosenActions.remove(idx);
        }
    }

    public void clear() {
        choosenActions.clear();
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
    public void load() {
        monde.reset();
        execIdx.set(0);
    }
    public void execute() {
        monde.reset();
        for(Commande c : getProgramActions()) {
            c.execute(monde);
        }
    }
    public void next() {
        int i = execIdx.get();
        if( i  >= 0 && i < choosenActions.getProgram().size()) {
            Commande c = choosenActions.getProgram().get(i);
            c.execute(monde);
            execIdx.set(i + 1);
        }
    }
    public Monde getMonde() {
        return monde;
    }

}
