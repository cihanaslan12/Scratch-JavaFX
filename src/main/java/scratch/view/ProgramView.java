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
import scratch.viewmodel.ActionsViewModel;


public class ProgramView extends VBox {

    private final ActionsViewModel vm;

    private final Label programLabel = new Label("Programme");
    private final ListView<Action> program = new ListView<>();

    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnDuplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider tout");
    private final Button btnPlus = new Button("+");
    private final Button btnMinus = new Button("-");

    private final VBox prgmInnerVbox = new VBox();
    private final HBox prgmBtnsHbox = new HBox();
    private final HBox innerEditBox = new HBox();


    private final TextField input = new TextField();
    private final TextField input2 = new TextField();

    private final TitledPane actionDetails = new TitledPane("Détails de l'action", innerEditBox);

    private final Label startLbl = new Label();
    private final Label middleLbl = new Label();
    private final Label endLbl = new Label();
    private final Label errLbl = new Label("Erreur valeur");


    public ProgramView(ActionsViewModel vm) {
        this.vm = vm;

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
        innerEditBox.setSpacing(10);

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
        actionDetails.setPrefWidth(360);
        innerEditBox.setPrefWidth(340);
      //  innerEditBox.setAlignment(Pos.CENTER_LEFT);

        // largeur colonnes
        middleLbl.setPrefWidth(50);
        input.setPrefWidth(50);
        input2.setPrefWidth(50);
        btnPlus.setPrefWidth(45);
        btnMinus.setPrefWidth(45);
    }

    private void configActions() {
        btnUp.setOnAction(e -> vm.up());
        btnDown.setOnAction(e -> vm.down());
        btnDuplicate.setOnAction(e -> vm.duplicate());
        btnDelete.setOnAction(e -> vm.delete());
        btnClear.setOnAction(e -> vm.clear());
        btnPlus.setOnAction(e-> vm.changeSecondParameter(1) );
        btnMinus.setOnAction(e-> vm.changeSecondParameter(-1));
    }

    private void configBindings() {
        btnUp.disableProperty().bind(vm.canUp().not());
        btnDown.disableProperty().bind(vm.canDown().not());
        btnDuplicate.disableProperty().bind(vm.canDuplicate().not());
        btnDelete.disableProperty().bind(vm.canDelete().not());
        btnClear.disableProperty().bind(vm.canClear().not());
        input.disableProperty().bind(vm.canEdit().not());
        input2.disableProperty().bind(vm.canEdit().not());

        errLbl.visibleProperty().bind(vm.showError());

        btnPlus.visibleProperty().bind(vm.showIncrementButtonsProperty());
        btnPlus.managedProperty().bind(btnPlus.visibleProperty());

        btnMinus.visibleProperty().bind(vm.showIncrementButtonsProperty());
        btnMinus.managedProperty().bind(btnMinus.visibleProperty());

    }

    private void configSelection() {
        program.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            vm.programIndexProperty().setValue(newVal.intValue());
        });

       /* startLbl.textProperty().bind(
                Bindings.when(vm.ActionProperty().isNull())
                        .then("(aucune action sélectionnée)")
                        .otherwise(vm.startLblProperty())
        );*/

       /* endLbl.textProperty().bind(vm.endLblProperty());
        input.textProperty().bindBidirectional(vm.inputProperty());
        input2.textProperty().bindBidirectional(vm.secondInputProperty());*/

        vm.inputProperty().addListener((obs, oldVal, newVal) -> program.refresh());
        vm.secondInputProperty().addListener((obs, oldVal, newVal) -> program.refresh());

        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Action action = vm.ActionProperty().get();

                if (action == null) {
                    return;
                }

                String text = input.getText();

                if (!action.isValidParameter(text)) {
                    input.setText(action.getRawParameter());
                }
            }
        });

        input2.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Action action = vm.ActionProperty().get();

                if (action == null || !action.hasTwoParameters()) {
                    return;
                }

                String text = input2.getText();

                if (!action.isValidSecondParameter(text)) {
                    input2.setText(action.getSecondParameter());
                }
            }
        });

        vm.programIndexProperty().addListener((obs, oldVal, newVal) -> {
            program.getSelectionModel().select(newVal.intValue());
        });

        vm.highlightIdxProperty().addListener((obs, oldV, newV) -> {
            int idx = newV.intValue();
            if (idx >= 0 && idx < program.getItems().size()) {
                program.getSelectionModel().select(idx);
                program.scrollTo(idx);
            }
        });

        vm.ActionProperty().addListener((obs, oldAction, newAction) -> {
            updateDetailArea(newAction);
        });

        updateDetailArea(vm.ActionProperty().get());
    }

    private Color actionColor(Action action) {
        switch (action.getType()) {
            case MOVE: return Color.DARKBLUE;
            case TURN_LEFT: return Color.RED;
            case TURN_RIGHT: return Color.RED;
            case PEN_DOWN:  return Color.GREEN;
            case PEN_UP:  return Color.GREEN;
            case VAR_DECLARATION: return Color.DARKORCHID;
            case VAR_ASSIGNMENT: return Color.DARKORCHID;
            case VAR_INCREMENT: return Color.DARKORCHID;
            case REPEAT: return Color.DARKORCHID;
            case END_REPEAT: return Color.DARKORCHID;
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
    private void clearDetailArea() {
        innerEditBox.getChildren().clear();

        startLbl.textProperty().unbind();
        endLbl.textProperty().unbind();
        middleLbl.textProperty().unbind();

        input.textProperty().unbindBidirectional(vm.inputProperty());
        input2.textProperty().unbindBidirectional(vm.secondInputProperty());
    }

    private void showEmptyDetailArea() {
        startLbl.setText("(aucune action sélectionnée)");
        input.setText("");
        input2.setText("");
        endLbl.setText("");
        middleLbl.setText("");

        innerEditBox.getChildren().addAll(startLbl);
    }

    private void showTwoParameterDetail(Action action) {
        startLbl.setText(action.detailActionLabel());
        input.setText(action.getRawParameter());

        middleLbl.setText(action.detailSecondActionLabel());
        input2.setText(action.getSecondParameter());

        input.textProperty().bindBidirectional(vm.inputProperty());
        input2.textProperty().bindBidirectional(vm.secondInputProperty());

        if (action.getType() == Type.VAR_INCREMENT) {
            innerEditBox.getChildren().addAll(startLbl, input, middleLbl, input2, btnPlus, btnMinus, errLbl);
        } else {
            innerEditBox.getChildren().addAll(startLbl, input, middleLbl, input2, errLbl);
        }
    }

    private void showSingleParameterDetail(Action action) {
        startLbl.textProperty().bind(
                Bindings.when(vm.ActionProperty().isNull())
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