package rmsclient;

import facade.MonitoringValue;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

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
        out=out+mv.getPowerSourceInformation()+"\n";
        return out;
    }

    /***
     * Thread collecting the values given by monitoringSendValueDynamic every 60 seconds
     */
    @Override
    public void run() {
        while(true){
            try {
            pw.println("monitoringvalue");
            pw.print(monitoringSendValueDinamic());
            pw.flush();
            Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
