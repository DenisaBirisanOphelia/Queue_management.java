package model;

import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class Task  extends TimerTask implements  Comparable<Task>{//aici modelez ce se intampla cu un client:id ul sau (pt identificare), timpu de start si finish
    int id;
    int arrivalTime;
    int serviceTime;
    private boolean finished;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public int compareTo(Task o) {
        return this.arrivalTime-o.arrivalTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }

    @Override
    public void run() {
       // System.out.println("aici sunt in clasa task:");
       /* try {
            Thread.currentThread().sleep(this.serviceTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
