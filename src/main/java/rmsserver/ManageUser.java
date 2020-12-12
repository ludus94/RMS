package rmsserver;

import jdk.jfr.events.SocketReadEvent;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ManageUser {
    private Map<String, Socket> clientUserMachines;
    private ArrayList<Socket> clientManagers;

    /***
     * Construction for create a object ManagerUser.
     *
     */
    public ManageUser() {
        if(this.clientManagers==null && this.clientManagers==null) {
            this.clientUserMachines = new TreeMap<>();
            this.clientManagers = new ArrayList<>();
        }
    }

    /***
     * Add a port user client
     * @param name
     * @param socket
     */
    public void addClientUser(String name,Socket socket){
        clientUserMachines.put(name,socket);
    }

    /***
     * Remove a client on the structure
     * @param name
     */
    public void removeClientUser(String name){
        clientUserMachines.remove(name);
    }

    /***
     * Add a port of client manager when require a connection
     * @param socket
     */
    public void addClientManager(Socket socket){
        clientManagers.add(socket);
    }

    /***
     * Remove a port of Manager when require a disconnection
     * @param socket
     */
    public void removeClientManager(Socket socket){
        clientManagers.remove(socket);
    }

    /***
     * Return a port of single matchin, where the name of machine is passed on param
     * @param name
     * @return
     */
    public Socket getSocketMachine(String name){
        return clientUserMachines.get(name);
    }

    /***
     * Return an ArrayList that contains a socket of user
     * @return ArrayList\<Socket\>
     */
    public ArrayList<Socket> getSocketMachines(){
        ArrayList<Socket> ports=new ArrayList<>();
        for (Map.Entry<String,Socket> entry: clientUserMachines.entrySet()){
            ports.add(entry.getValue());
        }
        return ports;
    }

    /***
     * Return an ArrayList that contains the names of machine associated an user
     * @return
     */
    public ArrayList<String> getNameMachines(){
        ArrayList<String> ports=new ArrayList<>();
        for (Map.Entry<String,Socket> entry: clientUserMachines.entrySet()){
            ports.add(entry.getKey());
        }
        return ports;
    }

    /***
     * Return an array list that contains the number of port associated at client manager
     * @return
     */
    public ArrayList<Socket> getSocketManagers(){
        return clientManagers;
    }

    /***
     * Return true if a machine is connected.False if is not exist
     * @param name
     * @return
     */
    public boolean isMachineConnect(String name){
        return clientUserMachines.containsKey(name);
    }

    /***
     * Indicates if an number of ports is occupated
     * @param port
     * @return
     */
    public boolean isPortManagerAllocated(Integer port){
        return clientManagers.contains(port);
    }
}
