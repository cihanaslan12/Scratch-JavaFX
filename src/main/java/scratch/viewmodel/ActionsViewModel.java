package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
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
    private final BooleanProperty loaded = new SimpleBooleanProperty(false); // cliquer sur Charger
    private final BooleanProperty stepping = new SimpleBooleanProperty(false); // bouton executer en mode suivant
    private final IntegerProperty highlightIdx = new SimpleIntegerProperty(-1); // ligne surligné du prog
    private final StringProperty loadButtonText = new SimpleStringProperty("Charger");
    private final StringProperty runButtonText = new SimpleStringProperty("Executer");

    public ActionsViewModel(Programme choosenActions, Monde monde) {
        this.choosenActions = choosenActions;
        this.monde = monde;

        this.choosenActions.getProgram().addListener((ListChangeListener<Commande>) c -> {
            loaded.set(false);
            stepping.set(false);
            execIdx.set(0);
            highlightIdx.set(-1);
            loadButtonText.set("Charger");
            runButtonText.set("Executer");
        } );
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
    public BooleanBinding canRun() {
        return  Bindings.createBooleanBinding(() -> {
            int size = choosenActions.getProgram().size();
            return loaded.get() && size > 0 && execIdx.get() < size;
        }, loaded,execIdx,choosenActions.getProgram());
    }
    public BooleanBinding canLoad() {
        return Bindings.size(choosenActions.getProgram()).greaterThan(0);
    }

    public void addAction() {
        int idx = actionIndex.get();
        choosenActions.addAction(idx);
        invalidateProgram();
    }

    public void up() {
        int idx = programIndex.get();
        if (idx > 0 && idx < choosenActions.getProgram().size()) {
            choosenActions.up(idx);
            programIndex.set(idx - 1);
            invalidateProgram();
        }
    }

    public void down() {
        int idx = programIndex.get();
        if (idx < choosenActions.getProgram().size()) {
            choosenActions.down(idx);
            programIndex.set(idx + 1);
            invalidateProgram();
        }
    }

    public void duplicate() {
        int idx = programIndex.get();
        choosenActions.duplicate(idx);
        invalidateProgram();
    }

    public void delete() {
        int idx = programIndex.get();
        if(idx >= 0 && idx < choosenActions.getProgram().size()) {
            choosenActions.remove(idx);
            invalidateProgram();
        }
    }

    public void clear() {
        choosenActions.clear();
        invalidateProgram();
    }



    public void loadOrReset() {
        if (choosenActions.getProgram().size() > 0) {
            monde.reset();
            execIdx.set(0);
            highlightIdx.set(-1);

            loaded.set(true);
            stepping.set(false);

            loadButtonText.set("Ré-initialiser");
            runButtonText.set("Executer");
        }
    }
    public void runButton() {
        int size = choosenActions.getProgram().size();
        if(loaded.get() && size > 0 && execIdx.get() < size) {
            // premier clic sur Executer -> sélectionne la première ligne du prog
            if(!stepping.get()) {
                stepping.set(true);
                runButtonText.set("Suivant");
                highlightIdx.set(0);
            } else {
                // mode Suivant
                int i = execIdx.get();
                if(i >= 0 && i < size) {
                    Commande c = choosenActions.getProgram().get(i);
                    c.execute(monde);

                    execIdx.set(i + 1);

                    if(execIdx.get() < size) {
                        highlightIdx.set(execIdx.get());
                    } else {
                        stepping.set(false);
                        runButtonText.set("Executer");
                        highlightIdx.set(size - 1);
                    }

                }
            }
        }
    }

    public IntegerProperty actionIndexProperty() {
        return actionIndex;
    }
    public IntegerProperty programIndexProperty() {
        return programIndex;
    }

    public ObservableList<Commande> getActions() {
        return ActionList.getCommandeList();
    }

    public ObservableList<Commande> getProgramActions() {
        return choosenActions.getProgram();
    }
    public Monde getMonde() {
        return monde;
    }
    public BooleanProperty loadedProperty() {
        return loaded;
    }
    public IntegerProperty highlightIdxProperty() {
        return highlightIdx;
    }
    public StringProperty runButtonTextProperty() {
        return runButtonText;
    }
    public StringProperty loadButtonTextProperty() {
        return loadButtonText;
    }
    private void invalidateProgram() {
        loaded.set(false);
        stepping.set(false);
        execIdx.set(0);
        highlightIdx.set(-1);
        loadButtonText.set("Charger");
        runButtonText.set("Executer");
    }

}
