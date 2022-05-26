package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import javax.swing.plaf.basic.BasicTreeUI;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    //trimite taskurile la servere conform strategiei stabilite
    private List<Server> servers;
    private int nrMaxServers;
    private int maxTasksPerServer;
    private Strategy strategy;


    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void setNrMaxServers(int nrMaxServers) {
        this.nrMaxServers = nrMaxServers;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public Scheduler(int nrMaxServers, int maxTasksPerServer) {
        for(int i=0;i<nrMaxServers;i++)
        {//creeaza server object
            Server serverel=new Server(new AtomicInteger(0));
            //creeaza un thread cu obiectul
            Thread t=new Thread(serverel);
            t.start();
        }
    }
    public void changeStrategy(Strategy.SelectionPolicy policy)
    {
        if(policy== Strategy.SelectionPolicy.SHORTEST_QUEUE)
                 strategy=new ShortestQueueStrategy();
        if(policy==Strategy.SelectionPolicy.SHORTEST_TIME)
            strategy=new TimeStrategy();
    }
    public void dispatchTask(Task t,SimulationFrame frame) throws InterruptedException { //cheama metoda addTask de la strategia aleasa
        strategy.addTask(servers,t,frame);
    }

    public List<Server> getServers() {
        return servers;
    }


}
