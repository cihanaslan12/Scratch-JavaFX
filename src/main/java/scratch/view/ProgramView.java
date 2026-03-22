package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scratch.model.Action;
import scratch.viewmodel.ActionsViewModel;
import javafx.util.converter.NumberStringConverter;

public class ProgramView extends VBox {

    private final ActionsViewModel vm;

    private final Label programLabel = new Label("Programme");
    private final ListView<Action> program = new ListView<>();

    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnDuplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider tout");

    private final VBox prgmInnerVbox = new VBox();
    private final HBox prgmBtnsHbox = new HBox();
    private final HBox innerEditBox = new HBox();

    private final TextField input = new TextField();
    private final TitledPane actionDetails = new TitledPane("Détails de l'action", input);
    private final Label startLbl = new Label();
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
        input.setPrefWidth(35);
        errLbl.setTextFill(Color.RED);
        errLbl.setVisible(false);

        setupColoredCells();
        configActions();
        configBindings();
        configSelection();
    }

    private void configActions() {
        btnUp.setOnAction(e -> vm.up());
        btnDown.setOnAction(e -> vm.down());
        btnDuplicate.setOnAction(e -> vm.duplicate());
        btnDelete.setOnAction(e -> vm.delete());
        btnClear.setOnAction(e -> vm.clear());
    }

    private void configBindings() {
        btnUp.disableProperty().bind(vm.canUp().not());
        btnDown.disableProperty().bind(vm.canDown().not());
        btnDuplicate.disableProperty().bind(vm.canDuplicate().not());
        btnDelete.disableProperty().bind(vm.canDelete().not());
        btnClear.disableProperty().bind(vm.canClear().not());
        input.disableProperty().bind(vm.canEdit().not());
        errLbl.visibleProperty().bind(vm.canEdit().and(vm.isValidInputProperty().not()));
    }

    private void configSelection() {
        program.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            vm.programIndexProperty().setValue(newVal.intValue());
        });

        startLbl.textProperty().bind(
                Bindings.when(vm.ActionProperty().isNull())
                        .then("(aucune action sélectionnée)")
                        .otherwise(vm.startLblProperty())
        );

        endLbl.textProperty().bind(vm.endLblProperty());
        input.textProperty().bindBidirectional(vm.inputProperty(), new NumberStringConverter());

        vm.inputProperty().addListener((obs, oldVal, newVal) -> program.refresh());

        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !vm.isValidInputProperty().get()) {
                vm.parameterProperty().set(vm.ActionProperty().get().parameterProperty().get());
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
    }

    private Color actionColor(Action action) {
        switch (action.getType()) {
            case MOVE: return Color.DARKBLUE;
            case TURN: return Color.RED;
            case PEN:  return Color.GREEN;
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
                    setText(item.toString());
                    setTextFill(c);
                    setGraphic(new Circle(5, c));
                    setGraphicTextGap(8);
                }
            }
        });
    }

    public Label getProgramLabel() {
        return programLabel;
    }
}