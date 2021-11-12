package controller;

import javafx.application.Platform;
import model.SelectionPolicy;
import model.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.PrintWriter;

public class SimulationManager implements Runnable {

    public int currentTime;
    public int timeLimit = 200;
    public int minArrivalTime = 10;
    public int maxArrivalTime = 101;
    public int minProcessingTime = 3;
    public int maxProcessingTime = 10;
    public int numberOfServers = 20;
    public int numberOfClients = 1000;
    public static float averageWaitingTime;
    public float averageServiceTime;
    public int peakHour, maximumClients = 0;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private final SimulationController simulationController;

    private Scheduler scheduler;
    private List<Task> generatedTasks;
    PrintWriter file;

    public SimulationManager(SimulationController controller) {
        simulationController = controller;

        try {
            file = new PrintWriter("file.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function generates N random tasks and sorts them
     */
    private void generateNRandomTasks() {
        generatedTasks = new ArrayList<>();
        Random random = new Random();
        for (int index = 0; index < numberOfClients; index++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;
            int processingTime = random.nextInt(maxProcessingTime - minProcessingTime) + minProcessingTime;
            averageServiceTime += processingTime;
            Task t = new Task(index, arrivalTime, processingTime);
            this.generatedTasks.add(t);
        }

        averageServiceTime /= numberOfClients;
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    /**
     * The main run method - it handles the tasks, servers and also the UI update
     */
    @Override
    public void run() {
        currentTime = 0;
        while (currentTime < timeLimit) {
            boolean finished = true;
            for (Server server : scheduler.getServers()) {
                if (server.getTasks().size() > 0) {
                    finished = false;
                }
            }

            if (finished && generatedTasks.size() == 0) {
                break;
            }

            getAndDispatch();
            updateAndRemove();

            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scheduler.stopServers();
        averageWaitingTime = averageWaitingTime / numberOfClients;
        simulationController.setFinalValues(peakHour, averageWaitingTime, averageServiceTime);
    }

    /**
     * The function gets the parameters from the UI and sets them
     *
     * @param N           - number of clients
     * @param Q           - number of queues
     * @param tMax        - maximum time
     * @param tMinArrival - minimum time of arrival
     * @param tMaxArrival - maximum time of arrival
     * @param tMinService - minimum time of service
     * @param tMaxService - maximum time of service
     */
    public void simulationSetup(int N, int Q, int tMax, int tMinArrival, int tMaxArrival, int tMinService,
                                int tMaxService) {
        this.numberOfClients = N;
        this.numberOfServers = Q;
        this.timeLimit = tMax;
        this.minArrivalTime = tMinArrival;
        this.maxArrivalTime = tMaxArrival;
        this.minProcessingTime = tMinService;
        this.maxProcessingTime = tMaxService;

        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomTasks();
    }

    /**
     * The function stops the current Thread
     */
    public void stopThread() {
        this.currentTime = timeLimit;
    }

    /**
     * Helper functions for the run method
     * --both from below
     */
    public void getAndDispatch() {
        System.out.println("Time: " + currentTime);
        int finalCurrentTime = currentTime;
        List<Task> taskList = generatedTasks
                .stream()
                .filter(t -> t.getArrivalTime() == finalCurrentTime)
                .collect(Collectors.toList());

        generatedTasks.removeAll(taskList);

        for (Task generatedTask : generatedTasks) {
            System.out.println(generatedTask.getID() + " " + generatedTask.getArrivalTime() + " " + generatedTask.getProcessingTime());
        }

        for (Task task : taskList) {
            scheduler.dispatchTask(task);
        }
    }

    public void updateAndRemove() {
        Platform.runLater(() -> simulationController.update(scheduler.getServers(), generatedTasks));
        int currentClients = 0;
        for (Server server : scheduler.getServers()) {
            System.out.println(server.toString());
            currentClients += server.getTasks().size();
        }

        //calculating peak hour
        if (currentClients > maximumClients) {
            peakHour = currentTime;
            maximumClients = currentClients;
        }
    }
}
