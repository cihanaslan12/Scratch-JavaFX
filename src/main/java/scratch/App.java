package scratch;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scratch.model.ActionList;
import scratch.model.Programme;
import scratch.view.ActionsView;
import scratch.view.MainView;
import scratch.viewmodel.ActionsViewModel;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
        Programme choosenActions = new Programme();
        ActionsViewModel actionsViewModel = new ActionsViewModel(choosenActions);

        Scene scene = new Scene(new MainView(actionsViewModel));
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}