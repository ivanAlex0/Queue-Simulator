package model;

import controller.Server;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        Server server = null;
        int minWait = Integer.MAX_VALUE;
        for (Server server1 : servers) {
            if (server1.getWaitingPeriod().get() < minWait) {
                server = server1;
                minWait = server1.getWaitingPeriod().get();
            }
        }

        assert server != null;
        server.addTask(t);
    }
}
