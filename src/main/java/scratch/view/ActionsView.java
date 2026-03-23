package scratch.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

    public ActionsView(ActionsViewModel vm) {
        this.vm = vm;

        actions.setItems(vm.getActions());

        getChildren().addAll(actions, btnAddToProgram);

        setSpacing(15);
        setPrefSize(250, 550);

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
    }

    private void configBindings() {
        btnAddToProgram.disableProperty().bind(vm.canAdd().not());
    }

    private void configSelection() {
        actions.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            vm.actionIndexProperty().setValue(newVal.intValue());
        });
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