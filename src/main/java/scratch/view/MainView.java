package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import scratch.viewmodel.ActionsViewModel;

import java.io.File;

public class MainView extends VBox {

    private final ActionsViewModel vm;

    private final VBox menuBarBox = new VBox();
    private final HBox mainBox = new HBox(30);

    private final ActionsView actionsView;
    private final ProgramView programView;
    private final WorldView worldView;

    private final MenuBar menuBar = new MenuBar();
    private final Menu menu = new Menu("File");
    private final MenuItem menuNew = new MenuItem("New...");
    private final MenuItem menuOpen = new MenuItem("Open...");
    private final MenuItem menuSaveAs = new MenuItem("Save As...");
    private final MenuItem menuExit = new MenuItem("Exit");

    public MainView(ActionsViewModel vm) {
        this.vm = vm;

        actionsView = new ActionsView(vm);
        programView = new ProgramView(vm);
        worldView = new WorldView(vm);

        configLayout();
        style();
        menuEvent();
    }

    private void configLayout() {
        menu.getItems().addAll(menuNew, menuOpen, menuSaveAs, menuExit);
        menuBar.getMenus().add(menu);
        menuBarBox.getChildren().add(menuBar);

        VBox actionsBox = new VBox(5);
        actionsBox.getChildren().addAll(
                actionsView.getActionsLabel(),
                actionsView
        );

        VBox programBox = new VBox(5);
        programBox.getChildren().addAll(
                programView.getProgramLabel(),
                programView
        );

        VBox worldBox = new VBox(5);
        worldBox.getChildren().addAll(
                worldView.getSceneLabel(),
                worldView
        );

        mainBox.getChildren().addAll(actionsBox, programBox, worldBox);

        getChildren().addAll(menuBarBox, mainBox);
    }

    private void style() {
        mainBox.setPadding(new Insets(5, 10, 20, 10));
    }

    private void menuEvent() {
        menuNew.setOnAction(e -> {
            vm.newProgram();
            worldView.refreshScene();
        });

        menuOpen.setOnAction(e -> openFile());
        menuSaveAs.setOnAction(e -> saveAs());
        menuExit.setOnAction(e -> vm.exitProgram());
    }

    private FileChooser myFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Scratch files", "*.src"));
        return fileChooser;
    }

    private void openFile() {
        File selectedFile = myFileChooser("Ouvrir").showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            vm.openFile(selectedFile);
            worldView.refreshScene();
        }
    }

    private void saveAs() {
        File selectedFile = myFileChooser("Enregistrer sous").showSaveDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            vm.saveFileAs(selectedFile);
        }
    }
}