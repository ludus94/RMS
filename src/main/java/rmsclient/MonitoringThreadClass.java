package rmsclient;

import facade.MonitoringValue;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MonitoringThreadClass implements Runnable {
    private PrintWriter pw;
    private MonitoringValue mv;
    private Socket sock;

    public MonitoringThreadClass(Socket sock) throws IOException {
        this.pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
        this.sock = sock;
        this.mv=new MonitoringValue();
    }

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
        out=out+mv.getPowerSourceInfornation()+"\n";
        return out;
    }
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
