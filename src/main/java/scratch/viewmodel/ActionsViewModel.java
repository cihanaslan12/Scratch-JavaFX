package scratch.viewmodel;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import scratch.model.*;
import scratch.model.ActionList;
import scratch.model.Programme;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ActionsViewModel {

    private final Programme choosenActions;
    private final Monde monde;
    private  Type type;
    private Pen pen;

    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1),
            programIndex = new SimpleIntegerProperty(-1);

    private final IntegerProperty execIdx = new SimpleIntegerProperty(0);
    private final BooleanProperty loaded = new SimpleBooleanProperty(false); // cliquer sur Charger
    private final BooleanProperty stepping = new SimpleBooleanProperty(false); // bouton executer en mode suivant
    private final IntegerProperty highlightIdx = new SimpleIntegerProperty(-1); // ligne surligné du prog

    private final StringProperty loadButtonText = new SimpleStringProperty("Charger");
    private final StringProperty runButtonText = new SimpleStringProperty("Executer");

    private final ObjectProperty<Action> actionProperty = new SimpleObjectProperty<>();

    private final StringProperty parameterProperty = new SimpleStringProperty("");
    private final StringProperty secondParameterProperty = new SimpleStringProperty("");
    private BooleanProperty isValidInput = new SimpleBooleanProperty(true);
    private BooleanProperty isValidSecondInput = new SimpleBooleanProperty(true);


    public ActionsViewModel(Programme choosenActions, Monde monde) {

        this.choosenActions = choosenActions;
        this.monde = monde;

        this.choosenActions.getProgram().addListener((ListChangeListener<Action>) c -> {
            loaded.set(false);
            stepping.set(false);
            execIdx.set(0);
            highlightIdx.set(-1);
            loadButtonText.set("Charger");
            runButtonText.set("Executer");
        });

        actionProperty.bind(
                Bindings.createObjectBinding(() -> {
                    int idx = programIndex.get();
                    if (idx >= 0 && idx < choosenActions.getProgram().size()) {
                        return choosenActions.getProgram().get(idx);
                    }
                    return null;
                }, programIndex, choosenActions.getProgram())
        );

        actionProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                parameterProperty.set("");
                secondParameterProperty.set("");
                isValidInput.set(true);
                isValidSecondInput.set(true);
                return;
            }

            if (!newVal.isEditable()) {
                parameterProperty.set("");
                secondParameterProperty.set("");
                isValidInput.set(true);
                isValidSecondInput.set(true);
                return;
            }

            parameterProperty.set(newVal.getRawParameter());
            secondParameterProperty.set(newVal.getSecondParameter());
            isValidInput.set(newVal.isValidParameter(parameterProperty.get()));
            isValidSecondInput.set(newVal.isValidSecondParameter(secondParameterProperty.get()));
            if (newVal.hasTwoParameters()) {
                isValidSecondInput.set(newVal.isValidSecondParameter(secondParameterProperty.get()));
            } else {
                isValidSecondInput.set(true);
            }
        });
        parameterProperty.addListener((obs, oldVal, newVal) -> {
            Action action = actionProperty.get();

            if (action == null || !action.isEditable()) {
                return;
            }

            boolean valid = action.isValidParameter(newVal);
            isValidInput.set(valid);

            if (valid) {
                action.setRawParameter(newVal);
            }
        });
        secondParameterProperty.addListener((obs, oldVal, newVal) -> {
            Action action = actionProperty.get();

            if (action == null || !action.hasTwoParameters()) {
                return;
            }

            boolean valid = action.isValidSecondParameter(newVal);
            isValidSecondInput.set(valid);

            if (valid) {
                action.setSecondParameter(newVal);
            }
        });
    }

    public BooleanBinding canAdd () {
            return actionIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canUp () {
            return programIndex.greaterThan(0);
    }

    public BooleanBinding canDown () {
            return Bindings.createBooleanBinding(() -> {
                int idx = programIndex.get();
                return idx >= 0 && idx < choosenActions.getProgram().size() - 1;
            }, programIndex, choosenActions.getProgram());
    }
    public BooleanBinding canDuplicate () {
            return programIndex.greaterThanOrEqualTo(0);
    }
    public BooleanBinding canDelete () {
            return programIndex.greaterThanOrEqualTo(0);
    }
    public BooleanBinding canClear () {
            return Bindings.size(choosenActions.getProgram()).greaterThan(0);
    }
    public BooleanBinding canRun () {
            return Bindings.createBooleanBinding(() -> {
                int size = choosenActions.getProgram().size();
                return loaded.get() && size > 0 && execIdx.get() < size;
            }, loaded, execIdx, choosenActions.getProgram());
    }
    public BooleanBinding canLoad () {
            return Bindings.createBooleanBinding(() ->
                            !choosenActions.getProgram().isEmpty() && choosenActions.isPenInstructionValid(),
                    choosenActions.getProgram()
            );
    }

    public void addAction() {
            int idx = actionIndex.get();
            choosenActions.addAction(idx);
            invalidateProgram();
    }

    public void up () {
            int idx = programIndex.get();
            if (idx > 0 && idx < choosenActions.getProgram().size()) {
                choosenActions.up(idx);
                programIndex.set(idx - 1);
                invalidateProgram();
            }
    }

    public void down () {
            int idx = programIndex.get();
            if (idx < choosenActions.getProgram().size()) {
                choosenActions.down(idx);
                programIndex.set(idx + 1);
                invalidateProgram();
            }
    }

    public void duplicate () {
            int idx = programIndex.get();
            choosenActions.duplicate(idx);
            invalidateProgram();
    }

    public void delete () {
            int idx = programIndex.get();
            if (idx >= 0 && idx < choosenActions.getProgram().size()) {
                choosenActions.remove(idx);
                invalidateProgram();
            }
    }

    public void clear () {
            choosenActions.clear();
            invalidateProgram();
    }


    public void loadOrReset () {
            if (!choosenActions.getProgram().isEmpty()) {
                monde.reset();
                execIdx.set(0);
                highlightIdx.set(-1);

                loaded.set(true);
                stepping.set(false);

                loadButtonText.set("Ré-initialiser");
                runButtonText.set("Executer");
            }
    }

    public void execOrNext() {
        int size = choosenActions.getProgram().size();
        if (loaded.get() && size > 0) {
            // premier clic sur Executer -> sélectionne la première ligne du prog
            if (!stepping.get()) {
                stepping.set(true);
                runButtonText.set("Suivant");
                highlightIdx.set(0);
            } else {
                // mode Suivant
                execIdx.set(choosenActions.executeNext(execIdx.get(), monde));
                if (execIdx.get() < size) {
                    highlightIdx.set(execIdx.get());
                } else {
                    stepping.set(false);
                    runButtonText.set("Executer");
                    highlightIdx.set(size - 1);
                }
            }
        }
    }

    public StringBinding turtlePosition() {
        return Bindings.createStringBinding(() -> {
            double x = monde.getPosPersonnageX().get() - 250;
            double y = 250 - monde.getPosPersonnageY().get();
            double a = monde.getPersonnageAngle().get();
            double angle = (a % 360 + 360) % 360;   // calcul de l'angle de 0 à 359
            return String.format("Tortue: x = %.1f, y = %.1f, direction = %.1f °", x, y, angle);
           }, monde.getPosPersonnageX(), monde.getPosPersonnageY(), monde.getPersonnageAngle()
        );
    }

    public IntegerProperty actionIndexProperty () {
            return actionIndex;
    }
    public IntegerProperty programIndexProperty () {
            return programIndex;
    }

    public ObjectProperty<Action> ActionProperty() {
            return actionProperty;
    }
    public StringBinding startLblProperty () {
            return Bindings.createStringBinding(
                   () -> {
                        Action action = actionProperty.get();
                        return action == null ? "" : action.detailActionLabel();
                   }, actionProperty);
    }
        public StringBinding endLblProperty () {
            return Bindings.createStringBinding(() -> {
                Action action = actionProperty.get();
                return action == null ? "" : actionProperty.get().unite();
            }, actionProperty);
        }
    public StringProperty inputProperty() {
        return parameterProperty;
    }

    public StringProperty parameterProperty() {
        return parameterProperty;
    }
    public ReadOnlyBooleanProperty isValidInputProperty() {
            return isValidInput;
    }
    public BooleanBinding canEdit () {
        return Bindings.createBooleanBinding(() -> {
            Action action = actionProperty.get();
            return action != null && action.isEditable();
        }, actionProperty);
    }
    public void newProgram() {
        choosenActions.clear();
        monde.reset();
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
                    case "MOVE_FORWARD" -> newAction = new Move(Integer.parseInt(parts[1]));
                    case "TURN_RIGHT" -> newAction = new Turn(Integer.parseInt(parts[1]), false);
                    case "TURN_LEFT" -> newAction = new Turn(Integer.parseInt(parts[1]), true);
                    case "PEN_UP" -> newAction = new Pen(false);
                    case "PEN_DOWN" -> newAction = new Pen(true);
                    case "VAR_DECLARATION" -> newAction = new VarDeclaration(parts[1]);
                }
                newAction.setInProgram(true);
                choosenActions.addActionForFile(newAction);
            }
            invalidateProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveFileAs(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Action action : getProgramActions()) {
                writer.println(action.stringForSave());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void exitProgram() {
        Platform.exit();
    }

    public ObservableList<Action> getActions () {
            return ActionList.getActionList();
    }
    public ObservableList<Action> getProgramActions () {
            return choosenActions.getProgram();
    }
    public Monde getMonde () {
            return monde;
    }
    public ObservableList<Segment> getSegments() {
        return monde.getSegments();
    }
    public DoubleProperty getPosX(){
        return monde.getPosPersonnageX();
    }
    public DoubleProperty getPosY() {
        return monde.getPosPersonnageY();
    }
    public double getAngle() {
        return monde.getPersonnageAngle().get();
    }
    public BooleanProperty loadedProperty () {
            return loaded;
    }
    public IntegerProperty highlightIdxProperty () {
            return highlightIdx;
    }
    public StringProperty runButtonTextProperty () {
            return runButtonText;
    }
    public StringProperty loadButtonTextProperty () {
        return loadButtonText;
    }

    public StringProperty secondInputProperty() {
        return secondParameterProperty;
    }

    public ReadOnlyBooleanProperty isValidSecondInputProperty() {
        return isValidSecondInput;
    }

   private void invalidateProgram () {
        loaded.set(false);
        stepping.set(false);
        execIdx.set(0);
        highlightIdx.set(-1);
        loadButtonText.set("Charger");
        runButtonText.set("Executer");
   }

   private boolean isPenInstructionValid () {
        boolean penDownState = true; // abaisser de base

       for (Action action : choosenActions.getProgram()) {
           if (action instanceof Pen pen) {
               boolean wantDown = pen.isStyloDown();

               if (wantDown == penDownState) {
                   return false; // règle métier FAQ
               }
                   penDownState = wantDown;
                }

            }
       return true;
        }
}


