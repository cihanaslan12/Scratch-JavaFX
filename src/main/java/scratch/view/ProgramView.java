package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scratch.model.Action;
import scratch.model.Type;
import scratch.viewmodel.ProgramViewModel;


public class ProgramView extends VBox {
    private final ProgramViewModel vm;

    private final Label programLabel = new Label("Programme");
    private final ListView<Action> program = new ListView<>();

    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnDuplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider tout");
    private final Button btnPlus = new Button("+");
    private final Button btnMinus = new Button("-");
    private final Button btnPolyPlus1 = new Button("+");
    private final Button btnPolyMinus1 = new Button("-");
    private final Button btnPolyPlus2 = new Button("+");
    private final Button btnPolyMinus2 = new Button("-");
    private final Button btnRectPlus1 = new Button("+");
    private final Button btnRectMinus1 = new Button("-");
    private final Button btnRectPlus2 = new Button("+");
    private final Button btnRectMinus2 = new Button("-");
    private final Button btnTelePlus1 = new Button("+");
    private final Button btnTeleMinus1 = new Button("-");
    private final Button btnTelePlus2 = new Button("+");
    private final Button btnTeleMinus2 = new Button("-");

    private final VBox prgmInnerVbox = new VBox();
    private final HBox prgmBtnsHbox = new HBox();
    private final HBox innerEditBox = new HBox();


    private final TextField input = new TextField();
    private final TextField input2 = new TextField();

    private final TitledPane actionDetails = new TitledPane("Détails de l'action", innerEditBox);

    private final Label startLbl = new Label();
    private final Label middleLbl = new Label();
    private final Label endLbl = new Label();
    private final Label errLbl = new Label();


    public ProgramView(ProgramViewModel vm) {
        this.vm = vm;

        // lie la liste aux actions du programme
        program.setItems(vm.getProgramActions());

        prgmBtnsHbox.getChildren().addAll(btnUp, btnDown, btnDuplicate, btnDelete, btnClear);
        startLbl.setText("(aucune action sélectionnée)");
        innerEditBox.getChildren().addAll(startLbl, input, endLbl, errLbl);
        actionDetails.setContent(innerEditBox);
        prgmInnerVbox.getChildren().addAll(prgmBtnsHbox, actionDetails);
        getChildren().addAll(program, prgmInnerVbox);

        setSpacing(15);
        prgmInnerVbox.setSpacing(15);
        prgmBtnsHbox.setSpacing(15);
        innerEditBox.setSpacing(5);

        errLbl.setTextFill(Color.RED);
        errLbl.setVisible(false);

        style();
        setupColoredCells();
        configActions();
        configBindings();
        configSelection();
    }
    private void style() {
        // largeur fixe colonne gauche
        this.setPrefWidth(400);
        this.setMinWidth(400);
        this.setMaxWidth(400);

        // largeur zone détail
        actionDetails.setPrefWidth(400);
        innerEditBox.setPrefWidth(400);
      //  innerEditBox.setAlignment(Pos.CENTER_LEFT);

        // largeur colonnes
        middleLbl.setPrefWidth(50);
        input.setPrefWidth(60);
        input2.setPrefWidth(50);
        btnPlus.setPrefWidth(45);
        btnPlus.setPrefWidth(45);
        btnPolyPlus1.setPrefWidth(25);
        btnPolyMinus1.setPrefWidth(25);
        btnPolyPlus2.setPrefWidth(25);
        btnPolyMinus2.setPrefWidth(25);
        btnRectPlus1.setPrefWidth(25);
        btnRectMinus1.setPrefWidth(25);
        btnRectPlus2.setPrefWidth(25);
        btnRectMinus2.setPrefWidth(25);
        btnTelePlus1.setPrefWidth(25);
        btnTeleMinus1.setPrefWidth(25);
        btnTelePlus2.setPrefWidth(25);
        btnTeleMinus2.setPrefWidth(25);
    }

