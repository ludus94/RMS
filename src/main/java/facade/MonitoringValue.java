package facade;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;

public class MonitoringValue {
    private SystemInfo si;
    private HardwareAbstractionLayer hal;
    private OperatingSystem os;

    public MonitoringValue() {
            si=new SystemInfo();
            hal=si.getHardware();
            os=si.getOperatingSystem();
    }
    public String getSystemOP(){
        return String.valueOf(os);
    }
    public Instant getBootedSystem(){
        return Instant.ofEpochSecond(os.getSystemBootTime());
    }
    public String cpuTotalLoad(){
        long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
        return String.format("%.1f%%", hal.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100);
    }
    public String cpuAverageLoad(){
        double[] loadAverage = hal.getProcessor().getSystemLoadAverage(3);
         return (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
            + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
            + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2]));
    }
    public String cpuLoadPerCore(){
        StringBuilder procCpu = new StringBuilder();
        double[] load = hal.getProcessor().getProcessorCpuLoadBetweenTicks(hal.getProcessor().getProcessorCpuLoadTicks());
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        return procCpu.toString();
    }
    public String getProcessActive(int limit){
        String out;
        OSProcess myProc = os.getProcess(os.getProcessId());
        GlobalMemory memory=hal.getMemory();
        // Sort by highest CPU
        List<OSProcess> procs = os.getProcesses(limit, ProcessSort.CPU);
        out="   PID  %CPU %MEM       VSZ       RSS Name\n";
        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            out=out+(String.format(" %5d %5.1f %4.1f %9s %9s %s\n", p.getProcessID(),
                100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
        }
        return out;
    }
    public Double getCpuTemperature(){
        Sensors sensor=hal.getSensors();
        return sensor.getCpuTemperature();
    }
    public int[] getFanSpeed(){
        Sensors sensor=hal.getSensors();
        return sensor.getFanSpeeds();
    }
    public Double getCpuVoltage(){
        Sensors sensor=hal.getSensors();
        return sensor.getCpuVoltage();
    }
    public String getPowerSourceInfornation(){
        List<PowerSource> powerSources=hal.getPowerSources();
        StringBuilder sb = new StringBuilder("Power Sources: ");
        if (powerSources.isEmpty()) {
            sb.append("Unknown");
        }
        for (PowerSource powerSource : powerSources) {
            sb.append("\n ").append(powerSource.toString());
        }
        return sb.toString();
    }
   
}
