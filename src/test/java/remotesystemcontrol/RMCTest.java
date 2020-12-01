package remotesystemcontrol;

public class RMCTest {
    public static void main(String[] args){
        RemoteSystemControl rmc=new RemoteSystemControl();
        //rmc.shutDown();
        rmc.reboot();
        //rmc.killProcessNameProcess("brave.exe");
        //rmc.killProcessPID("422");
    }
}
