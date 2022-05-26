package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class SimulationManager implements  Runnable{
    //datele citite din interfata grafica
    int nrClients;
    int nrServers;
    int timpMaxSimulare;
    int minArrivalTime;
    int maxArrivalTime;
    int minServiceTime;
    int maxServiceTime;

    //ceea ce imi trebuie pentru cele 3 calcule
    private  List<Integer> listaWaitingTime=new ArrayList<>();
    private List<Integer> listaPeakTimes=new ArrayList<>();



    AtomicInteger currentTime=new AtomicInteger(0);

    public void setNrClients(int nrClients) {
        this.nrClients = nrClients;
    }

    public void setNrServers(int nrServers) {
        this.nrServers = nrServers;
    }

    public void setTimpMaxSimulare(int timpMaxSimulare) {
        this.timpMaxSimulare = timpMaxSimulare;
    }

    public void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public void setMinServiceTime(int minServiceTime) {
        this.minServiceTime = minServiceTime;
    }

    public void setMaxServiceTime(int maxServiceTime) {
        this.maxServiceTime = maxServiceTime;
    }

    //lista de clienti
    private List<Task> listaClienti=new ArrayList<Task>();
    private Strategy.SelectionPolicy selectionPolicy;
    //entitatea responsabila cu managementul cozilor si al clientilor
    private Scheduler scheduler;
    //interfata grafica
    private SimulationFrame frame;

    public float getAverageWaitingTime()
    {
        float S=0;
        for(int i=0;i<listaWaitingTime.size();i++)//adun  waiting times si impart la nrSecunde
             S+=listaWaitingTime.get(i);

        return S/(float)timpMaxSimulare;
    }
    public float getAverageServiceTime() {
       //calculez serviceTime mediu per client, avand toti clientii cunoscuti
        float sum=0;
        for(Task client:listaClienti)
            sum+=client.getServiceTime();

        return sum/nrClients;
    }
 public int getPeakTime()
 { int max=Integer.MIN_VALUE;
     int index=-1;
     for(int i=0;i<listaPeakTimes.size();i++)
         if(listaPeakTimes.get(i)>max) {
             max = listaPeakTimes.get(i);
             index=i;
         }
     return index;
 }
    public SimulationManager(int nrClients, int nrServers, int timpMaxSimulare, int minArrivalTime,
                             int maxArrivalTime, int minServiceTime, int maxServiceTime,SimulationFrame simp,String strat) {
        this.frame=simp;
        this.nrClients = nrClients;
        this.nrServers = nrServers;
        this.timpMaxSimulare = timpMaxSimulare;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        //initializeaza schedulerul
        this.scheduler=new Scheduler(nrServers,100);//maxim 100 tasks/coada


        //initializeaza strategia
        Strategy strategy=new ShortestQueueStrategy();
        if(strat.equals("SHORTEST_QUEUE")) selectionPolicy= Strategy.SelectionPolicy.SHORTEST_QUEUE;
        else selectionPolicy=Strategy.SelectionPolicy.SHORTEST_TIME;
        scheduler.changeStrategy(selectionPolicy);

        //initializeaza frameul
       // this.frame=new SimulationFrame(this);
        //generez nr clienti si ii pun in lista de clienti
        generateNRandomTasks();
        //creeaza si incepe threadurile
        List<Server> listaS=new ArrayList<>();
        for(int i=0;i<nrServers;i++) {
             Server s=new Server(new AtomicInteger(0));
            listaS.add(s);

        }
        scheduler.setServers(listaS);
        scheduler.setMaxTasksPerServer(1000);
        scheduler.setNrMaxServers(1000);
        for(int i=0;i<nrServers;i++)
        {
            Thread t = new Thread(listaS.get(i));
            t.start();
        }



    }
    private void generateNRandomTasks()
    {//genereaza N=nrClients random tasks, adica clientii si sorteaza-i dupa arrivalTime
        List<Task> listuta=new ArrayList<Task>();
        System.out.println("Nr clienti e"+nrClients);
        System.out.println(nrServers);
        System.out.println(minArrivalTime);
        System.out.println(maxArrivalTime);
        System.out.println(minServiceTime);
        System.out.println(maxServiceTime);
        System.out.println(timpMaxSimulare);
        for(int i=0;i<nrClients;i++)
        {
            int id=i+1;
            //asa generez ceva random in intervalul acela minArrival  si maxArrival
            int tpArrival= (int) Math.floor(Math.random()*(maxArrivalTime-minArrivalTime+1)+minArrivalTime);
            int timpService= (int) Math.floor(Math.random()*(maxServiceTime-minServiceTime+1)+minServiceTime);
            Task clientel=new Task(id,tpArrival,timpService);
            listuta.add(clientel);
        }

        Collections.sort(listuta);//le ordonez
       /* listuta.get(0).setArrivalTime(2);
        listuta.get(1).setArrivalTime(3);
        listuta.get(2).setArrivalTime(5);
        listuta.get(3).setArrivalTime(8);*/
        //setez lista ordonata de clienti
        this.listaClienti=listuta;

    }

    @Override
    public void run() {

         CreareFisier fisi=new CreareFisier();
         fisi.creeazaFisier("LogEvents");

        while(currentTime.get()<=timpMaxSimulare)
        {       if(currentTime.get()==0)
            frame.setAvgService(String.valueOf(getAverageServiceTime()));
            System.out.println("Timpul curent este:"+currentTime);
            //modific si in interfata
            frame.setCurrentTime(String.valueOf(currentTime));

            fisi.scrie("Time:"+currentTime);
            System.out.println("Lista clienti:");

            //itereaza prin lista de tasks=clienti si alege taskurile care au arrival time=currentTime
            int clientulDorit=-1;
            for(int i=0;i<listaClienti.size();i++)
            {
                if(currentTime.get()>=listaClienti.get(i).getArrivalTime()) clientulDorit=i;
            }
            //trimit taskul la o coada,chemand metoda de dispatch din Scheduler
            if(clientulDorit!=-1) {
                try {
                    //aici il adaug incoada
                    scheduler.dispatchTask(listaClienti.get(clientulDorit),frame);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //il sterg din lista de clienti
                listaClienti.remove(clientulDorit);
            }
            //verific daca timpArrival+timpService e egal pentru vreun client,daca da,il sterg din coada
            for(Server s: scheduler.getServers())//pt fiecare coada verific asta
            {
                if(!s.getTasks().isEmpty()) {
                    for(Task client:s.getTasks())
                       if(client.getServiceTime()==0)// e gat cu clientul asta
                       {//actualizez si textFieldul
                            int indexulVrut=scheduler.getServers().indexOf(s);
                           System.out.println("Coada de la care vr sa elimin:"+indexulVrut);
                           frame.getCoziDisplay().get(indexulVrut).setText(frame.getCoziDisplay().get(indexulVrut).getText()
                                   .replaceFirst("OM",""));
                           //il scot si din coada lui

                       }

                }

            }


            //astept 1 s
            try {
              sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //aici incerc sa updatez service time de la fiecare+ sa sterg clientii cu serviceTime=0;



            fisi.scrie("Waiting clients:");
            for(Task t:listaClienti) {
                System.out.println(t);
                fisi.scrie("("+t.getId()+", "+t.getArrivalTime()+", "+t.getServiceTime()+" )");
            }
            int j=0;
            for(Server s: scheduler.getServers())
            {   if(!s.getTasks().isEmpty() ) {
                //aici scrie in fisier
                fisi.scrie("Queue #" + j + ": ");
                for(Task t:s.getTasks())
                    fisi.scrie("("+t.getId()+", "+t.getArrivalTime()+", "+t.getServiceTime()+" ) ");
            }
            else
            {
                fisi.scrie("Queue #"+j+" : closed");
            }
             j++;
            }
            //incrementez timpul curent
            currentTime.incrementAndGet();
            //aici scad waiting time cu 1 din fiecare coada
            for(Server s: scheduler.getServers()) {
                if (!s.getTasks().isEmpty() && s.getWaitingPeriod().get() >= 1)
                    s.getWaitingPeriod().getAndAdd(-1);
            }

            int suma=0;
             for(Server s: scheduler.getServers())//adun waiting time de la fiecare coada/ per fiecare secunda
             {
                 suma+=s.getWaitingPeriod().get();
             }
             listaWaitingTime.add(suma);//aici am de fapt suma waiting times intr o secunda
            //fac rost de cati clienti is in toate listele in secunda aceea
            int nrClienti=0;
            for(int i=0;i<scheduler.getServers().size();i++)
                nrClienti+=scheduler.getServers().get(i).getTasks().size();
             listaPeakTimes.add(nrClienti);
                if(currentTime.get()==timpMaxSimulare)//cand e gata simularea

                { //acum pun waiting time si peak time
                    frame.setAvgWaiting(String.valueOf(getAverageWaitingTime()));
                    frame.setPeakTime(String.valueOf(getPeakTime()));
                }

            for(Server s: scheduler.getServers())
            {  //fac rost de taskul din varful cozii
                Task t=s.getTasks().peek();
                if(t!=null) {
                    if (t.getServiceTime() >= 1) t.setServiceTime(t.getServiceTime() - 1);
                    if (t.getServiceTime() == 0) {
                        s.getTasks().remove(t);

                        int indexulVrut = scheduler.getServers().indexOf(s);
                        System.out.println("Coada de la care vr sa elimin:" + indexulVrut);
                        frame.getCoziDisplay().get(indexulVrut).setText(frame.getCoziDisplay().get(indexulVrut).getText()
                                .replaceFirst("OM", ""));
                    }
                }
            }


        }


    }
    public static void main(String[] args)
    {
        SimulationFrame frame=new SimulationFrame();
    }
}
