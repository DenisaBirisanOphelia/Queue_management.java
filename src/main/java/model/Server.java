package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {//modeleaza o coada si ce se intampla cu ea
    private BlockingQueue<Task> tasks;//coada de task uri
    private AtomicInteger waitingPeriod;
    private int nrtotalClienti;

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public Server(AtomicInteger waitingPeriod)
    {
        this.waitingPeriod = waitingPeriod; //initializez coada si perioada de asteptare
        this.waitingPeriod.getAndSet(0);
        tasks = new LinkedBlockingDeque<>();

    }
    public void addTask(Task t)
    {  //adaug taskul la coada si cresc perioada de asteptare
        tasks.add(t);
        waitingPeriod.getAndAdd(t.serviceTime);
    }
    public void removeTask(Task t)
    {  //adaug taskul la coada si cresc perioada de asteptare
        tasks.remove(t);
    }
    @Override
    public void run() {
        boolean continua=true;
        if(tasks.isEmpty()) continua=false;
        while(continua)
        {

            //ia noul task din coada
            Task t=null;
            try {
                t=tasks.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //opreste threadul cu un tp egal cu timpul de procesare al taskului
            try {
                Thread.currentThread().wait(t.serviceTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //scade perioada de asteptare
           waitingPeriod.getAndDecrement();
        }

    }
    public BlockingQueue<Task> getTasks()
    {
        return tasks;
    }
}
