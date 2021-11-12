package model;

import controller.Server;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        //TO DO
        Server server = null;
        int minLen = Integer.MAX_VALUE;
        for (Server server1 : servers) {
            if (server1.getTasks().size() < minLen) {
                server = server1;
                minLen = server1.getTasks().size();
            }
        }

        assert server != null;
        server.addTask(t);
    }
}
