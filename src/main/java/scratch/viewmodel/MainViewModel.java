package scratch.viewmodel;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import scratch.model.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainViewModel {
    private final ActionsViewModel aVm;
    private final ProgramViewModel pVm;
    private final WorldViewModel wVm;

    public MainViewModel() {
        this.aVm = new ActionsViewModel();
        this.pVm = new ProgramViewModel();
        this.wVm = new WorldViewModel();

        wVm.execIdxProperty().addListener((obs, oldIdx, newIdx) -> {
            pVm.highlightIdxProperty().set(newIdx.intValue());
        });

        wVm.hasErrorProperty().addListener((obs, oldVal, hasError) -> {
            if (hasError) {
                pVm.runtimeErrorProperty().set(true);
            }
        });
            // on réinitialise tout en cas de changement de program
        pVm.getProgramActions().addListener((ListChangeListener<Action>) c -> {
            mainInvalidate();
        });

        pVm.canLoad().addListener(observable -> {
            wVm.loadedProperty().set(true);
        });

        pVm.runtimeErrorProperty().addListener(observable -> {
            wVm.hasErrorProperty().set(true);
        });
    }

    public ActionsViewModel getAVm() {
        return aVm;
    }

    public ProgramViewModel getPVm() {
        return pVm;
    }

    public WorldViewModel getWVm() {
        return wVm;
    }

    public void newProgram() {
        pVm.getProgramActions().clear();    // choosenActions.clear();
        wVm.getMonde().reset();
        pVm.runtimeErrorProperty().set(false);
    }

    public void openFile(File file) {
        this.newProgram();
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                String command = scan.nextLine();
                String[] parts = command.split(";");
                String action = parts[0];

                Action newAction = null;

                switch (action) {
                    case "MOVE_FORWARD" -> newAction = new Move((parts[1]));
                    case "TURN_RIGHT" -> newAction = new Turn((parts[1]), false);
                    case "TURN_LEFT" -> newAction = new Turn((parts[1]), true);
                    case "PEN_UP" -> newAction = new Pen(false);
                    case "PEN_DOWN" -> newAction = new Pen(true);
                    case "VAR_DECLARATION" -> newAction = new VarDeclaration(parts[1]);
                    case "VAR_ASSIGNMENT" -> newAction = new VarAssignment(parts[1], parts[2]);
                    case "INCREMENT_VARIABLE" -> newAction = new IncrementVariable(parts[1], parts[2]);
                    case "REPEAT" -> newAction = new Repeat(parts[1]);
                    case "END_REPEAT" -> newAction = new Repeat(false);
                }
                if(newAction != null) {
                    newAction.setInProgram(true);
                    pVm.getModelProgram().addActionForFile(newAction);
                }
            }
            pVm.invalidateProgram();
            pVm.getModelProgram().refreshRepeatValid();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveFileAs(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Action action : pVm.getProgramActions()) {
                writer.println(action.stringForSave());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exitProgram() {
        Platform.exit();
    }

    private void mainInvalidate () {
        pVm.invalidateProgram();
        wVm.invalidateWorld();
    }

}
