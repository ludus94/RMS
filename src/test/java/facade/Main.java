package facade;

import facade.MonitoringValue;

public class Main {
    public static void main(String[] args){
        MonitoringValue monit=new MonitoringValue();
        System.out.println(monit.getSystemOP());
        System.out.println("Booted: "+monit.getBootedSystem());
        System.out.println(monit.getProcessActive(20)); //JtextArea
        System.out.println("CPU Load: "+monit.cpuTotalLoad()); //JGraphic
        System.out.println("CPU Load Avarege: "+monit.cpuAverageLoad()); //JGraphic
        System.out.println("CPU Load per Core: "+monit.cpuLoadPerCore()); //JTextArea
        System.out.println("CPU Temperature: "+monit.getCpuTemperature()); //JGraphic
        int [] speed= monit.getFanSpeed();
        if(speed.length>0)
            System.out.println("SpeedFan: ["+speed[0]+" "+speed[1]+"]"); //JText

        System.out.println("Cpu Voltage: "+monit.getCpuVoltage()); //JGraphic
        System.out.println(monit.getPowerSourceInformation());//JText
        System.out.println(monit.getPower());
    }
}
