package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import java.util.List;

public interface Strategy {
    public void addTask(List<Server> servers, Task t, SimulationFrame frame) throws InterruptedException;
    public enum SelectionPolicy
    {
        SHORTEST_QUEUE,SHORTEST_TIME;
    }
}
