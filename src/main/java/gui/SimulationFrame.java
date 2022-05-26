package gui;

import businessLogic.ShortestQueueStrategy;
import businessLogic.SimulationManager;
import businessLogic.Strategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.awt.Font.MONOSPACED;

public class SimulationFrame extends JFrame {
    //am un model de app de lucru cu threaduri aici

    private Button start=new Button("LET'S GO");
    private Button clear=new Button("Clear");
    private JTextField nrClients=new JTextField(3);
    private JTextField nrServers=new JTextField(3);
    private JTextField minArrival=new JTextField(3);
    private JTextField maxArrival=new JTextField(15);
    private JTextField minService=new JTextField(15);
    private JTextField maxService=new JTextField(15);
    private JTextField timpSimulare=new JTextField(4);
    private JTextField currentTime=new JTextField(10);
    private JTextField avgWaiting=new JTextField(4);
    private  JTextField avgService=new JTextField(4);
    private  JTextField peakTime=new JTextField(4);
    private JComboBox alegeStrategia;
    private SimulationFrame framic;
    ArrayList<JTextField> coziDisplay=new ArrayList<JTextField>();

    public SimulationFrame()
    {

    framic=this;
        this.setSize(1000,500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//cu macro-ul asta inchid doar fereastra curenta,
        // nu toata app cd dau pe x
        this.setTitle("Interfata aplicatiei cu threaduri");

        JPanel content=new JPanel(new FlowLayout());
        content.add(new JLabel("Ready to start?"));
        //creez JPanelul cu buton
        JPanel content1=new JPanel(new FlowLayout());
        JLabel label11=new JLabel("Timpul curent:");
        label11.setHorizontalAlignment(JLabel.CENTER);
        content1.add(label11);
        content1.add(currentTime);
        currentTime.setEditable(false);
        start.setPreferredSize(new Dimension(100,60));
        start.setBackground(Color.cyan);
        start.setFont(Font.getFont(MONOSPACED));
        content1.add(start);
        clear.setPreferredSize(new Dimension(100,60));
        clear.setBackground(Color.cyan);
        clear.setFont(Font.getFont(MONOSPACED));
        content1.add(clear);
        JLabel label70=new JLabel("Avg waiting time:");
        label70.setHorizontalAlignment(JLabel.CENTER);
        content1.add(label70);
        avgWaiting.setEditable(false);
        content1.add(avgWaiting);
        JLabel label71=new JLabel("Avg service time:");
        label71.setHorizontalAlignment(JLabel.CENTER);
        content1.add(label71);
        avgService.setEditable(false);
        content1.add(avgService);
        JLabel label72=new JLabel("Peak time:");
        label72.setHorizontalAlignment(JLabel.CENTER);
        content1.add(label72);
        peakTime.setEditable(false);
        content1.add(peakTime);



        // pun textFields acum
        JPanel content4=new JPanel(new GridLayout(0,2));
        JLabel label1=new JLabel("Numar clienti:");
        label1.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label1);
        content4.add(nrClients);
        JLabel label2=new JLabel("Numar de cozi:");
        label2.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label2);
        content4.add(nrServers);
        JLabel label3=new JLabel("Min arrival time:");
        label3.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label3);
        content4.add(minArrival);
        JLabel label4=new JLabel("Max arrival time:");
        label4.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label4);
        content4.add(maxArrival);
        JLabel label5=new JLabel("Min service time:");
        label5.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label5);
        content4.add(minService);
        JLabel label6=new JLabel("Max service time:");
        label6.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label6);
        content4.add(maxService);
        JLabel label7=new JLabel("Timp simulare:");
        label7.setHorizontalAlignment(JLabel.CENTER);
        content4.add(label7);
        content4.add(timpSimulare);
        content4.add(new JLabel("                      "));
        String[] posibilitati=new String[3];
        posibilitati[0]="SHORTEST_QUEUE";
        posibilitati[1]="SHORTEST_TIME";

        alegeStrategia=new JComboBox<>(posibilitati);
        content4.add(alegeStrategia);


        //le pun laolalta
        JPanel content11=new JPanel(new GridLayout(0,1));
        JPanel content2=new JPanel(new GridLayout(0,1));
        content2.add(content);
        content2.add(content1);
        content2.add(content4);
        JPanel content10=new JPanel( new GridLayout(0,2));
        content10.add(content2);

        //setez contentu ferestrei la panelul creat si restu setarilor clasice
        this.add(content10);

        this.setVisible(true);

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //aici creez frumos textFields pt cozi

                content11.removeAll();;
                for(int i=0;i<getNrCozi();i++)
                {
                       JTextField t=new JTextField(4);
                       t.setEditable(false);
                    coziDisplay.add(t);
                    content11.add(new JLabel("Queue #"+i+" "));
                    content11.add(coziDisplay.get(i));
                   content11.revalidate();
                   content11.repaint();

                }

                content10.add(new JScrollPane(content11));
                content10.revalidate();
                content10.repaint();
                String strat= String.valueOf(alegeStrategia.getItemAt(alegeStrategia.getSelectedIndex()));

                SimulationManager simp=new SimulationManager(getNrClients(),getNrCozi(),getTimpSimulare(),
                        getMinArrival(),getMaxArrival(),
                        getMinService(),getMaxArrival(), framic,strat);

                Thread t=new Thread(simp);
                t.start();
            }
        });
    }
  public void setNrClients(String s)
  {
      nrClients.setText(s);
  }
  public int getNrClients()
  {
      return Integer.parseInt(nrClients.getText());
  }

  public void setNrCozi(String s)
  {
      nrServers.setText(s);
  }
  public int getNrCozi()
  {
      return Integer.parseInt(nrServers.getText());
  }
  public void setminArrival(String s)
  {
      minArrival.setText(s);
  }
  public int getMinArrival()
  {
      return Integer.parseInt(minArrival.getText());
  }
  public void setMaxArrival(String s)
  {
      maxArrival.setText(s);
  }
  public int getMaxArrival()
  {
      return Integer.parseInt(maxArrival.getText());
  }
  public void setMinServiceS(String s)
  {
      minService.setText(s);
  }
  public int getMinService()
  {
      return Integer.parseInt(minService.getText());
  }
  public int getMaxService()
  {
      return Integer.parseInt(maxService.getText());
  }
  public void setMaxService(String s)
  {
      maxService.setText(s);
  }
  public void settpSimulare(String s)
  {
      timpSimulare.setText(s);
  }
  public int getTimpSimulare()
  {
      return Integer.parseInt(timpSimulare.getText());
  }
  public void setCurrentTime(String s)
  {
      currentTime.setText(s);
  }
  public void reset()
  {
      setNrCozi("");
      setNrClients("");
      setminArrival("");
      setMaxArrival("");
      setMinServiceS("");
      setMaxService("");
      settpSimulare("");
  }

    public ArrayList<JTextField> getCoziDisplay() {
        return coziDisplay;
    }

    public void setCoziDisplay(ArrayList<JTextField> coziDisplay) {
        this.coziDisplay = coziDisplay;
    }

    public void setAvgWaiting(String s)
    {
       avgWaiting.setText(s);
    }
    public void setAvgService(String s)
    {
        avgService.setText(s);
    }
    public void setPeakTime(String s)
    {
        peakTime.setText(s);
    }

}
