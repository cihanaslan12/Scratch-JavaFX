package scratch.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Stack;

public class Programme {

    private final ObservableList<Action> program = FXCollections.observableArrayList();
    private final BooleanProperty repeatValid = new SimpleBooleanProperty(true);

    public ObservableList<Action> getProgram() {
        return FXCollections.unmodifiableObservableList(program);
    }

    public void addAction(Action action, int insertIndex) {
        if (insertIndex >= 0 && insertIndex < program.size()) {

            program.add(insertIndex + 1, action);
        } else {
            program.add(action);
        }
    }

    public void up(int index) {
        if (index > 0 && index < program.size()) {
            Action action = program.set(index, program.get(index - 1));
            program.set(index - 1, action);
        } else {
            throw new RuntimeException("Cannot go up !");
        }
    }

    public void down(int idx) {
        if (idx < program.size()) {
            Action action = program.get(idx);
            program.set(idx, program.get(idx + 1));
            program.set(idx + 1, action);
        } else {
            throw new RuntimeException("Cannot go down !");
        }

    }

    public void duplicate(int idx) {
        if(idx >= 0 && idx < program.size() ) {
            Action action = program.get(idx);
            Action copy = action.copyActionForProgram();
            copy.setInProgram(true);
            program.add( copy);
        }


    }
    public void remove(int idx) {
        if(idx >= 0 && idx < program.size()) {
            program.remove(idx);
        }
    }

    public void clear() {
        program.clear();
    }
    public int executeNext(int execIdx, Monde monde) {
        if(execIdx >= 0 && execIdx < program.size()) {
            monde.setExecIdx(execIdx);
            program.get(execIdx).execute(monde);
            if (monde.getExecIdx() != execIdx) {
                return monde.getExecIdx() + 1;
            }
            return execIdx + 1;
        }
        return execIdx;
    }
    public boolean isPenInstructionValid() {
        boolean penDownState = true;

        for (Action action : getProgram()) {

            if (action.getType() == Type.PEN_DOWN || action.getType() == Type.PEN_UP) {

                boolean wantDown = action.getPenState();

                if (wantDown == penDownState) {
                    return false;
                }

                penDownState = wantDown;
            }
        }

        return true;
    }
    public boolean isRepeatValid() {
        /*
        La pile qui contiendra le nombre de fois que le programme doit être répter
        on les stock dans une pile car on peut avoir des boucles impbriquées
        */
        Stack<String> compteursBoucle = new Stack<>();

        for (Action action : getProgram()) {
            /* si on est dans un bloc de repeat
                 On récup le nb fois et on le stock dans une string
                    si c'est pas null et que c'est pas un entier (donc une variable), on l'ajout a la pile
                    sinon c'est un entier donc on ajoute null, car un entier n'a pas besoin de protection contre l'incrémentation dans la boucle
            */
            if (action.getType() == Type.REPEAT) {
                String param = action.getRawParameter();

                if (param != null && !param.matches("-?\\d+")) {
                    compteursBoucle.push(param);
                } else {
                    compteursBoucle.push(null);
                }
            }
            /*
            si c'est la fin de la boucle
                si y a encore "un nombre de fois a tourner" on enlève cette valeur pour sortir de la boucle actuelle
            */
            else if (action.getType() == Type.END_REPEAT) {
                if (!compteursBoucle.isEmpty()) {
                    compteursBoucle.pop();
                }
            }
            /*
            si la pile de compteurs n'est pas vide, c'est a dire qu'on est dans une boucle imbriquée
                on récup la valeur de la boucle suivante (le nb de fois que cette boucle doit tourner)
                on vérifie si on essaye de modfier la valeur du compteur
            */
            else if (!compteursBoucle.isEmpty()) {
                if (action.getType() == Type.VAR_ASSIGNMENT
                        || action.getType() == Type.VAR_INCREMENT
                        || action.getType() == Type.VAR_DECLARATION) {

                    String variableModifiee = getVariableModifiedBy(action);
                    /*
                    parcours toutes les variables dans la pile
                    car si on est dans une boucle imbriqué
                    ex :
                    REPEAT x fois
                        REPEAT y fois
                            ++x <== ca devrait pas être possible
                        FIN REPEAT
                    FIN REPEAT
                    */
                    for (String compteur : compteursBoucle) {
                        if (compteur != null && compteur.equals(variableModifiee)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
    private String getVariableModifiedBy(Action action) {
        if (action.getType() == Type.VAR_ASSIGNMENT
                || action.getType() == Type.VAR_DECLARATION
                || action.getType() == Type.VAR_INCREMENT) {
            return action.getRawParameter();
        }
        return null;
    }
    public void addActionForFile(Action action) {
        program.add(action);
    }
    public BooleanProperty repeatValidProperty() {
        return repeatValid;
    }

    public void refreshRepeatValid() {
        repeatValid.set(isRepeatValid());
    }
}
