package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Task;

import java.util.List;

public class SimulationController extends SceneController {

    @FXML
    ListView<String> queuesField;
    @FXML
    ListView<String> waitingQueueField;
    @FXML
    TextField textField;
    @FXML
    TextField peakHourField;
    @FXML
    TextField averageWaitingTimeField;
    @FXML
    TextField averageServiceTimeField;
    SimulationManager simulationManager = null;

    @FXML
    public void startSim() {
        Thread thread = new Thread(simulationManager);
        thread.start();
    }

    public synchronized void update(List<Server> tasks, List<Task> waitingQueue) {
        ObservableList<String> waitingQueueList = FXCollections.observableArrayList();
        ObservableList<String> queueList = FXCollections.observableArrayList();
        for (Task task : waitingQueue) {
            waitingQueueList.add(task.toString());
        }

        textField.setText(String.valueOf(simulationManager.currentTime));

        for (Server task : tasks) {
            queueList.add(task.toString());
        }
        this.queuesField.setItems(queueList);
        this.waitingQueueField.setItems(waitingQueueList);
    }

    public void stopSimulation() {
        this.simulationManager.stopThread();
    }

    public void setSimulationManager(SimulationManager sim) {
        this.simulationManager = sim;
    }

    public void setFinalValues(int peakHour, float averageWaitingTime, float averageServiceTime) {
        this.peakHourField.setText(String.valueOf(peakHour));
        this.averageWaitingTimeField.setText(String.valueOf(averageWaitingTime));
        this.averageServiceTimeField.setText(String.valueOf(averageServiceTime));
    }

}
