package facade;

import java.time.Instant;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;

/***
 * This class contains the facade's OSHI library defined on the maven's dependencies.
 *
 */
public class MonitoringValue {
    private SystemInfo si;
    private HardwareAbstractionLayer hal;
    private OperatingSystem os;

    /***
     * The constructor istance the object used to read information's machine
     */
    public MonitoringValue() {
            si=new SystemInfo();
            hal=si.getHardware();
            os=si.getOperatingSystem();
    }

    /***
     * Get method for Operative System properties
     * @return OS name and version as String
     */
    public String getSystemOP(){
        return String.valueOf(os);
    }

    /***
     * Get method for BootedSystem
     * @return Instance of Instant (Datetime) time since system boot
     */
    public Instant getBootedSystem(){
        return Instant.ofEpochSecond(os.getSystemBootTime());
    }

    /***
     * Get method for CPUTotalLoad
     * @return Percentage of CPU total usage (as String)
     */
    public String cpuTotalLoad(){
        long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
        return String.format("%.1f", hal.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100).replace(",", ".");
    }

    /***
     * Get method for CPUAverageLoad for each core
     * @return Percentage of CPU's core average load (as String)
     */
    public String cpuAverageLoad(){
        double[] loadAverage = hal.getProcessor().getSystemLoadAverage(3);
         return (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
            + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
            + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2]));
    }

    /***
     * Get method for CPULoad for each core
     * @return Percentage of CPU's core load (as String)
     */
    public String cpuLoadPerCore(){
        StringBuilder procCpu = new StringBuilder();
        double[] load = hal.getProcessor().getProcessorCpuLoadBetweenTicks(hal.getProcessor().getProcessorCpuLoadTicks());
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        return procCpu.toString();
    }

    /***
     * Returns a description of the most CPU demanding process (up to limit processes)
     * @param limit Threshold of processes shown
     * @return A description of Processes and the resources consumed (as String)
     */
    public String getProcessActive(int limit){
        String out;
        OSProcess myProc = os.getProcess(os.getProcessId());
        GlobalMemory memory=hal.getMemory();
        // Sort by highest CPU
        List<OSProcess> procs = os.getProcesses(limit, ProcessSort.CPU);
        out="   PID  %CPU %MEM       VSZ       RSS Name\n";
        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            if(!p.getName().contains("java"))
            out=out+(String.format(" %5d %5.1f %4.1f %9s %9s %s\n", p.getProcessID(),
                100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
        }
        out=out+"stop\n";
        return out;
    }

    /***
     * Get method for CPUTemperature
     * @return CPU temperature
     */
    public Double getCpuTemperature(){
        Sensors sensor=hal.getSensors();
        return sensor.getCpuTemperature();
    }

    /***
     * Get method for FanSpeed
     * @return CPU fan speed (as rpm)
     */
    public int[] getFanSpeed(){
        Sensors sensor=hal.getSensors();
        return sensor.getFanSpeeds();
    }

    /***
     * Get method for CPUVoltage
     * @return CPU Voltage (as Volt)
     */
    public Double getCpuVoltage(){
        Sensors sensor=hal.getSensors();
        return sensor.getCpuVoltage();
    }

    /***
     * Get method for PowerSourceInformation
     * @return Detailed information on Power Source Supplier
     */
    public String getPowerSourceInformation(){
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

    /***
     * Get method for Power
     * @return Power consumption (as mW)
     */
    public double getPower(){
        List<PowerSource> powerSources=hal.getPowerSources();
        double power_mW=0;
        for(PowerSource power: powerSources){
            power_mW=power.getPowerUsageRate();
        }
        return power_mW;
    }
   
}
