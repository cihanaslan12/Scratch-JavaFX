package scratch.viewmodel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import scratch.model.*;
import scratch.model.ActionList;
import scratch.model.Programme;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ActionsViewModel {

    private final Programme choosenActions;
    private final Monde monde;

    private final IntegerProperty actionIndex = new SimpleIntegerProperty(-1),
            programIndex = new SimpleIntegerProperty(-1);

    private final IntegerProperty execIdx = new SimpleIntegerProperty(0);
    private final BooleanProperty loaded = new SimpleBooleanProperty(false); // cliquer sur Charger
    private final BooleanProperty stepping = new SimpleBooleanProperty(false); // bouton executer en mode suivant
    private final IntegerProperty highlightIdx = new SimpleIntegerProperty(-1); // ligne surligné du prog

    private final StringProperty loadButtonText = new SimpleStringProperty("Charger");
    private final StringProperty runButtonText = new SimpleStringProperty("Executer");

    // action actuellement sélectionnée dans le programme
    private final ObjectProperty<Action> actionProperty = new SimpleObjectProperty<>();

    // paramètre dans détail action
    private final StringProperty parameterProperty = new SimpleStringProperty("");
    private final StringProperty secondParameterProperty = new SimpleStringProperty("");

    private final BooleanProperty isValidInput = new SimpleBooleanProperty(true);
    private final BooleanProperty isValidSecondInput = new SimpleBooleanProperty(true);

    private final Timeline executeAuto = new Timeline();
    private final DoubleProperty speed = new SimpleDoubleProperty(1.0);

    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);

    // indique si une erreur s'est produite à l'exécution
    private final BooleanProperty runtimeError = new SimpleBooleanProperty(false);

    public ActionsViewModel(Programme choosenActions, Monde monde) {

        this.choosenActions = choosenActions;
        this.monde = monde;

        // lie l'action sélectionnée à l'index sélectionné dans le programme
        actionProperty.bind(
                Bindings.createObjectBinding(() -> {
                    int idx = programIndex.get();
                    if (idx >= 0 && idx < choosenActions.getProgram().size()) {
                        return choosenActions.getProgram().get(idx);
                    }
                    return null;
                }, programIndex, choosenActions.getProgram())
        );

        // met à jour les champs de saisie quand l'action sélectionnée change
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

            if (newVal.hasTwoParameters()) {
                isValidSecondInput.set(newVal.isValidSecondParameter(secondParameterProperty.get()));
            } else {
                isValidSecondInput.set(true);
            }
        });

        // met à jour le premier paramètre de l'action quand le champ texte change
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
            choosenActions.refreshRepeatValid();
            choosenActions.refreshVarsDeclarationValid();
        });

        // Met à jour le deuxième paramètre de l'action quand le champ texte change
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
            // met à jour les validations du programme
            choosenActions.refreshRepeatValid();
            choosenActions.refreshVarsDeclarationValid();
        });

        // initialise l'exécution automatique
        keyFrame();

        //mise à jour de la vitesse(du timeline) lorsque la vitesse du slider change
        speed.addListener((obs, oldVal, newVal) -> {
            executeAuto.setRate(newVal.doubleValue());
        });
    }


    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public ObservableList<VarDeclaration> getVariables() {
        return monde.getVariables();
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
                            !choosenActions.getProgram().isEmpty()
                                    && choosenActions.isPenInstructionValid()
                                    && choosenActions.repeatValidProperty().get()
                                    && choosenActions.areVarDeclarationsAtTop()
                                    && choosenActions.varsDeclarationValidProperty().get()
                                    && choosenActions.isPenStateOkInLoops(),

                    choosenActions.getProgram(), choosenActions.repeatValidProperty(),choosenActions.varsDeclarationValidProperty()
            );
    }

    // indique si un message d'erreur de saisie doit être affiché
    public BooleanBinding showError() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();

                    if (action == null || !action.isEditable()) {
                        return false;
                    }

                    String first = parameterProperty.get();
                    String second = secondParameterProperty.get();

                    boolean firstEmpty = first == null || first.isBlank();
                    boolean secondEmpty = second == null || second.isBlank();

                    if (action.hasTwoParameters()) {
                        if (firstEmpty) {
                            return false;
                        }
                        if (secondEmpty) {
                            return false;
                        }
                        return !isValidInput.get() || !isValidSecondInput.get();
                    }

                    if (firstEmpty) {
                        return false;
                    }

                    return !isValidInput.get();
                },
                actionProperty,
                parameterProperty,
                secondParameterProperty,
                isValidInput,
                isValidSecondInput
        );
    }
    // retourne le texte du label d'erreur
    public StringBinding errorLabelTextProperty() {
        return Bindings.createStringBinding(() -> {
                    if (runtimeError.get() || showError().get()) {
                        return "Erreur valeur";
                    }
                    return "";
                }, runtimeError, actionProperty, parameterProperty,
                secondParameterProperty, isValidInput, isValidSecondInput);
    }

    //  indique si les boutons + et - doivent être affichés
    public BooleanBinding showIncrementButtonsProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = secondParameterProperty.get();

                    return action != null
                            && action.getType() == Type.VAR_INCREMENT
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                secondParameterProperty
        );
    }

    public void addAction() {
        int idxAction = actionIndex.get();
        int idxProgram = programIndex.get();

        if (idxAction < 0 || idxAction >= getActions().size()) {
            return;
        }
        Action newAction = getActions().get(idxAction).copyActionForProgram();
        choosenActions.addAction(newAction, idxProgram);

        if (idxProgram >= 0 && idxProgram < choosenActions.getProgram().size() - 1) {
            programIndex.set(idxProgram + 1);
        } else {
            programIndex.set(choosenActions.getProgram().size() - 1);
        }

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
                runtimeError.set(false);

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
                if (!isRunning.get()) {     // si exec auto -> pas de btn suivant
                    runButtonText.set("Suivant");
                }
                highlightIdx.set(0);
            } else {
                // mode Suivant
                // try catch -> quand il y a une erreur a l'éxecution, le bouton Suivant est désactivé
                try {
                    execIdx.set(choosenActions.executeNext(execIdx.get(), monde));
                    if (execIdx.get() < size) {
                        highlightIdx.set(execIdx.get());
                    } else {
                        stepping.set(false);
                        runButtonText.set("Executer");
                        highlightIdx.set(size - 1);
                    }
                } catch (RuntimeException e) {
                    runtimeError.set(true);
                    stepping.set(false);
                    runButtonText.set("Executer");

                    e.printStackTrace();

                    if (execIdx.get() >= 0 && execIdx.get() < size) {
                        highlightIdx.set(execIdx.get());
                    }
                }
            }
        }
    }
    // méthode qui lie le temps d'execution et les méthodes d'execution à l'execution auto
    private void keyFrame() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
            if (canRun().get()) {
                execOrNext();
            } else {
                stopExec();
            }
        });
        executeAuto.getKeyFrames().add(keyFrame);
        executeAuto.setCycleCount(Animation.INDEFINITE);
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void startAutoExec() {
        executeAuto.play();
        isRunning.set(true);
    }

    public void stopExec() {
        executeAuto.stop();
        isRunning.set(false);
    }

    // retourne un texte décrivant la position et l'angle de la tortue
    public StringBinding turtlePosition() {
        return Bindings.createStringBinding(() -> {
            double x = monde.getPosPersonnageX().get() - getWorldOriginX();
            double y = getWorldOriginY() - monde.getPosPersonnageY().get();
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

    // retourne l'action sélectionnée
    public ObjectProperty<Action> actionProperty() {
            return actionProperty;
    }

    // retourne le texte de début pour la zone détail
    public StringBinding startLblProperty () {
            return Bindings.createStringBinding(
                   () -> {
                        Action action = actionProperty.get();
                        return action == null ? "" : action.detailActionLabel();
                   }, actionProperty);
    }

    // retourne le texte de fin pour la zone détail
    public StringBinding endLblProperty () {
        return Bindings.createStringBinding(() -> {
            Action action = actionProperty.get();
            return action == null ? "" : actionProperty.get().unite();
        }, actionProperty);
    }

    // retourne le premier paramètre édité
    public StringProperty inputProperty() {
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
        runtimeError.set(false);
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
                    choosenActions.addActionForFile(newAction);
                }
            }
            invalidateProgram();
            choosenActions.refreshRepeatValid();
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

    // modifie le deuxième paramètre d'une action (pour l'incrémentation + 1 ou - 1)
    public void changeSecondParameter(int increment) {
        Action action = actionProperty.get();

        if (action == null || !action.hasTwoParameters()) {
            return;
        }

        String text = secondParameterProperty.get();

        if (text != null && text.matches("-?\\d+")) {
            int value = Integer.parseInt(text);
            secondParameterProperty.set(String.valueOf(value + increment));
        }
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
    public int getWorldSize() {
        return monde.getWorldSize();
    }
    public int getWorldOriginX() {
        return monde.getWorldOriginX();
    }
    public int getWorldOriginY() {
        return monde.getWorldOriginY();
    }
    public DoubleProperty getPosX(){
        return monde.getPosPersonnageX();
    }
    public DoubleProperty getPosY() {
        return monde.getPosPersonnageY();
    }
    public DoubleProperty getAngleProperty() {
        return monde.getPersonnageAngle();
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
    public BooleanProperty runtimeErrorProperty() {
        return runtimeError;
    }

    // invalide l'état d'exécution du programme après une modification
   private void invalidateProgram () {
        loaded.set(false);
        stepping.set(false);
        execIdx.set(0);
        highlightIdx.set(-1);
        runtimeError.set(false);
        loadButtonText.set("Charger");
        runButtonText.set("Executer");
   }

    // calcule le niveau d'indentation visuelle d'une action dans la liste
    public int getIndentationLevel(int index) {
        int indent = 0;

        for (int i = 0; i < index; i++) {
            Action action = choosenActions.getProgram().get(i);

            if (action.getType() == Type.REPEAT) {
                indent++;
            } else if (action.getType() == Type.END_REPEAT) {
                indent--;
            }
        }

        Action current = choosenActions.getProgram().get(index);
        if (current.getType() == Type.END_REPEAT) {
            indent--;
        }

        // je dois prévoir le cas ou indent devient négatif
        // ex : si je répète End repeat deux foix
        return Math.max(indent, 0);
    }

}


