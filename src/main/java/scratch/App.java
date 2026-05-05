package scratch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scratch.model.*;
import scratch.view.MainView;
import scratch.viewmodel.ActionsViewModel;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new MainView());
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}