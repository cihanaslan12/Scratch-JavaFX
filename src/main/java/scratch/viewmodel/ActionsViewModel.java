package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import scratch.model.ActionList;
import scratch.model.Commande;

public class ActionsViewModel {

    private final ActionList actionList;
    // private final BooleanBinding

    public ActionsViewModel(ActionList actionList) {
        this.actionList = actionList;
    }

    public ObservableList<Commande> getActions() {
        return actionList.getCommandeList();
    }



}
