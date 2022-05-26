package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import java.util.List;

public class TimeStrategy implements  Strategy{
    @Override
    public void addTask(List<Server> servers, Task t, SimulationFrame frame) {

          int min=Integer.MAX_VALUE;
          int coadaDorita=-1;
          //calculez perioada minima de stat la coada
          for(Server s:servers)
          {
              if(s.getWaitingPeriod().get()<min) {
                  min = s.getWaitingPeriod().get();
                  coadaDorita=servers.indexOf(s);
              }
          }
          //il pun la coada cu timpul acela minim
          servers.get(coadaDorita).addTask(t);
          //aici imi dau seama de ce coada folosesc
            frame.getCoziDisplay().get(coadaDorita).setText(frame.getCoziDisplay().get(coadaDorita).getText()+"  "+"OM ");
          //afisez
        System.out.println("Clientul meu actual:"+t);
        System.out.println("Indexul cozii in care il bag:"+coadaDorita);

        System.out.println("Waiting time pt cozi este:");
        for(Server s: servers)
            System.out.println(s.getWaitingPeriod());
    }
}
