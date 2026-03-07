package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.converter.NumberStringConverter;
import scratch.model.*;
import scratch.viewmodel.ActionsViewModel;

public class MainView extends VBox {

    private final ActionsViewModel vm;

    private final ListView<Action> actions = new ListView<>();
    private final ListView<Action> program = new ListView<>();
    private final Pane scenePane = new Pane();
    private Polygon turtle;
    private Circle headTurtle;


    // Hbox pour que les button soient aligner horizontalement
    private final HBox labelsBox = new HBox(30);

    // Hbox qui contient trois vBox :
        // La liste des actions de base
        // Le programme
        // L'espace pour le monde
    private final HBox bodyBox = new HBox();

    // trois vBox que je met dans le bodyBox
    private final VBox actionsBox = new VBox();
    private final VBox programBox = new VBox();
    private final VBox sceneBox = new VBox();

    // A l'interieur du box du programme :
        // un vbox pour mettre des boutons + details de l'action
    private final VBox prgmInnerVbox = new VBox();
    // Pour aligner les boutons horizontalement
    private final HBox prgmBtnsHbox = new HBox();
    // Pour les détails de l'action
    // private final HBox editBox = new HBox();

    private final HBox innerEditBox = new HBox();
    private final TextField input = new TextField();
    private final TitledPane actionDetails = new TitledPane("Détails de l'action",input);
    private final Label startLbl = new Label();
    private final Label endLbl = new Label();
    private final Label errLbl = new Label();

    private final Label actionsLabel = new Label("Palette d'actions");
    private final Label programLabel = new Label("Progrmme");
    private final Label sceneLabel = new Label("Scène");

    private final Button btnFile = new Button("File");
    private final Button btnAddToProgram = new Button("Ajouter au programme");
    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnDuplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider tout");
    private final Button loadBtn = new Button("Charger");
    private final Button executeBtn = new Button("Executer");

    public MainView(ActionsViewModel actionsViewModel) {
        this.vm = actionsViewModel;
        actions.setItems(vm.getActions());
        program.setItems(vm.getProgramActions());
        configLayouts();
        style();
        setupColoredCells();
        loadBtn.textProperty().bind(vm.loadButtonTextProperty());
        executeBtn.textProperty().bind(vm.runButtonTextProperty());

        configActions();
        configButtonsDisabling();
        configSelectionModels();
        drawGrid();
        drawTurtle(250,250,0);
    }

    public void configLayouts() {

        actionsBox.getChildren().addAll(actions, btnAddToProgram);


        prgmBtnsHbox.getChildren().addAll(btnUp, btnDown, btnDuplicate, btnDelete, btnClear);
        startLbl.setText("(aucune action sélectionnée)");
        innerEditBox.getChildren().addAll(startLbl, input, endLbl, errLbl);
        actionDetails.setContent(innerEditBox);
        prgmInnerVbox.getChildren().addAll(prgmBtnsHbox, actionDetails);
        programBox.getChildren().addAll(program, prgmInnerVbox);

        // monde
        sceneBox.getChildren().addAll(scenePane);
        sceneBox.getChildren().addAll(loadBtn,executeBtn);

        labelsBox.getChildren().addAll(actionsLabel, programLabel,sceneLabel);
        bodyBox.getChildren().addAll(actionsBox, programBox, sceneBox);
        this.getChildren().addAll(btnFile,labelsBox, bodyBox);
    }

    public void style() {


        actionsLabel.setPadding(new Insets(5,165,5,10));
        programLabel.setPadding(new Insets(5,340,5,0));
        prgmBtnsHbox.setSpacing(15);

//        bodyBox.setAlignment(Pos.CENTER);
        bodyBox.setSpacing(30);
        bodyBox.setPadding(new Insets(5,10,20,10));
        // style des actions de base
        actionsBox.setPrefSize(250, 550);
        actionsBox.setSpacing(15);

        input.setPrefWidth(35);
        innerEditBox.setSpacing(10);
        // style du programme
        programBox.setSpacing(15);

        // style du box a l'interieur du programme
        prgmInnerVbox.setSpacing(15);
        // style du monde
        sceneBox.setPrefWidth(500);
        scenePane.setPrefSize(500, 500);
        scenePane.setStyle("-fx-border-color: black; -fx-border-width: 2;");

    }

