package rmsclientmanagerGUI;

import rmsclientmanager.*;

import javax.swing.*;
import java.util.Map;

public class ThreadJtextUpgrade implements Runnable{
    private Map<String,StringObject> outputjtext;
    private JTextArea jTextArea;
    private String deviceSelected;

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

    @Override
    public void run() {
        while(true){
            jTextArea.append(outputjtext.get(deviceSelected).getOut());
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
