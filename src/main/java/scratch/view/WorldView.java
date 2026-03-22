package scratch.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import scratch.viewmodel.ActionsViewModel;

public class WorldView extends VBox {

    private final ActionsViewModel vm;

    private final Label sceneLabel = new Label("Scène");
    private final Pane scenePane = new Pane();
    private final Button loadBtn = new Button("Charger");
    private final Button executeBtn = new Button("Executer");

    private Polygon turtle;
    private Circle headTurtle;

    public WorldView(ActionsViewModel vm) {
        this.vm = vm;

        scenePane.setPrefSize(500, 500);
        scenePane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        setPrefWidth(500);

        getChildren().addAll(scenePane, loadBtn, executeBtn);

        loadBtn.textProperty().bind(vm.loadButtonTextProperty());
        executeBtn.textProperty().bind(vm.runButtonTextProperty());

        configActions();
        configBindings();

        drawGrid();
        drawTurtle(250, 250, 0);
    }

    private void configActions() {
        loadBtn.setOnAction(e -> {
            vm.loadOrReset();
            refreshScene();
        });

        executeBtn.setOnAction(e -> {
            vm.execOrNext();
            refreshScene();
        });
    }

    private void configBindings() {
        loadBtn.disableProperty().bind(vm.canLoad().not());
        executeBtn.disableProperty().bind(vm.canRun().not());
    }

    private void drawGrid() {
        double step = 50;

        for (double x = 0; x <= 500; x += step) {
            Line line = new Line(x, 0, x, 500);
            line.setStroke(Color.LIGHTBLUE);
            scenePane.getChildren().add(line);
        }

        for (double y = 0; y <= 500; y += step) {
            Line line = new Line(0, y, 500, y);
            line.setStroke(Color.LIGHTBLUE);
            scenePane.getChildren().add(line);
        }
    }

    private void drawTurtle(double x, double y, double angle) {
        double TURTLE_SIZE = 15;
        double HEAD_SIZE = 3;

        double xP1 = x + TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.cos(Math.toRadians(angle + 90));
        double yP1 = y - TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.sin(Math.toRadians(angle + 90));

        double xP2 = x - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle));
        double yP2 = y + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle));

        double xP3 = x + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle));
        double yP3 = y - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle));

        turtle = new Polygon(xP1, yP1, xP2, yP2, xP3, yP3);
        turtle.setFill(Color.DEEPSKYBLUE);
        turtle.setStroke(Color.BLACK);

        headTurtle = new Circle(HEAD_SIZE);
        headTurtle.setFill(Color.DARKRED);
        headTurtle.setTranslateX(xP1);
        headTurtle.setTranslateY(yP1);

        scenePane.getChildren().addAll(turtle, headTurtle);
    }

    public void refreshScene() {
        scenePane.getChildren().clear();
        drawGrid();

        for (var s : vm.getSegments()) {
            Line line = new Line(
                    s.getStart().getX(), s.getStart().getY(),
                    s.getEnd().getX(), s.getEnd().getY()
            );
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
            scenePane.getChildren().add(line);
        }

        drawTurtle(vm.getPosX(), vm.getPosY(), vm.getAngle());
    }

    public Label getSceneLabel() {
        return sceneLabel;
    }
}