    private void configActions() {
        btnAddToProgram.setOnAction(e -> vm.addAction());
        btnUp.setOnAction(e -> vm.up());
        btnDown.setOnAction(e -> vm.down());
        btnDuplicate.setOnAction(e -> vm.duplicate());
        btnDelete.setOnAction(e -> vm.delete());
        btnClear.setOnAction(e -> vm.clear());
        loadBtn.setOnAction(e-> {
            vm.loadOrReset();
            refreshScene();
        });
       /* executeBtn.setOnAction( e -> {
            vm.execute();
            refreshScene();
        });*/
        executeBtn.setOnAction(e -> {
            vm.execOrNext();
            refreshScene();
        });
        this.actions.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                vm.addAction();
            }
        });
    }

    private void configButtonsDisabling() {
        btnAddToProgram.disableProperty().bind(vm.canAdd().not());
        btnUp.disableProperty().bind(vm.canUp().not());
        btnDown.disableProperty().bind(vm.canDown().not());
        btnDuplicate.disableProperty().bind(vm.canDuplicate().not());
        input.disableProperty().bind(vm.canEdit().not());
        btnDelete.disableProperty().bind(vm.canDelete().not());
        btnClear.disableProperty().bind(vm.canClear().not());
        loadBtn.disableProperty().bind(vm.canLoad().not());
        executeBtn.disableProperty().bind(vm.canRun().not());
    }

    private void configSelectionModels() {
        actions.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal ) -> {
            vm.actionIndexProperty().setValue(newVal.intValue());
        });

        program.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            this.vm.programIndexProperty().setValue(newVal.intValue());
            // System.out.println(vm.commandeProperty());
        });
        startLbl.textProperty().bind(
                Bindings.when(vm.commandeProperty().isNull())
                        .then("(aucune action sélectionnée)")
                        .otherwise(vm.startLblProperty())
        );
        endLbl.textProperty().bind(vm.endLblProperty());
        input.textProperty().bindBidirectional(vm.inputProperty(),new NumberStringConverter());
        this.vm.programIndexProperty().addListener((obs, oldVal, newVal) -> {
            this.program.getSelectionModel().select(newVal.intValue());
        });

        this.vm.highlightIdxProperty().addListener((obs, oldV, newV) -> {
            int idx = newV.intValue();
            if (idx >= 0 && idx < program.getItems().size()) {
                program.getSelectionModel().select(idx);
                program.scrollTo(idx);
            }
        });
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
    private void refreshScene() {
        // tout supprimer puis redessiner la grille
        scenePane.getChildren().clear();
        drawGrid();
        // dessiner les segments
        for (var s : vm.getMonde().getSegments()) {
            Line line = new Line(
                    s.getStart().getX(), s.getStart().getY(),
                    s.getEnd().getX(), s.getEnd().getY()
            );
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
            scenePane.getChildren().add(line);
        }
        // dessiner la tortue à sa position actuelle
        double x = vm.getMonde().getPersonnage().getPosition().getX();
        double y = vm.getMonde().getPersonnage().getPosition().getY();
        double angle = vm.getMonde().getPersonnage().getAngle();

        drawTurtle(x, y, angle);
    }
    private Color actionColor(Action action) {
        if(action instanceof Move) return Color.DARKBLUE;
        if(action instanceof Turn) return Color.RED;
        if(action instanceof Pen)  return Color.GREEN;

        return Color.BLACK;
    }
    private void setupColoredCells() {

        actions.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Color c = actionColor(item);

                    setText(item.toString());
                    setTextFill(c);


                    Circle dot = new Circle(5, c);
                    setGraphic(dot);
                    setGraphicTextGap(8);
                }
            }
        });

        program.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Color c = actionColor(item);

                    setText(item.toString());
                    setTextFill(c);

                    Circle dot = new Circle(5, c);
                    setGraphic(dot);
                    setGraphicTextGap(8);
                }
            }
        });
    }
}
