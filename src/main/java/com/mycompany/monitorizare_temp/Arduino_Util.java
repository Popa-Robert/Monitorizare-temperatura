/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitorizare_temp;

import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Robert
 */
public class Arduino_Util {

    public static Thread createCommunicationThread(SerialPort chosenPort, XYSeries grafic,XYSeries grafic2, JFrame window, JTextField curent, JTextField interval) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                float timer = 0.0f;
                Scanner scanner = new Scanner(chosenPort.getInputStream());
                while (scanner.hasNextLine()) {
                    try {
                        String line = scanner.nextLine();
                        String[] info = line.split(",");
                        System.out.println(line);
                        System.out.println(info[1]);
                        float umi = Float.parseFloat(info[0]);
                        float number = Float.parseFloat(info[1]);
                        float time = Float.parseFloat(info[2]);
                        time = time / 1000;
                        System.out.println("timp " + timer);
                        grafic.add(timer, number);
                        grafic2.add(timer, umi);
                        timer = timer + time;
                        curent.setText(String.valueOf(number));
                        interval.setText(String.valueOf(time + "sec"));
                        window.repaint();
                    } catch (Exception e) {
                    }
                }
                scanner.close();
            }
        };
        return thread;
    }

}
