package rmsclientmanagerGUI;

import rmsclientmanager.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

/***
 * Class used to upgrade information on JTextArea
 */
public class JTextUpgrade implements Runnable{
    private Map<String,StringObject> outputjtext;
    private JTextArea jTextArea;
    private JScrollPane jScrollPane;
    private String deviceSelected;

    /***
     * Costructor of Thread Objects
     * @param outputjtext
     * @param jTextArea
     * @param deviceSelected
     * @param jScrollPane
     */
    public JTextUpgrade(Map<String, StringObject> outputjtext, JTextArea jTextArea, String deviceSelected,JScrollPane jScrollPane) {
        this.outputjtext = outputjtext;
        this.jTextArea = jTextArea;
        this.deviceSelected = deviceSelected;
        this.jScrollPane=jScrollPane;
    }

    /***
     * Set Method for DeviceSelceted, it's used to indicate the device information selected in a GUI
     * @param deviceSelected
     */
    public void setDeviceSelected(String deviceSelected) {
        this.deviceSelected = deviceSelected;
    }

    /***
     * Seth Method for text to display in a text area
     * @param outputjtext Map
     */
    public void setOutputjtext(Map<String, StringObject> outputjtext) {
        this.outputjtext = outputjtext;
    }

    /***
     * Thread to upgrade information
     */
    @Override
    public void run() {
        while(true) {
            if (outputjtext.containsKey(deviceSelected)) {
                jTextArea.append(outputjtext.get(deviceSelected).getOut());
                jTextArea.setRows(outputjtext.get(deviceSelected).getCount());
                jTextArea.setAutoscrolls(true);
                jTextArea.revalidate();
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
