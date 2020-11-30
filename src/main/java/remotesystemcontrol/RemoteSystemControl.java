package remotesystemcontrol;

public class RemoteSystemControl {
    private static String os = System.getProperty("os.name").toLowerCase();

    private WindowRemoteControll wrc;
    private UnixRemoteControll  urc;
    public RemoteSystemControl(){
        if(isWindows())
            wrc=new WindowRemoteControll();
        else{
            urc=new UnixRemoteControll();
        }
    }
    public void shutDown(){
        if(isWindows()){
            wrc.shutDown();
        }else{
            urc.shutDown();
        }
    }
    public void reboot(){
        if(isWindows()){
            wrc.reboot();
        }else{
            urc.reboot();
        }
    }
    public void killProcessNameProcess(String nameprocess){
        if (isWindows()){
            wrc.killProcessNameProcess(nameprocess);
        }
    }
    public void killProcessPID(String pid){
        if (isWindows()){
            wrc.killProcessPID(pid);
        }
        else{
            urc.killProcessPID(pid);
        }
    }
    public static boolean isWindows() {

        return (os.indexOf("win") >= 0);

    }
}
