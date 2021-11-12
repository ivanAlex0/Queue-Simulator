package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private final List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(maxTasksPerServer, i);
            Thread t = new Thread(server);
            servers.add(server);
            t.start();
        }
    }

    /**
     * The function changes the strategy according to the policy parameter
     * @param policy - the new SelectionPolicy
     */
    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    /**
     * The function stops all the servers that are currently running
     */
    public void stopServers() {
        for (Server server : this.servers) {
            server.stop();
        }
    }

    /**
     * The function dispatches the Task t to one server
     * @param t - the Task to be dispatched
     */
    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }
}

