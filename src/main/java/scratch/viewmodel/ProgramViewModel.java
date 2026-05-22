package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import scratch.model.Action;
import scratch.model.Programme;
import scratch.model.Type;

public class ProgramViewModel {
    private final Programme choosenActions;
    private final IntegerProperty programIndex = new SimpleIntegerProperty(-1);
    private final IntegerProperty highlightIdx = new SimpleIntegerProperty(-1); // ligne surligné du prog
    // action actuellement sélectionnée dans le programme
    private final ObjectProperty<Action> actionProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Action> actionToAdd = new SimpleObjectProperty<>();
    // paramètre dans détail action
    private final StringProperty parameterProperty = new SimpleStringProperty("");
    private final StringProperty secondParameterProperty = new SimpleStringProperty("");
    private final BooleanProperty isValidInput = new SimpleBooleanProperty(true);
    private final BooleanProperty isValidSecondInput = new SimpleBooleanProperty(true);
    // indique si une erreur s'est produite à l'exécution
    private final BooleanProperty runtimeError = new SimpleBooleanProperty(false);

    public ProgramViewModel() {
        this.choosenActions = new Programme();

        // ajoute l'action dans la liste de program dès l'ajout
        actionToAdd.addListener((obs, oldAct, newAct) -> {
            if (newAct != null) {
                addAction();
                actionToAdd.set(null);
            }
        });

        // lie l'action sélectionnée à l'index sélectionné dans le programme
        actionProperty.bind(
                Bindings.createObjectBinding(() -> {
                    int idx = programIndex.get();
                    if (idx >= 0 && idx < getProgramActions().size()) {
                        return getProgramActions().get(idx);
                    }
                    return null;
                }, programIndex, getProgramActions())
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
    }

    public Programme getModelProgram() {
        return choosenActions;
    }

    public ObjectProperty<Action> actionProperty() {
        return actionProperty;
    }

    public ObjectProperty<Action> actionToAddProperty() {
        return actionToAdd;
    }

    public ObservableList<Action> getProgramActions () {
        return choosenActions.getProgram();
    }

    public IntegerProperty programIndexProperty () {
        return programIndex;
    }

    public IntegerProperty highlightIdxProperty () {
        return highlightIdx;
    }

    public BooleanBinding canUp () {
        return programIndex.greaterThan(0);
    }

    public BooleanBinding canDown () {
        return Bindings.createBooleanBinding(() -> {
            int idx = programIndex.get();
            return idx >= 0 && idx < getProgramActions().size() - 1;
        }, programIndex, getProgramActions());
    }

    public BooleanBinding canDuplicate () {
        return programIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canDelete () {
        return programIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canClear () {
        return Bindings.size(getProgramActions()).greaterThan(0);
    }

    public BooleanBinding canLoad () {
        return Bindings.createBooleanBinding(() ->
                        !getProgramActions().isEmpty()
                                && !choosenActions.max3DrawPolygon()
                                && choosenActions.drawRectangleIsNotLast()
                                && !choosenActions.teleportationInRepeat()
                                && choosenActions.isPenInstructionValid()
                                && choosenActions.repeatValidProperty().get()
                                && choosenActions.areVarDeclarationsAtTop()
                                && choosenActions.varsDeclarationValidProperty().get()
                                && choosenActions.isPenStateOkInLoops(),

                getProgramActions(), choosenActions.repeatValidProperty(),choosenActions.varsDeclarationValidProperty()
        );
    }

    public void addAction() {
        int idxProgram = programIndex.get();
        Action newAction = actionToAdd.get().copyActionForProgram();
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
        if (idx > 0 && idx < getProgramActions().size()) {
            choosenActions.up(idx);
            programIndex.set(idx - 1);
            invalidateProgram();
        }
    }

    public void down () {
        int idx = programIndex.get();
        if (idx < getProgramActions().size()) {
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
        if (idx >= 0 && idx < getProgramActions().size()) {
            choosenActions.remove(idx);
            invalidateProgram();
        }
    }

    public void clear () {
        choosenActions.clear();
        invalidateProgram();
    }

    public BooleanBinding canEdit () {
        return Bindings.createBooleanBinding(() -> {
            Action action = actionProperty.get();
            return action != null && action.isEditable();
        }, actionProperty);
    }

    // retourne le texte de début pour la zone détail
    public StringBinding startLblProperty () {
        return Bindings.createStringBinding(
                () -> {
                    Action action = actionProperty.get();
                    return action == null ? "" : action.detailActionLabel();
                }, actionProperty);
    }

    // retourne le premier paramètre édité
    public StringProperty inputProperty() {
        return parameterProperty;
    }

    public ReadOnlyBooleanProperty isValidInputProperty() {
        return isValidInput;
    }

    // retourne le texte de fin pour la zone détail
    public StringBinding endLblProperty () {
        return Bindings.createStringBinding(() -> {
            Action action = actionProperty.get();
            return action == null ? "" : actionProperty.get().unite();
        }, actionProperty);
    }

    public StringProperty secondInputProperty() {
        return secondParameterProperty;
    }

    public ReadOnlyBooleanProperty isValidSecondInputProperty() {
        return isValidSecondInput;
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

    public BooleanBinding showFirstInputDPIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = parameterProperty.get();

                    return action != null
                            && action.getType() == Type.DRAW_POLYGON
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                parameterProperty
        );
    }

    public BooleanBinding showSecondInputDPIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = secondParameterProperty.get();

                    return action != null
                            && action.getType() == Type.DRAW_POLYGON
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                secondParameterProperty
        );
    }

    public BooleanBinding showFirstInputDRIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = parameterProperty.get();

                    return action != null
                            && action.getType() == Type.DRAW_RECTANGLE
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                parameterProperty
        );
    }

    public BooleanBinding showSecondInputDRIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = secondParameterProperty.get();

                    return action != null
                            && action.getType() == Type.DRAW_RECTANGLE
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                secondParameterProperty
        );
    }

    public BooleanBinding showFirstInputTeleIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = parameterProperty.get();

                    return action != null
                            && action.getType() == Type.TELEPORTATION
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                parameterProperty
        );
    }

    public BooleanBinding showSecondInputTeleIncrBtnProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    Action action = actionProperty.get();
                    String text = secondParameterProperty.get();

                    return action != null
                            && action.getType() == Type.TELEPORTATION
                            && text != null
                            && text.matches("-?\\d+");
                },
                actionProperty,
                secondParameterProperty
        );
    }

    // modifie le premier paramètre d'une action (pour l'incrémentation + 1 ou - 1)
    public void changeFirstParameter(int increment) {
        Action action = actionProperty.get();

        if (action == null) {
            return;
        }

        String text = parameterProperty.get();

        if (text != null && text.matches("-?\\d+")) {
            int value = Integer.parseInt(text);
            parameterProperty.set(String.valueOf(value + increment));
        }
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

    public BooleanProperty runtimeErrorProperty() {
        return runtimeError;
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

    // invalide l'état d'exécution du programme après une modification
    public void invalidateProgram () {
        highlightIdx.set(-1);
        runtimeError.set(false);
    }

    // calcule le niveau d'indentation visuelle d'une action dans la liste
    public int getIndentationLevel(int index) {
        int indent = 0;

        for (int i = 0; i < index; i++) {
            Action action = getProgramActions().get(i);

            if (action.getType() == Type.REPEAT) {
                indent++;
            } else if (action.getType() == Type.END_REPEAT) {
                indent--;
            }
        }

        Action current = getProgramActions().get(index);
        if (current.getType() == Type.END_REPEAT) {
            indent--;
        }

        // je dois prévoir le cas ou indent devient négatif
        // ex : si je répète End repeat deux foix
        return Math.max(indent, 0);
    }
}
