package facade;

import facade.MonitoringValue;

public class Main {
    public static void main(String[] args){
        MonitoringValue monit=new MonitoringValue();
        System.out.println(monit.getSystemOP());
        System.out.println("Booted: "+monit.getBootedSystem());
        System.out.println(monit.getProcessActive(20));
        System.out.println("CPU Load: "+monit.cpuTotalLoad());
        System.out.println("CPU Load Avarege: "+monit.cpuAverageLoad());
        System.out.println("CPU Load per Core: "+monit.cpuLoadPerCore());
        System.out.println("CPU Temperature: "+monit.getCpuTemperature());
        int [] speed= monit.getFanSpeed();
        if(speed.length>0)
            System.out.println("SpeedFan: ["+speed[0]+" "+speed[1]+"]");

        System.out.println("Cpu Voltage: "+monit.getCpuVoltage());
        System.out.println(monit.getPowerSourceInfornation());
    }
}
