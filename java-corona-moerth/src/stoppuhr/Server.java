/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.omg.CORBA.Request;

public class Server {

    private ServerSocket serverSocket;
    private final List<ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;

    public Server() {

    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        startMillis = 0;

        while (true) {
            final Socket clientSocket = serverSocket.accept();
            for(ConnectionHandler h: handlers){
                if(h.isClosed()){
                    handlers.remove(h);
                }
            }
            
            if (handlers.size() < 3){
                final ConnectionHandler handler = new ConnectionHandler(clientSocket);   
                new Thread(handler).start(); //hintergrund Thread
                handlers.add(handler);
            } else {
                clientSocket.close();
            }
        }
    }

    public boolean isTimerRunning() {
        return startMillis > 0;
    }

    public long getTimerMillis() {
        if (startMillis == 0) {
            return timeOffset;
        } else {
            return timeOffset + (System.currentTimeMillis() - startMillis);
        }
    }
//------------------------------------------------------------------------

    private class ConnectionHandler implements Runnable {

        private final Socket socket;
        private boolean master;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        public boolean isClosed() {
            return socket.isClosed();
        }

        public boolean isMaster() {
            return master;
        }

        @Override
        public void run() {
            int cnt = 0;

            try {
                while (true) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                    final String line = reader.readLine(); // zeichen werden in das attribut line gespeichert
                    cnt++;

                    final Gson gson = new Gson();
                    gson.toJson(line); // einkommenden Zeilen werden in ein Objekt gespeichert
                    final Request req = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeichen beinhaltet
                    System.out.println(line);
                    System.out.println(req);
                    
                    if (req.isMaster()) {
                        master = true;
                        for (ConnectionHandler c : handlers) {
                            if (c != this && c.isMaster() == true) {
                                master = false;

                                break;
                            }
                        }
                    }

                    if (master){
                        if (req.isStart()) {
                            startMillis = System.currentTimeMillis();
                        }
                        if (req.isClear()) {
                            timeOffset = 0;
                            if (isTimerRunning()) {
                                startMillis = System.currentTimeMillis();
                            } else {
                                startMillis = 0;
                            }
                        }
                        if (req.isStop()) {
                            startMillis = 0;
                        } else {
                            timeOffset = System.currentTimeMillis() - startMillis + timeOffset;
                        }
                        if (req.isEnd()) {
                            serverSocket.close();
                            socket.close();
                            synchronized (socket) {}
                            handlers.remove(this);
                            return;
                        }
                    }
                    
                    final Response res = new Response(master, isTimerRunning(), getTimerMillis());
                    System.out.println(res);
                    final String resString = gson.toJson(res);
                    System.out.println(resString);
                    writer.write(resString);
                    writer.flush();
                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
//-----------------------------------------------------------------------------

    public class Request {

        public Boolean master;
        public Boolean start;
        public Boolean stop;
        public Boolean clear;
        public Boolean end;
        
        public boolean isMaster(){
            return master != null && master;
        }
        
        public boolean isStart(){
            return start != null && start;
        }
        
        public boolean isStop(){
            return stop != null && stop;
        }
        
        public boolean isClear(){
            return clear != null && clear;
        }
        
        public boolean isEnd(){
            return end != null && end;
        }

        @Override
        public String toString() {
            return "Request{" + "master=" + master + ", start=" + start + ", stop=" + stop + ", clear=" + clear + ", end=" + end + '}';
        }
    }

//-------------------------------------------------------------------------
    public class Response {

        private boolean master;
        private boolean count;
        private boolean running;
        private long time;

        private Response(boolean master, boolean timerRunning, long timerMillis) {
            this.master = master;
            this.count = count;
            this.running = running;
            this.time = time;
        }

        @Override
        public String toString() {
            return "Response{" + "master=" + master + ", count=" + count + ", running=" + running + ", time=" + time + '}';
        }
    }

    //-----------------------------------------------------------------------
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

}
