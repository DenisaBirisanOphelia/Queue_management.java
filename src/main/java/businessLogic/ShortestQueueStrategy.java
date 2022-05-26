package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import java.util.List;
import java.util.TimerTask;

public class ShortestQueueStrategy  implements  Strategy {
    @Override
    public  synchronized  void  addTask(List<Server> servers, Task t, SimulationFrame frame) throws InterruptedException {
              //banuiesc ca aici iau toate cozile si o caut pe cea cu size u cel mai mic
        int min=Integer.MAX_VALUE;
        int indexulDorit=0;
        for(int i=0;i<servers.size();i++)
        {
            if(servers.get(i).getTasks().size()<min) {
                min =servers.get(i).getTasks().size();
                indexulDorit=i;
            }
        }
        //il pun la coada cu timpul acela minim
        servers.get(indexulDorit).addTask(t);

        frame.getCoziDisplay().get(indexulDorit).setText(frame.getCoziDisplay().get(indexulDorit).getText()+"  "+"OM ");

        System.out.println("Clientul meu actual:"+t);
        System.out.println("Indexul cozii in care il bag:"+indexulDorit);
        //servers.get(indexulDorit).getTasks().add(t);


    }


}
