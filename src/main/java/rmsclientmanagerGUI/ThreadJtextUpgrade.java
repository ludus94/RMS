package rmsclientmanagerGUI;

import rmsclientmanager.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class ThreadJtextUpgrade implements Runnable{
    private Map<String,StringObject> outputjtext;
    private JTextArea jTextArea;
    private String deviceSelected;
    private BufferedReader bufferedReader;

    public ThreadJtextUpgrade(Map<String, StringObject> outputjtext, JTextArea jTextArea, String deviceSelected) {
        this.outputjtext = outputjtext;
        this.jTextArea = jTextArea;
        this.deviceSelected = deviceSelected;
    }

    public void setDeviceSelected(String deviceSelected) {
        this.deviceSelected = deviceSelected;
    }

    public void setOutputjtext(Map<String, StringObject> outputjtext) {
        this.outputjtext = outputjtext;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        while(true){
            try {
                String action=bufferedReader.readLine();
                if(action.contains("monitoringvalue")) {
                    jTextArea.append(outputjtext.get(deviceSelected).getOut());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
