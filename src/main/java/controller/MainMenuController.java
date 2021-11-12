package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends SceneController {

    @FXML
    TextField NField;
    @FXML
    TextField QField;
    @FXML
    TextField tMaxField;
    @FXML
    TextField tMinArrivalField;
    @FXML
    TextField tMaxArrivalField;
    @FXML
    TextField tMinServiceField;
    @FXML
    TextField tMaxServiceField;
    int N, Q, tMax, tMinArrival, tMaxArrival, tMinService, tMaxService;

    @FXML
    public void startSimulation(ActionEvent actionEvent) {
        //parsing the data from the input
        if (!parseInput()) {
            //show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bad input");
            alert.setHeaderText("Only integers accepted");
            alert.setContentText("Input cannot be parsed");

            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            Parent root = loader.load();
            SimulationController simulationController = loader.getController();
            SimulationManager simulationManager = new SimulationManager(simulationController);
            simulationManager.simulationSetup(N, Q, tMax, tMinArrival, tMaxArrival, tMinService, tMaxService);
            simulationController.setSimulationManager(simulationManager);
            if (scene == null) {
                scene = new Scene(root);
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else
                stage.getScene().setRoot(root);
            stage.setOnCloseRequest(e -> simulationController.stopSimulation());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public boolean parseInput() {
        try {
            N = Integer.parseInt(this.NField.getText());
            Q = Integer.parseInt(this.QField.getText());
            tMax = Integer.parseInt(this.tMaxField.getText());
            tMaxArrival = Integer.parseInt(this.tMaxArrivalField.getText());
            tMinArrival = Integer.parseInt(this.tMinArrivalField.getText());
            tMaxService = Integer.parseInt(this.tMaxServiceField.getText());
            tMinService = Integer.parseInt(this.tMinServiceField.getText());
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}
