package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Programme {

    private final ObservableList<Commande> program = FXCollections.observableArrayList();

    public ObservableList<Commande> getProgram() {
        return FXCollections.unmodifiableObservableList(program);
    }

    public void addAction(int idx) {

        Commande c = ActionList.getCommande(idx);
        if(c != null) {
            program.add(c);
        }

    }

    public void up(int index) {
        if (index > 0 && index < program.size()) {
            Commande c = program.set(index, program.get(index - 1));
            program.set(index - 1, c);
        }


    }

    public void down() {


    }

    public void duplicate() {

    }
    public void remove() {

    }

    public void clear() {

    }
}
