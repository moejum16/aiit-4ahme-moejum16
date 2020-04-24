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
public class ConnectionHandler implements Runnable{
    private Socket socket;
    private boolean master;


    public ConnectionHandler(Socket socket){
        this.socket = socket;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
    
    public boolean isMaster() {
        return master;
    }
    
    public void run(){
        
        
        try{
            while(client.stop == false){
                
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