    private void configActions() {
        btnUp.setOnAction(e -> vm.up());
        btnDown.setOnAction(e -> vm.down());
        btnDuplicate.setOnAction(e -> vm.duplicate());
        btnDelete.setOnAction(e -> vm.delete());
        btnClear.setOnAction(e -> vm.clear());
        btnPlus.setOnAction(e-> vm.changeSecondParameter(1) );
        btnMinus.setOnAction(e-> vm.changeSecondParameter(-1));
        btnPolyPlus1.setOnAction(e -> vm.changeFirstParameter(1));
        btnPolyMinus1.setOnAction(e -> vm.changeFirstParameter(-1));
        btnPolyPlus2.setOnAction(e-> vm.changeSecondParameter(1) );
        btnPolyMinus2.setOnAction(e-> vm.changeSecondParameter(-1));
        btnRectPlus1.setOnAction(e -> vm.changeFirstParameter(1));
        btnRectMinus1.setOnAction(e -> vm.changeFirstParameter(-1));
        btnRectPlus2.setOnAction(e-> vm.changeSecondParameter(1) );
        btnRectMinus2.setOnAction(e-> vm.changeSecondParameter(-1));
        btnTelePlus1.setOnAction(e -> vm.changeFirstParameter(1));
        btnTeleMinus1.setOnAction(e -> vm.changeFirstParameter(-1));
        btnTelePlus2.setOnAction(e-> vm.changeSecondParameter(1) );
        btnTeleMinus2.setOnAction(e-> vm.changeSecondParameter(-1));
    }

    private void configBindings() {
        btnUp.disableProperty().bind(vm.canUp().not());
        btnDown.disableProperty().bind(vm.canDown().not());
        btnDuplicate.disableProperty().bind(vm.canDuplicate().not());
        btnDelete.disableProperty().bind(vm.canDelete().not());
        btnClear.disableProperty().bind(vm.canClear().not());
        input.disableProperty().bind(vm.canEdit().not());
        input2.disableProperty().bind(vm.canEdit().not());

        errLbl.visibleProperty().bind(vm.showError().or(vm.runtimeErrorProperty()));
        errLbl.textProperty().bind(vm.errorLabelTextProperty());

        btnPlus.visibleProperty().bind(vm.showIncrementButtonsProperty());
        btnPlus.managedProperty().bind(btnPlus.visibleProperty());

        btnMinus.visibleProperty().bind(vm.showIncrementButtonsProperty());
        btnMinus.managedProperty().bind(btnMinus.visibleProperty());

        btnPolyPlus1.visibleProperty().bind(vm.showFirstInputDPIncrBtnProperty());
        btnPolyPlus1.managedProperty().bind(btnPolyPlus1.visibleProperty());

        btnPolyMinus1.visibleProperty().bind(vm.showFirstInputDPIncrBtnProperty());
        btnPolyMinus1.managedProperty().bind(btnPolyMinus1.visibleProperty());

        btnPolyPlus2.visibleProperty().bind(vm.showSecondInputDPIncrBtnProperty());
        btnPolyPlus2.managedProperty().bind(btnPolyPlus2.visibleProperty());

        btnPolyMinus2.visibleProperty().bind(vm.showSecondInputDPIncrBtnProperty());
        btnPolyMinus2.managedProperty().bind(btnPolyMinus2.visibleProperty());

        btnRectPlus1.visibleProperty().bind(vm.showFirstInputDRIncrBtnProperty());
        btnRectPlus1.managedProperty().bind(btnRectPlus1.visibleProperty());

        btnRectMinus1.visibleProperty().bind(vm.showFirstInputDRIncrBtnProperty());
        btnRectMinus1.managedProperty().bind(btnRectMinus1.visibleProperty());

        btnRectPlus2.visibleProperty().bind(vm.showSecondInputDRIncrBtnProperty());
        btnRectPlus2.managedProperty().bind(btnRectPlus2.visibleProperty());

        btnRectMinus2.visibleProperty().bind(vm.showSecondInputDRIncrBtnProperty());
        btnRectMinus2.managedProperty().bind(btnRectMinus2.visibleProperty());

        btnTelePlus1.visibleProperty().bind(vm.showFirstInputTeleIncrBtnProperty());
        btnTelePlus1.managedProperty().bind(btnTelePlus1.visibleProperty());

        btnTeleMinus1.visibleProperty().bind(vm.showFirstInputTeleIncrBtnProperty());
        btnTeleMinus1.managedProperty().bind(btnTeleMinus1.visibleProperty());

        btnTelePlus2.visibleProperty().bind(vm.showSecondInputTeleIncrBtnProperty());
        btnTelePlus2.managedProperty().bind(btnTelePlus2.visibleProperty());

        btnTeleMinus2.visibleProperty().bind(vm.showSecondInputTeleIncrBtnProperty());
        btnTeleMinus2.managedProperty().bind(btnTeleMinus2.visibleProperty());
    }

