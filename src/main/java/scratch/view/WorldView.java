package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import scratch.model.SegmentTeleportation;
import scratch.model.VarDeclaration;
import scratch.viewmodel.WorldViewModel;

public class WorldView extends VBox {
    private final WorldViewModel vm;

    private final Label sceneLabel = new Label("Scène");

    // zone graphique où on dessine la grille, les segments et la tortue
    private final Pane scenePane = new Pane();

    private final VBox stateVBox = new VBox();
    private final VBox executeVBox = new VBox();

    private Text turtlePos = new Text();
    private final CheckBox showTeleportation = new CheckBox("afficher la téléportation");
    private final HBox radioHBox = new HBox();
    private final RadioButton radioAuto = new RadioButton("Execution automatique");
    private final RadioButton radioManu = new RadioButton("Execution manuelle");
    private final ToggleGroup executeToggle = new ToggleGroup();

    private final HBox btnHBox = new HBox();
    private final Button loadBtn = new Button("Charger");
    private final Button executeBtn = new Button("Executer");
    private final Button stopBtn = new Button("Arrêter");

    private final Slider speedSlider = new Slider(0.01, 5, 0.5);

    private Polygon turtle;
    private Circle headTurtle;

     public WorldView(WorldViewModel vm) {
        this.vm = vm;

        scenePane.setPrefSize(vm.getWorldSize(), vm.getWorldSize());
        scenePane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        setPrefWidth(vm.getWorldSize());

        configStateZone();
        configActions();
        configBindings();

        drawGrid();
        drawTurtle(vm.getWorldOriginX(), vm.getWorldOriginY(), vm.getAngle());

        // refresh la scène si la position ou l'angle changent
        vm.getPosX().addListener((obs, oldV, newV) -> refreshScene());
        vm.getPosY().addListener((obs, oldV, newV) -> refreshScene());
        vm.getAngleProperty().addListener((obs, oldV, newV) -> refreshScene());

        showTeleportation.selectedProperty().addListener((obs, oldVal, newVal) -> refreshScene());

    }

    private void configStateZone() {
        setSpacing(10);

        turtlePos.textProperty().bind(vm.turtlePosition());
        Label tableLabel = new Label("Variables");
        TableView<VarDeclaration> varTable = varTable();
        HBox radioHBox = radioContainer();
        HBox btnHBox = btnContainer();
        Slider slider = sliderContainer();

        stateVBox.getChildren().addAll(turtlePos, tableLabel, varTable);
        stateVBox.setPrefSize(150, 200);
        stateVBox.setStyle("-fx-border-color: black; -fx-border-width: 0.5;");
        stateVBox.setPadding(new Insets(5));
        stateVBox.setSpacing(5);

        executeVBox.getChildren().addAll(radioHBox, btnHBox, slider);
        executeVBox.setSpacing(10);

        getChildren().addAll(scenePane, stateVBox, showTeleportation, executeVBox);
    }

    // endroit qui affiche les variables et leurs valeurs
    private TableView<VarDeclaration> varTable() {
        TableView<VarDeclaration> varTable = new TableView<>();
        TableColumn<VarDeclaration, String> nom = new TableColumn<>("Nom");
        nom.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        TableColumn<VarDeclaration, Number> valeur = new TableColumn<>("Valeur");
        valeur.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        varTable.getColumns().addAll(nom, valeur);
        varTable.setItems(vm.getVariables());
        varTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return varTable;
    }

    // choix mode d'exécution
    private HBox radioContainer() {
        radioAuto.setToggleGroup(executeToggle);
        radioManu.setToggleGroup(executeToggle);
        radioManu.setSelected(true);
        radioHBox.getChildren().addAll(radioAuto, radioManu);
        radioHBox.setAlignment(Pos.CENTER);
        radioHBox.setSpacing(10);

        return radioHBox;
    }

    private HBox btnContainer() {
        loadBtn.textProperty().bind(vm.loadButtonTextProperty());
        executeBtn.textProperty().bind(vm.runButtonTextProperty());
        stopBtn.visibleProperty().bind(radioAuto.selectedProperty());

        btnHBox.getChildren().addAll(loadBtn, executeBtn, stopBtn);
        btnHBox.setAlignment(Pos.CENTER);

        return btnHBox;
    }

    // curseur de vitesse
    private Slider sliderContainer() {
        speedSlider.visibleProperty().bind(radioAuto.selectedProperty());
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(3);
        speedSlider.valueProperty().bindBidirectional(vm.speedProperty());
        return speedSlider;
    }

    private void configActions() {
        loadBtn.setOnAction(e -> {
            vm.loadOrReset();
            refreshScene();
        });

        executeBtn.setOnAction(e -> {
            if (radioAuto.isSelected()) {
                vm.startAutoExec();
                refreshScene();
            } else {
                vm.execOrNext();
                refreshScene();
            }
        });

        stopBtn.setOnAction(e -> {
            vm.stopExec();
            refreshScene();
        });
    }

    private void configBindings() {
        loadBtn.disableProperty().bind(vm.canLoadProperty().not());
        executeBtn.disableProperty().bind(vm.canRun().not().or(vm.hasErrorProperty()));
        stopBtn.disableProperty().bind(vm.isRunningProperty().not());

        showTeleportation.selectedProperty().bindBidirectional(vm.showTeleportationLinesProperty());
    }

    private void drawGrid() {
        double step = 50;

        for (double x = 0; x <= vm.getWorldSize(); x += step) {
            Line line = new Line(x, 0, x,  vm.getWorldSize());
            line.setStroke(Color.LIGHTBLUE);
            scenePane.getChildren().add(line);
        }

        for (double y = 0; y <= vm.getWorldSize(); y += step) {
            Line line = new Line(0, y,  vm.getWorldSize(), y);
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

    // redessine entièrement la scène du Monde
    public void refreshScene() {
        scenePane.getChildren().clear();

        // redessine la grille
        drawGrid();

        // redessine tous les segments tracés
        for (var s : vm.getSegments()) {
            if (s.isDashed()) {
                if (showTeleportation.isSelected()) {
                    Line line = new Line(
                            s.getStart().getX().get(), s.getStart().getY().get(),
                            s.getEnd().getX().get(), s.getEnd().getY().get()
                    );
                    line.getStrokeDashArray().addAll(10.0, 10.0);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(2);
                    scenePane.getChildren().add(line);
                }
            } else {
                Line line = new Line(
                        s.getStart().getX().get(), s.getStart().getY().get(),
                        s.getEnd().getX().get(), s.getEnd().getY().get()
                );
                line.getStrokeDashArray().clear();
                line.setStroke(Color.RED);
                line.setStrokeWidth(2);
                scenePane.getChildren().add(line);
            }
        }
        // redessine la tortue à sa position actuelle
        drawTurtle(vm.getPosX().get(), vm.getPosY().get(), vm.getAngle());
    }

    public Label getSceneLabel() {
        return sceneLabel;
    }
}