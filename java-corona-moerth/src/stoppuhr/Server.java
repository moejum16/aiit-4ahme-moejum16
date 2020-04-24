/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Julian
 */
public class Server {
    
    private ServerSocket serverSocket;
    private final List<ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;
    
    public Server(){
        
    }

    public void start(int port) throws IOException{
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        while (true){
            Socket clientSocket = serverSocket.accept();
            ConnectionHandler h = new ConnectionHandler(clientSocket);
            handlers.add(h);
        }
    }
    
    public boolean isTimerRunning(){
        return startMillis > 0;
    }
    
    public long getTimerMillis(){
        return timeOffset;
    }
    //------------------------------------------------------------------
    
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
    //--------------------------------------------------------------------
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

}
