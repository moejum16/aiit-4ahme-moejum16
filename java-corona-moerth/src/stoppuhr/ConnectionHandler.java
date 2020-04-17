/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr;

import java.net.Socket;

/**
 *
 * @author Julian
 */
public class ConnectionHandler {
    private Socket socket;
    private boolean master;

    public ConnectionHandler(Socket socket, boolean master) {
        this.socket = socket;
        this.master = master;
    }

    public ConnectionHandler(Socket socket){
        
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
    
    public boolean isMaster() {
        return master;
    }
    
    public void run(){
        
    }
}
