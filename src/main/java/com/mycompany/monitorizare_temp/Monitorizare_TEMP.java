/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.monitorizare_temp;

import com.fazecast.jSerialComm.SerialPort;
import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.NORTH;
import java.awt.Color;
import static java.awt.Color.gray;
import static java.awt.Color.lightGray;
import static java.awt.Color.red;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Robert
 */
public class Monitorizare_TEMP {

    static SerialPort chosenPort;

    public static void main(String[] args) {

        // Creez frame-ul si extrag componanetele de care am nevoie mai jos
        AppFrame mainFrame = new AppFrame();
        JButton connectButton = mainFrame.getConnectButton();
        JButton refresh = mainFrame.getRefresh();
        XYSeries grafic = mainFrame.getGrafic();
        XYSeries grafic2 = mainFrame.getGrafic2();
        JComboBox<String> portlist = mainFrame.getPortlist();
        JTextField curent = mainFrame.getCurent();
        JTextField interval = mainFrame.getInterval();
        //Configurarea butonului de conectare
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connectButton.getText().equals("Connect")) {
                    chosenPort = SerialPort.getCommPort(portlist.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if (chosenPort.openPort()) {
                        connectButton.setText("Disconnect");
                        portlist.setEnabled(false);
                        refresh.setEnabled(false);
                    }
                    // Creem un thread pentru a citii elementelele de pe seriala
                    Thread thread = Arduino_Util.createCommunicationThread(chosenPort, grafic, grafic2, mainFrame, curent, interval);
                    thread.start();

                } else {
                    //disconect
                    chosenPort.closePort();
                    portlist.setEnabled(true);
                    refresh.setEnabled(true);
                    connectButton.setText("Connect");
                    grafic.clear();
                    grafic2.clear();
                }
            }
        });

        // Afisarea fereastra 
        mainFrame.setVisible(true);
    }
}