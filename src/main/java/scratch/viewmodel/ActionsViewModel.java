package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import scratch.model.ActionList;
import scratch.model.Commande;
import scratch.model.Move;
import scratch.model.Programme;

public class ActionsViewModel {

    private final Programme choosenActions;

    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1),
            programIndex = new SimpleIntegerProperty(-1);

    private final ObjectProperty<Commande> commandeProperty = new SimpleObjectProperty<>();
    private final IntegerProperty parameterProperty = new SimpleIntegerProperty();
    private IntegerProperty boundParam = null;

    private final StringProperty startLblProperty = new SimpleStringProperty();
    private final StringProperty endLblProperty = new SimpleStringProperty();
    private final StringProperty inputProperty = new SimpleStringProperty();

    public ActionsViewModel(Programme choosenActions) {
        this.choosenActions = choosenActions;

        commandeProperty.bind(
                Bindings.createObjectBinding(() -> {
                    int idx = programIndex.get();
                    if (idx >= 0 && idx < choosenActions.getProgram().size()) {
                        return choosenActions.getProgram().get(idx);
                    } else {
                        return null;
                    }
                }, programIndex,choosenActions.getProgram())
        );
        commandeProperty.addListener((obs,oldVal,newVal) -> {
            if (boundParam != null) {
                parameterProperty.unbindBidirectional(boundParam);
                boundParam = null;
            }
            if (newVal != null && newVal.parametreProperty() != null) {
                boundParam = newVal.parametreProperty();
                parameterProperty.bindBidirectional(boundParam);
            } else {
                parameterProperty.set(0);
            }
        });
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

    public ObjectProperty<Commande> commandeProperty() {
        return commandeProperty;
    }
    public StringBinding startLblProperty() {
        return Bindings.createStringBinding(
                () -> {
                    Commande c = commandeProperty.get();
                    return c == null ? "" : c.toString();
                }, commandeProperty);
    }
    public StringBinding endLblProperty() {
        return Bindings.createStringBinding(() -> {
            Commande c = commandeProperty.get();
            return c == null ? "" : commandeProperty.get().unite();
        }, commandeProperty);
    }
    public IntegerProperty inputProperty() {
        return parameterProperty;
    }
    public BooleanBinding canEdit() {
        return Bindings.createBooleanBinding(() -> {
            Commande c = commandeProperty.get();
            return c != null && c.isEditable();
        }, commandeProperty);
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
