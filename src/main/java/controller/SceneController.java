package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    public static Stage stage = null;
    public static Scene scene = null;

    /**
     * The Function changes the currentScene to the Scene from fxmlFilePath
     */
    public void changeScene(ActionEvent actionEvent, String fxmlFilePath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        if (scene == null) {
            scene = new Scene(parent);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else
            stage.getScene().setRoot(parent);
    }
}
