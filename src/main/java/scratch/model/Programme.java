package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Programme {

    private final ObservableList<Commande> program = FXCollections.observableArrayList();

//    public Programme(Commande c) {
//        program.add(c);
//    }

    public ObservableList<Commande> getProgram() {
        return this.program;
    }
}