    private void configSelection() {
        // met à jour l'index sélectionné dans le ViewModel
        program.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            vm.programIndexProperty().setValue(newVal.intValue());
        });

        // refresh la liste quand les paramètres changent pour mettre à jour
        // l'affichage du toString() dans le programme
        vm.inputProperty().addListener((obs, oldVal, newVal) -> program.refresh());
        vm.secondInputProperty().addListener((obs, oldVal, newVal) -> program.refresh());

        // quand on quitte le premier champ, on vérifie si la valeur est valide
        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Action action = vm.actionProperty().get();

                if (action == null) {
                    return;
                }

                String text = input.getText();

//                // si valeur invalide -> on remet l'ancienne valeur
//                if (!action.isValidParameter(text)) {
//                    input.setText(action.getRawParameter());
//                }
            }
        });

        // quand on quitte le deuxième champ, on vérifie aussi la validité
        input2.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Action action = vm.actionProperty().get();

                if (action == null || !action.hasTwoParameters()) {
                    return;
                }

                String text = input2.getText();

//                // si valeur invalide -> on remet l'ancienne valeur
//                if (!action.isValidSecondParameter(text)) {
//                    input2.setText(action.getSecondParameter());
//                }
            }
        });

        // si le ViewModel change l'index, on met à jour la sélection visuelle
        vm.programIndexProperty().addListener((obs, oldVal, newVal) -> {
            program.getSelectionModel().select(newVal.intValue());
        });

        // " surligne " l'action en cours d'éexuction
        vm.highlightIdxProperty().addListener((obs, oldV, newV) -> {
            int idx = newV.intValue();
            if (idx >= 0 && idx < program.getItems().size()) {
                program.getSelectionModel().select(idx);
                program.scrollTo(idx);
            }
        });

        // met à jour la zone de détail quand l'action sélectionnée change
        vm.actionProperty().addListener((obs, oldAction, newAction) -> {
            updateDetailArea(newAction);
        });

        // initialisation de la zone de détail
        updateDetailArea(vm.actionProperty().get());
    }

    private Color actionColor(Action action) {
        switch (action.getType()) {
            case MOVE: return Color.DARKBLUE;
            case TURN_LEFT, TURN_RIGHT: return Color.RED;
            case PEN_DOWN, PEN_UP:  return Color.GREEN;
            case VAR_DECLARATION, VAR_ASSIGNMENT, VAR_INCREMENT, REPEAT, END_REPEAT: return Color.DARKORCHID;
            case DRAW_POLYGON, DRAW_RECTANGLE: return Color.ORANGE;
            case TELEPORTATION: return Color.DEEPSKYBLUE;
            default:   return Color.BLACK;
        }
    }

    private void setupColoredCells() {
        program.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Color c = actionColor(item);

                    int indentLevel = vm.getIndentationLevel(getIndex());
                    Region space = new Region();
                    space.setPrefWidth(indentLevel * 20);

                    Circle circle = new Circle(5,c);
                    Label label = new Label(item.toString());
                    label.setTextFill(c);

                    HBox box = new HBox(8, space, circle, label);
                    setText(null);
                    setGraphic(box);
                    /*
                    setText(item.toString());
                    setTextFill(c);
                    setGraphic(new Circle(5, c));
                    setGraphicTextGap(8); */
                }
            }
        });
    }

    //  met à jour toute la zone de détail selon l'action sélectionnée
    private void updateDetailArea(Action action) {
        clearDetailArea();

        if (action == null) {
            showEmptyDetailArea();
        } else if (action.hasTwoParameters()) {
            showTwoParameterDetail(action);
        } else {
            showSingleParameterDetail(action);
        }
    }

    // nettoie la zone de détail avant de la reconstruire
    private void clearDetailArea() {
        innerEditBox.getChildren().clear();

        startLbl.textProperty().unbind();
        endLbl.textProperty().unbind();
        middleLbl.textProperty().unbind();

        input.textProperty().unbindBidirectional(vm.inputProperty());
        input2.textProperty().unbindBidirectional(vm.secondInputProperty());
    }

    // affiche la zone de détail quand aucune action n'est sélectionnée
    private void showEmptyDetailArea() {
        startLbl.setText("(aucune action sélectionnée)");
        input.setText("");
        input2.setText("");
        endLbl.setText("");
        middleLbl.setText("");

        innerEditBox.getChildren().addAll(startLbl);
    }

    // affiche la zone de détail pour une action à deux paramètres
    private void showTwoParameterDetail(Action action) {
        startLbl.setText(action.detailActionLabel());
        input.setText(action.getRawParameter());

        middleLbl.setText(action.detailSecondActionLabel());
        input2.setText(action.getSecondParameter());

        // lie les textField  au ViewModel
        input.textProperty().bindBidirectional(vm.inputProperty());
        input2.textProperty().bindBidirectional(vm.secondInputProperty());

        // pour incrémentation, on affiche les boutons + et -
        if (action.getType() == Type.VAR_INCREMENT) {
            innerEditBox.getChildren().addAll(startLbl, input, middleLbl, input2, btnPlus, btnMinus, errLbl);
        } else if (action.getType() == Type.DRAW_POLYGON) {
            innerEditBox.getChildren().addAll(startLbl, input, btnPolyPlus1, btnPolyMinus1, middleLbl, input2, btnPolyPlus2, btnPolyMinus2, errLbl);
        } else if (action.getType() == Type.DRAW_RECTANGLE) {
            innerEditBox.getChildren().addAll(startLbl, input, btnRectPlus1, btnRectMinus1, middleLbl, input2, btnRectPlus2, btnRectMinus2, errLbl);
        } else if (action.getType() == Type.TELEPORTATION) {
            innerEditBox.getChildren().addAll(startLbl, input, btnTelePlus1, btnTeleMinus1, middleLbl, input2, btnTelePlus2, btnTeleMinus2, errLbl);
        } else {
            innerEditBox.getChildren().addAll(startLbl, input, middleLbl, input2, errLbl);
        }
    }
    // affiche la zone de détail pour une action à un seul paramètre
    private void showSingleParameterDetail(Action action) {
        startLbl.textProperty().bind(
                Bindings.when(vm.actionProperty().isNull())
                        .then("(aucune action sélectionnée)")
                        .otherwise(vm.startLblProperty())
        );

        endLbl.textProperty().bind(vm.endLblProperty());
        input.textProperty().bindBidirectional(vm.inputProperty());

        innerEditBox.getChildren().addAll(startLbl, input, endLbl, errLbl);
    }
    public Label getProgramLabel() {
        return programLabel;
    }
}