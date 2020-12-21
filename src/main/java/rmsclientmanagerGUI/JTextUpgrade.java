package rmsclientmanagerGUI;

import rmsclientmanager.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class JTextUpgrade {
    private Map<String,StringObject> outputjtext;
    private JTextArea jTextArea;
    private String deviceSelected;

    public JTextUpgrade(Map<String, StringObject> outputjtext, JTextArea jTextArea, String deviceSelected) {
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

    public void upgrade() {
            if(outputjtext.containsKey(deviceSelected)) {
                jTextArea.append(outputjtext.get(deviceSelected).getOut());
            }

    }
}
