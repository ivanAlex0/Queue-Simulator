import controller.SceneController;
import controller.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/mainMenu.fxml"));
        primaryStage.setTitle("Queue Simulator");
        primaryStage.setScene(new Scene(root, 650, 550));
        SceneController.stage = primaryStage;
        SceneController.scene = primaryStage.getScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
