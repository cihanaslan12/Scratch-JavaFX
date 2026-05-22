package scratch.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scratch.model.Action;
import scratch.viewmodel.ActionsViewModel;

public class ActionsView extends VBox {

    private final ActionsViewModel vm;

    private final Label actionsLabel = new Label("Palette d'actions");
    private final ListView<Action> actions = new ListView<>();
    private final Button btnAddToProgram = new Button("Ajouter au programme");
    private final CheckBox advancedMode = new CheckBox("Mode avancé");

    public ActionsView(ActionsViewModel vm) {
        this.vm = vm;

        // lie la liste graphique aux action du ViewModel
        actions.setItems(vm.getActionList());

        getChildren().addAll(actions, btnAddToProgram, advancedMode);

        setSpacing(15);
        setPrefSize(150, 550);

        setupColoredCells();
        configActions();
        configBindings();
        configSelection();
    }

    private void configActions() {
        btnAddToProgram.setOnAction(e -> vm.addAction());

        actions.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                vm.addAction();
            }
        });

         advancedMode.selectedProperty().set(false);

         advancedMode.setOnAction(e -> vm.addRectToActionList(advancedMode.selectedProperty().get()));
    }

    private void configBindings() {
        btnAddToProgram.disableProperty().bind(vm.canAdd().not());
    }

    private void configSelection() {
        // met à jour l'index sélectionné dans le ViewModel
        actions.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            vm.actionIndexProperty().setValue(newVal.intValue());
        });
    }

    private Color actionColor(Action action) {
        switch (action.getType()) {
            case MOVE: return Color.DARKBLUE;
            case TURN_LEFT, TURN_RIGHT: return Color.RED;
            case PEN_DOWN, PEN_UP:  return Color.GREEN;
            case VAR_DECLARATION, VAR_ASSIGNMENT, VAR_INCREMENT, REPEAT, END_REPEAT: return Color.DARKORCHID;
            case DRAW_POLYGON, DRAW_RECTANGLE: return Color.ORANGE;
            default:   return Color.BLACK;
        }
    }

    private void setupColoredCells() {
        actions.setCellFactory(lv -> new ListCell<>() {
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

    public Label getActionsLabel() {
        return actionsLabel;
    }
}