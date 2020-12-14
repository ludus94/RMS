package rmsclientmanagerGUI;

import rmsclientmanager.ClientManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author L.RUSSO62
 */
public class ManagerClientGUI extends javax.swing.JFrame {
    private DataSet datasettemperature;
    private DataSet datasetcpuload;
    private DataSet  datasetcupvoltage;
    private DataSet datasetpower;
    private ArrayList<DataSet> devicetemperature;
    private ArrayList<DataSet> devicecpuload;
    private ArrayList<DataSet> devicecpuvoltage;
    private ArrayList<DataSet> devicepower;
    private DefaultListModel model=new DefaultListModel();
    private ChartLine temperature;
    private ChartLine cpuload;
    private ChartLine cpuvoltage;
    private ChartLine power;
    private String PID;
    private String name;
    private String deviceSelected;
    private ArrayList<String> devicelist;
    private static ClientManager clientManager;
    private String user;
    /**
     * Creates new form ManagerClient
     */
    public ManagerClientGUI(ClientManager clientManager){
        initComponents();
        this.clientManager=clientManager;
        devicetemperature=new ArrayList<>();
        devicecpuload=new ArrayList<>();
        devicecpuvoltage=new ArrayList<>();
        devicepower=new ArrayList<>();
        model=new DefaultListModel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/java/rmsclientmanagerGUI/logoapp.jpeg").getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/java/rmsclientmanagerGUI/logoapp.jpeg");    
        this.setIconImage(icon);
        if(clientManager.getImage()==null) {
            jImageUser.setIcon(imageIcon);
            jImageUser.setText("");
        }
        else{
            try {
                jImageUser.setIcon(clientManager.getImageIcon());
                jImageUser.setText("");
            }catch(UnsupportedEncodingException ex){

            } catch (IOException e) {

            }
        }
        jEmailUser.setText(clientManager.getUsername());

        devicelist=clientManager.getDevicesList();
        this.refreshDeviceList(devicelist);
        for(int i=0;i<model.capacity();i++){
            devicetemperature.add(new DataSet("chartline"));
            devicecpuload.add(new DataSet("chartline"));
            devicecpuvoltage.add(new DataSet("chartline"));
            devicepower.add(new DataSet("chartline"));
        }
        
        this.datasettemperature=devicetemperature.get(0);
        this.datasetcpuload=devicecpuload.get(0);
        this.datasetcupvoltage=devicecpuvoltage.get(0);
        this.datasetpower=devicepower.get(0);
        
        this.temperature=new ChartLine(jTemperaturePanel.getWidth(),jTemperaturePanel.getHeight(),"Temperature",this.datasettemperature);
        this.cpuload=new ChartLine(jCPULoadPanel.getWidth(),jCPULoadPanel.getHeight(),"CPULoad",this.datasetcpuload);
        this.cpuvoltage=new ChartLine(jCPUVoltagePanel.getWidth(),jCPUVoltagePanel.getHeight(),"CPUVoltage",this.datasetcupvoltage);
        this.power=new ChartLine(jPowerPanel.getWidth(),jPowerPanel.getHeight(),"Power",this.datasetpower);
        
