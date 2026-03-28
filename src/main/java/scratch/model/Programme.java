package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Programme {

    private final ObservableList<Action> program = FXCollections.observableArrayList();

    public ObservableList<Action> getProgram() {
        return FXCollections.unmodifiableObservableList(program);
    }

    public void addAction(int actionListIndex, int insertIndex) {
        Action action = ActionList.getActionList().get(actionListIndex).copyActionForProgram();

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
            program.get(execIdx).execute(monde);
            return execIdx + 1;
        }
        return execIdx;
    }
    public boolean isPenInstructionValid() {
        boolean penDownState = true;

        for (Action action : getProgram()) {

            if (action.isPenAction()) {

                boolean wantDown = action.getPenState();

                if (wantDown == penDownState) {
                    return false;
                }

                penDownState = wantDown;
            }
        }

        return true;
    }

    public void addActionForFile(Action action) {
        program.add(action);
    }
}
