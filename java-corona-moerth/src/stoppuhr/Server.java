/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Julian
 */
public class Server {
    private ServerSocket serverSocket;
    private final List<ConnectionHandler> = new List<>();
    private long timeOffset;
    private long startMillis;
    
    public Server() throws IOException {
        serverSocket = new ServerSocket();
        
        while (true){
            final Socket clientSocket = new ServerSocket().accept();
            
        }
    }

    public void start(int port){
        
    }
    
    public boolean isTimerRunning(){
        
    }
    
    public long getTimerMillis(){
        
    }
    
    public static void main(String[] args) throws IOException {
        new Server();
    }

}
