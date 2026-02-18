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
        } else {
            throw new RuntimeException("Cannot go up !");
        }
    }

    public void down(int idx) {
        if (idx < program.size()) {
            Commande c = program.get(idx);
            program.set(idx, program.get(idx + 1));
            program.set(idx + 1, c);
        } else {
            throw new RuntimeException("Cannot go down !");
        }

    }

    public void duplicate(int idx) {
        if(idx >= 0 && idx < program.size() ) {
            Commande c = program.get(idx);
            program.add(c);
        }


    }
    public void remove() {

    }

    public void clear() {

    }
}