        jTemperaturePanel.setLayout(new java.awt.BorderLayout());
        jTemperaturePanel.add(temperature.getChartPannel(),BorderLayout.CENTER);
        jTemperaturePanel.validate();
        jPowerPanel.setLayout(new java.awt.BorderLayout());
        jPowerPanel.add(power.getChartPannel(), BorderLayout.CENTER);
        jPowerPanel.validate();
        jCPULoadPanel.setLayout(new java.awt.BorderLayout());
        jCPULoadPanel.add(cpuload.getChartPannel(),BorderLayout.CENTER);
        jCPULoadPanel.validate();
        jCPUVoltagePanel.setLayout(new java.awt.BorderLayout());
        jCPUVoltagePanel.add(cpuvoltage.getChartPannel(),BorderLayout.CENTER);
        jCPUVoltagePanel.validate();
        jDeviceList.setLayout(new java.awt.CardLayout());
        jDeviceList.setSelectedIndex(0);
        deviceSelected=devicelist.get(0);
        jDashBoardPannel.setVisible(true);
    }
    
    public DataSet getDataSetTemperature() {
        return datasettemperature;
    }

    public DataSet getDataSetCpuLoad() {
        return datasetcpuload;
    }

    public DataSet getDatasetCupVoltage() {
        return datasetcupvoltage;
    }

    public DataSet getDataSetPower() {
        return datasetpower;
    }

    public ArrayList<DataSet> getDeviceTemperature() {
        return devicetemperature;
    }

    public ArrayList<DataSet> getDeviceCpuLoad() {
        return devicecpuload;
    }

    public ArrayList<DataSet> getDeviceCpuVoltage() {
        return devicecpuvoltage;
    }

    public ArrayList<DataSet> getDevicePower() {
        return devicepower;
    }

    public String getPID() {
        return PID;
    }

    public String getNameProcess() {
        return name;
    }
    public String getSelectedDevice() {
        return deviceSelected;
    }

    public void setUser(String user) {
        this.user = user;
        jEmailUser.setText(this.user);
    }
    public void refreshDeviceList(ArrayList<String> devicelist){
        DefaultListModel modelnew=new DefaultListModel();
        int i=0;
        for (String device : devicelist) {
            modelnew.add(i,device);
            i++;
        }
        model=modelnew;
        JFrame f=new JFrame();
        if(jDeviceList.getSelectedIndex()>=0){
            JOptionPane.showMessageDialog(f,"Update device");
            int index=jDeviceList.getSelectedIndex();
            model.removeElementAt(jDeviceList.getSelectedIndex());
            jDeviceList.remove(index);
            jDeviceList.setModel(model);
            jDeviceMenu.validate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuPannel = new javax.swing.JPanel();
        jDeviceMenu = new javax.swing.JScrollPane();
        jDeviceList = new javax.swing.JList<>();
        jInformationUserPannel = new javax.swing.JPanel();
        jImageUser = new javax.swing.JLabel();
        jEmailUser = new javax.swing.JLabel();
        jDashBoardPannel = new javax.swing.JPanel();
        jChartBoard = new javax.swing.JPanel();
        jCPULoadPanel = new javax.swing.JPanel();
        jPowerPanel = new javax.swing.JPanel();
        jCPUVoltagePanel = new javax.swing.JPanel();
        jTemperaturePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jShutdownButton = new javax.swing.JButton();
        jRebootButton = new javax.swing.JButton();
        jKillProcessButton = new javax.swing.JButton();
        jKillProcessWithNameButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RMS Manager");

        jMenuPannel.setBackground(new java.awt.Color(51, 51, 51));
        jMenuPannel.setMaximumSize(new java.awt.Dimension(200, 200));
        jMenuPannel.setPreferredSize(new java.awt.Dimension(100, 150));

        jDeviceMenu.setBackground(new java.awt.Color(51, 51, 51));

        jDeviceList.setBackground(new java.awt.Color(51, 51, 51));
        jDeviceList.setForeground(new java.awt.Color(255, 255, 255));
        jDeviceList.setModel(model);
        jDeviceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jDeviceList.setSelectionBackground(new java.awt.Color(255, 0, 0));
        jDeviceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jDeviceListValueChanged(evt);
            }
        });
        jDeviceMenu.setViewportView(jDeviceList);

        jInformationUserPannel.setBackground(new java.awt.Color(51, 51, 51));

        jImageUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jImageUser.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jImageUser.setMaximumSize(new java.awt.Dimension(75, 75));
        jImageUser.setMinimumSize(new java.awt.Dimension(75, 75));
        jImageUser.setPreferredSize(new java.awt.Dimension(75, 75));

        jEmailUser.setBackground(new java.awt.Color(51, 51, 51));
        jEmailUser.setForeground(new java.awt.Color(255, 255, 255));
        jEmailUser.setText("Email");
        jEmailUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jEmailUserMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jEmailUserMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jEmailUserMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jInformationUserPannelLayout = new javax.swing.GroupLayout(jInformationUserPannel);
        jInformationUserPannel.setLayout(jInformationUserPannelLayout);
        jInformationUserPannelLayout.setHorizontalGroup(
            jInformationUserPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInformationUserPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImageUser, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jEmailUser)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jInformationUserPannelLayout.setVerticalGroup(
            jInformationUserPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInformationUserPannelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jInformationUserPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jEmailUser)
                    .addComponent(jImageUser, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jMenuPannelLayout = new javax.swing.GroupLayout(jMenuPannel);
        jMenuPannel.setLayout(jMenuPannelLayout);
        jMenuPannelLayout.setHorizontalGroup(
            jMenuPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDeviceMenu)
            .addGroup(jMenuPannelLayout.createSequentialGroup()
                .addComponent(jInformationUserPannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jMenuPannelLayout.setVerticalGroup(
            jMenuPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMenuPannelLayout.createSequentialGroup()
                .addComponent(jInformationUserPannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDeviceMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jChartBoard.setBackground(new java.awt.Color(51, 51, 51));
        jChartBoard.setPreferredSize(new java.awt.Dimension(600, 450));

        javax.swing.GroupLayout jCPULoadPanelLayout = new javax.swing.GroupLayout(jCPULoadPanel);
        jCPULoadPanel.setLayout(jCPULoadPanelLayout);
        jCPULoadPanelLayout.setHorizontalGroup(
            jCPULoadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jCPULoadPanelLayout.setVerticalGroup(
            jCPULoadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPowerPanelLayout = new javax.swing.GroupLayout(jPowerPanel);
        jPowerPanel.setLayout(jPowerPanelLayout);
        jPowerPanelLayout.setHorizontalGroup(
            jPowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jPowerPanelLayout.setVerticalGroup(
            jPowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jCPUVoltagePanelLayout = new javax.swing.GroupLayout(jCPUVoltagePanel);
        jCPUVoltagePanel.setLayout(jCPUVoltagePanelLayout);
        jCPUVoltagePanelLayout.setHorizontalGroup(
            jCPUVoltagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jCPUVoltagePanelLayout.setVerticalGroup(
            jCPUVoltagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jTemperaturePanelLayout = new javax.swing.GroupLayout(jTemperaturePanel);
        jTemperaturePanel.setLayout(jTemperaturePanelLayout);
        jTemperaturePanelLayout.setHorizontalGroup(
            jTemperaturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jTemperaturePanelLayout.setVerticalGroup(
            jTemperaturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jChartBoardLayout = new javax.swing.GroupLayout(jChartBoard);
        jChartBoard.setLayout(jChartBoardLayout);
        jChartBoardLayout.setHorizontalGroup(
            jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jChartBoardLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTemperaturePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCPUVoltagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCPULoadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPowerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jChartBoardLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCPULoadPanel, jCPUVoltagePanel, jPowerPanel, jTemperaturePanel});

        jChartBoardLayout.setVerticalGroup(
            jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jChartBoardLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTemperaturePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCPULoadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jChartBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCPUVoltagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPowerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jChartBoardLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCPULoadPanel, jCPUVoltagePanel, jTemperaturePanel});

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setPreferredSize(new java.awt.Dimension(150, 92));
        jScrollPane1.setViewportView(jTextArea1);

        jShutdownButton.setText("Shutdown");
        jShutdownButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jShutdownButtonMouseClicked(evt);
            }
        });

        jRebootButton.setText("Reboot");
        jRebootButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRebootButtonMouseClicked(evt);
            }
        });

        jKillProcessButton.setText("Kill Process");
        jKillProcessButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jKillProcessButtonMouseClicked(evt);
            }
        });

        jKillProcessWithNameButton.setText("Kill Process Name");
        jKillProcessWithNameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jKillProcessWithNameButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jDashBoardPannelLayout = new javax.swing.GroupLayout(jDashBoardPannel);
        jDashBoardPannel.setLayout(jDashBoardPannelLayout);
        jDashBoardPannelLayout.setHorizontalGroup(
            jDashBoardPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDashBoardPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDashBoardPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jChartBoard, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE))
                .addGap(18, 38, Short.MAX_VALUE)
                .addGroup(jDashBoardPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jRebootButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jKillProcessButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jKillProcessWithNameButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jShutdownButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        jDashBoardPannelLayout.setVerticalGroup(
            jDashBoardPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDashBoardPannelLayout.createSequentialGroup()
                .addGroup(jDashBoardPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDashBoardPannelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jShutdownButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRebootButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jKillProcessButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jKillProcessWithNameButton))
                    .addGroup(jDashBoardPannelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jChartBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jMenuPannel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDashBoardPannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMenuPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
            .addComponent(jDashBoardPannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jEmailUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jEmailUserMouseClicked
        jEmailUser.setForeground(Color.WHITE);
    }//GEN-LAST:event_jEmailUserMouseClicked

    private void jEmailUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jEmailUserMouseEntered
        jEmailUser.setForeground(Color.red);
    }//GEN-LAST:event_jEmailUserMouseEntered

    private void jEmailUserMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jEmailUserMouseExited
        jEmailUser.setForeground(Color.WHITE);
    }//GEN-LAST:event_jEmailUserMouseExited

    private void jShutdownButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jShutdownButtonMouseClicked
        JFrame f=new JFrame();
        if(jDeviceList.getSelectedIndex()>=0){
            JOptionPane.showMessageDialog(f,"Send Shutdown ");
            refreshDeviceList(clientManager.getDevicesList());
        }
    }//GEN-LAST:event_jShutdownButtonMouseClicked

    private void jDeviceListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jDeviceListValueChanged
        jDashBoardPannel.setVisible(true);
        int index=jDeviceList.getSelectedIndex();
        deviceSelected=devicelist.get(index);
        jTemperaturePanel.removeAll();
        jPowerPanel.removeAll();
        jCPULoadPanel.removeAll();
        jCPUVoltagePanel.removeAll();
        this.datasettemperature=devicetemperature.get(index);
        this.datasetcpuload=devicecpuload.get(index);
        this.datasetcupvoltage=devicecpuvoltage.get(index);
        this.datasetpower=devicepower.get(index);
        
        this.temperature=new ChartLine(jTemperaturePanel.getWidth(),jTemperaturePanel.getHeight(),"Temperature",this.datasettemperature);
        this.cpuload=new ChartLine(jCPULoadPanel.getWidth(),jCPULoadPanel.getHeight(),"CPULoad",this.datasetcpuload);
        this.cpuvoltage=new ChartLine(jCPUVoltagePanel.getWidth(),jCPUVoltagePanel.getHeight(),"CPUVoltage",this.datasetcupvoltage);
        this.power=new ChartLine(jPowerPanel.getWidth(),jPowerPanel.getHeight(),"Power",this.datasetpower);
        
        jTemperaturePanel.setLayout(new java.awt.BorderLayout());
        jTemperaturePanel.add(temperature.getChartPannel(), BorderLayout.CENTER);
        jTemperaturePanel.validate();
        jPowerPanel.setLayout(new java.awt.BorderLayout());
        jPowerPanel.add(power.getChartPannel(), BorderLayout.CENTER);
        jPowerPanel.validate();
        jCPULoadPanel.setLayout(new java.awt.BorderLayout());
        jCPULoadPanel.add(cpuload.getChartPannel(),BorderLayout.CENTER);
        jCPULoadPanel.validate();
        jCPUVoltagePanel.setLayout(new java.awt.BorderLayout());
        jCPUVoltagePanel.add(cpuvoltage.getChartPannel(),BorderLayout.CENTER);
        jCPUVoltagePanel.validate();
        
    }//GEN-LAST:event_jDeviceListValueChanged

    private void jRebootButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRebootButtonMouseClicked
        JFrame f=new JFrame();
        if(jDeviceList.getSelectedIndex()>=0){
            JOptionPane.showMessageDialog(f,"Send Reboot");
            refreshDeviceList(clientManager.getDevicesList());
        }
    }//GEN-LAST:event_jRebootButtonMouseClicked

    private void jKillProcessButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jKillProcessButtonMouseClicked
        JFrame f=new JFrame();
        String PID=JOptionPane.showInputDialog(f,"Enter process identify (PID)");
        if(!PID.contains(""))
            this.PID=PID;
        else{
            JOptionPane.showMessageDialog(f, "Field of PID mustn't be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jKillProcessButtonMouseClicked

    private void jKillProcessWithNameButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jKillProcessWithNameButtonMouseClicked
        JFrame f=new JFrame();
        String name=JOptionPane.showInputDialog(f,"Enter process name");
        if(!name.contains(""))
            this.name=name;
        else{
            JOptionPane.showMessageDialog(f, "Field of Process name mustn't be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jKillProcessWithNameButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerClientGUI(clientManager).setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jCPULoadPanel;
    private javax.swing.JPanel jCPUVoltagePanel;
    private javax.swing.JPanel jChartBoard;
    private javax.swing.JPanel jDashBoardPannel;
    private javax.swing.JList<String> jDeviceList;
    private javax.swing.JScrollPane jDeviceMenu;
    private javax.swing.JLabel jEmailUser;
    private javax.swing.JLabel jImageUser;
    private javax.swing.JPanel jInformationUserPannel;
    private javax.swing.JButton jKillProcessButton;
    private javax.swing.JButton jKillProcessWithNameButton;
    private javax.swing.JPanel jMenuPannel;
    private javax.swing.JPanel jPowerPanel;
    private javax.swing.JButton jRebootButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jShutdownButton;
    private javax.swing.JPanel jTemperaturePanel;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
