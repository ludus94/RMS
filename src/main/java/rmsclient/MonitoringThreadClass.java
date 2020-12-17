package rmsclient;

import facade.MonitoringValue;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

/***
 * Class dedicated to sending constantly data to the server
 */
public class MonitoringThreadClass implements Runnable {
    private PrintWriter pw;
    private MonitoringValue mv;
    private Socket sock;

    /***
     * Initializes required classes
     * @param sock Reference to the same Socket
     * @throws IOException
     */
    public MonitoringThreadClass(Socket sock) throws IOException {
        this.pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
        this.sock = sock;
        this.mv=new MonitoringValue();
    }
    public String getNameMachine(){
        return System.getenv("COMPUTERNAME");
    }

    /***
     * Collects and writes all the varying values
     * @return Formatted String with all the info
     */
    public String monitoringSendValueDinamic(){
        String out="";
        out=mv.getProcessActive(20);
        out=out+mv.cpuTotalLoad()+"\n";
        out=out+mv.cpuAverageLoad()+"\n";
        out=out+mv.cpuLoadPerCore()+"\n";
        out=out+mv.getCpuTemperature()+"\n";
        int [] speed= mv.getFanSpeed();
        if(speed.length>0)
           out=out+speed[0]+" "+speed[1]+"\n";
        else {
            out = out + "Speed Information not available\n";
        }
        out=out+mv.getCpuVoltage()+"\n";
        out=out+mv.getPower()+"\n";
        return out;
    }

    /***
     * Thread collecting the values given by monitoringSendValueDynamic every 60 seconds
     */
    @Override
    public void run() {
        while(true){
            try {
                LocalDateTime localDateTime=LocalDateTime.now();
                pw.println("monitoringvalue");
                pw.println(getNameMachine());
                pw.println(mv.getProcessActive(20));
                pw.println(mv.cpuTotalLoad());
                pw.println(mv.cpuAverageLoad());
                pw.println(mv.cpuLoadPerCore());
                pw.println(mv.getCpuTemperature());
                int [] speed= mv.getFanSpeed();
                if(speed.length>0)
                    pw.println(speed[0]+" "+speed[1]);
                else {
                    pw.println("Speed Information not available");
                }
                pw.println(mv.getCpuVoltage());
                pw.println(mv.getPower());
                pw.println(localDateTime.getHour()+":"+localDateTime.getMinute());
                pw.flush();
                System.out.println(monitoringSendValueDinamic());
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
