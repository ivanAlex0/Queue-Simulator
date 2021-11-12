package controller;

import lombok.Getter;
import lombok.Setter;
import model.Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private final AtomicBoolean working = new AtomicBoolean(false);
    int id;

    public Server(int capacity, int id) {
        tasks = new ArrayBlockingQueue<>(capacity);
        waitingPeriod = new AtomicInteger(0);
        this.id = id;
    }

    /**
     * The function adds the queue of tasks
     * @param newTask - the task to be added to the queue
     */
    public void addTask(Task newTask) {
        try {
            tasks.put(newTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newTask.setFinalTime(newTask.getArrivalTime() + newTask.getProcessingTime() + this.waitingPeriod.get());
        waitingPeriod.getAndAdd(newTask.getProcessingTime());
    }

    /**
     * The function is the run function that had to be Overridden from the Runnable interface
     * It handles the tasks in the queue and it removes them accordingly
     */
    @Override
    public void run() {
        working.set(true);
        while (working.get()) {
            try {
                if (!tasks.isEmpty()) {
                    Task task = tasks.element();
                    for (int interval = 0; interval < task.getProcessingTime(); interval++) {
                        Thread.sleep(1000);
                        this.waitingPeriod.getAndDecrement();
                    }
                    tasks.remove(task);
                    SimulationManager.averageWaitingTime += task.getFinalTime() - task.getArrivalTime();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Server " + this.id + " is dead");
    }

    /**
     * The function stops the current Thread
     */
    public void stop() {
        working.set(false);
    }

    @Override
    public String toString() {
        return "Server{" +
                "tasks=" + tasks +
                '}';
    }
}

