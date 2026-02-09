package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scratch.model.Commande;
import scratch.viewmodel.ActionsViewModel;

public class MainView extends VBox {

    private final ActionsViewModel vm;

    private final ListView<Commande> actions = new ListView<>();
    private final ListView<Commande> program = new ListView<>();
    // pour tester
    private final ListView<Commande> scenTest = new ListView<>();

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
    private final HBox editBox = new HBox();

    private final Label actionsLabel = new Label("Palette d'actions");
    private final Label programLabel = new Label("Progrmme");
    private final Label sceneLabel = new Label("Scène");

    private final Button btnFile = new Button("File");
    private final Button btnAddToProgram = new Button("Ajouter au programme");
    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnduplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider tout");

    public MainView(ActionsViewModel actionsViewModel) {
        this.vm = actionsViewModel;
        actions.setItems(vm.getActions());
        program.setItems(vm.getProgramActions());
        configLayouts();
        style();

        configActions();
        configButtonsDisabling();
        configSelectionModels();
    }

    public void configLayouts() {

        actionsBox.getChildren().addAll(actions, btnAddToProgram);


        prgmBtnsHbox.getChildren().addAll(btnUp, btnDown, btnduplicate, btnDelete, btnClear);
        editBox.getChildren().add(new Label("Details de l'action"));
        prgmInnerVbox.getChildren().addAll(prgmBtnsHbox, editBox);
        programBox.getChildren().addAll(program, prgmInnerVbox);

        // monde
        sceneBox.getChildren().addAll(scenTest);

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


        // style du programme
        programBox.setSpacing(15);

        // style du box a l'interieur du programme
        prgmInnerVbox.setSpacing(15);
        // style du monde
        // sceneBox.setPrefSize(600, 600);
        sceneBox.setPrefWidth(650);
    }

    private void configActions() {
        btnAddToProgram.setOnAction(e -> vm.addAction());
        this.actions.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                vm.addAction();
            }
        });
    }

    private void configButtonsDisabling() {
        btnAddToProgram.disableProperty().bind(vm.canAdd().not());
    }

    private void configSelectionModels() {
        actions.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal ) -> {
            vm.actionIndexProperty().setValue(newVal.intValue());
        });
    }
}
