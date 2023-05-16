/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitorizare_temp;

import com.fazecast.jSerialComm.SerialPort;
import static com.mycompany.monitorizare_temp.Monitorizare_TEMP.chosenPort;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 *
 * @author Robert
 */
public class AppFrame extends JFrame {

    JButton connectButton;
    JButton refresh;
    JButton BMax;
    JButton cald;
    JButton racire;
    XYSeries grafic;
    XYSeries grafic2;
    JComboBox<String> portlist;
    JTextField Curent;
    JTextField Interval;
    JRadioButton T1, T2, T3, T4;
    ButtonGroup bg;

    public AppFrame() {
        //Crearea si configurara ferestrei
        this.setTitle("Monotorizare Temp");
        this.setSize(1000, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //crearea elementelor lista si buton si plasarea in partea de top
        portlist = new JComboBox();
        connectButton = new JButton("Connect");
        refresh = new JButton("Refrsh");
        JPanel topPanel = new JPanel();
        topPanel.add(portlist);
        topPanel.add(connectButton);
        topPanel.add(refresh);

        this.add(topPanel, BorderLayout.NORTH);

        // Introducere variantelor de porturi in JComboBox
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (int i = 0; i < portNames.length; i++) {
            portlist.addItem(portNames[i].getSystemPortName());
        }

        //creare grafic
        grafic = new XYSeries("Temperatura");
        grafic2 = new XYSeries("Umiditate");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(grafic);
        dataset.addSeries(grafic2);

        JFreeChart chart = ChartFactory.createXYLineChart("Temperatura", "Time", "Temp&Um", dataset);

        //stilizare grafic
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        this.add(new ChartPanel(chart), BorderLayout.CENTER);

        //afisare parametri 
        // temp max
        JLabel lMax = new JLabel();
        lMax.setText("Max temp");
        JTextField Max = new JTextField();

        Max.setMaximumSize(new Dimension(140, 20));
        Max.setEditable(false);
        Max.setText("  ");
        JPanel rightPanel = new JPanel();
        BMax = new JButton(" MAX ");

        //Temperatura live
        JLabel LCurent = new JLabel();
        LCurent.setText("Temp ");
        Curent = new JTextField();
        Curent.setMaximumSize(new Dimension(140, 20));
        Curent.setEditable(false);
        Curent.setText("  ");
        Curent.setEditable(false);

        //Intervalul 
        JLabel Inter = new JLabel("Interval");
        Interval = new JTextField();
        Interval.setMaximumSize(new Dimension(140, 20));
        Interval.setEditable(false);
        Interval.setText("  ");
        Interval.setEditable(false);

        //Selectare interval butoane radio
        T1 = new JRadioButton("0,5 sec");
        T2 = new JRadioButton("1 sec");
        T3 = new JRadioButton("2 sec");
        T4 = new JRadioButton("3 sec");

        bg = new ButtonGroup();

        bg.add(T1);
        bg.add(T2);
        bg.add(T3);
        bg.add(T4);

        RadioListener myListener = new RadioListener();
        T1.addActionListener(myListener);
        T2.addActionListener(myListener);
        T3.addActionListener(myListener);
        T4.addActionListener(myListener);

        //Butoame comanda 
        JLabel Comenzi = new JLabel("Comenzi pentru temperatura");
        cald = new JButton("Incalzire");
        racire = new JButton("  Racire ");

        cald.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("7");
                output.flush();

            }

        });

        racire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("8");
                output.flush();

            }
        });

        BMax.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                double maxValue = grafic.getMaxY();
                float max = (float) maxValue;
                Max.setText(String.valueOf(max));
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SerialPort[] portNames = SerialPort.getCommPorts();

                for (int i = 0; i < portNames.length; i++) {
                    portlist.addItem(portNames[i].getSystemPortName());
                }
            }

        });

        Box boxR = Box.createVerticalBox();
        boxR.add(lMax);
        boxR.add(Max);
        boxR.add(BMax);
        boxR.add(Box.createVerticalStrut(10));
        boxR.add(LCurent);
        boxR.add(Curent);
        boxR.add(Box.createVerticalStrut(10));
        boxR.add(Inter);
        boxR.add(Box.createVerticalStrut(10));
        boxR.add(Interval);
        boxR.add(Box.createVerticalStrut(10));
        boxR.add(T1);
        boxR.add(T2);
        boxR.add(T3);
        boxR.add(T4);
        boxR.add(Box.createVerticalStrut(20));
        boxR.add(Comenzi);
        boxR.add(cald);
        boxR.add(Box.createVerticalStrut(10));
        boxR.add(racire);
        rightPanel.add(boxR);

        this.add(rightPanel, BorderLayout.EAST);

    }

    class RadioListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (T1.isSelected()) {
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("0.5");
                output.flush();

            }
            if (T2.isSelected()) {
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("1");
                output.flush();

            }
            if (T3.isSelected()) {
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("2");
                output.flush();

            }
            if (T4.isSelected()) {
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

                output.print("3");
                output.flush();

            }
        }
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getBMax() {
        return BMax;
    }

    public JComboBox<String> getPortlist() {
        return portlist;
    }

    public JButton getRefresh() {
        return refresh;
    }

    public XYSeries getGrafic() {
        return grafic;
    }

    public XYSeries getGrafic2() {
        return grafic2;
    }

    public JTextField getCurent() {
        return Curent;
    }

    public JTextField getInterval() {
        return Interval;
    }

}

