/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr.server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        startMillis = 0;
        System.out.println("Server auf Port " + port + " gestartet!");

        while (true) {
            final Socket socket = serverSocket.accept();
            synchronized(handlers){
                for(ConnectionHandler h: handlers){
                    if(h.isClosed()){
                        handlers.remove(h);
                    }
                }

                if (handlers.size() < 3){
                    final ConnectionHandler handler = new ConnectionHandler(socket);   
                    handlers.add(handler);
                    new Thread(handler).start(); //hintergrund Thread
                } else {
                    System.out.println("Connection refuse (" + socket.toString() + ")");
                    socket.close();
                }
            }
        }
    }

    public boolean isTimerRunning() {
        synchronized (handlers){
            return startMillis > 0;
        }
    }

    public long getTimerMillis() {
        synchronized(handlers){
            if (startMillis == 0) {
                return timeOffset;
            } else {
                return timeOffset + (System.currentTimeMillis() - startMillis);
            }
        }
    }
//------------------------------------------------------------------------

    private class ConnectionHandler implements Runnable {

        private Socket socket;
        private boolean master;

        public ConnectionHandler(Socket socket) {
            if(socket == null){
                throw new NullPointerException();
            }
            this.socket = socket;
        }

        public boolean isClosed() {
            return socket == null || socket.isClosed();
        }

        public boolean isMaster() {
            return master;
        }

        @Override
        public void run() {
            long cnt = 0;

            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                final Gson gson = new Gson();
                
                while (true) {
                    final String line = reader.readLine(); // zeichen werden in das attribut line gespeichert
                    if(line == null){
                        socket.close();
                        return;
                    }

                    gson.toJson(line); // einkommenden Zeilen werden in ein Objekt gespeichert
                    final Request req = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeichen beinhaltet
                    System.out.println(line);
                    System.out.println(req);
                    
                    if(req.master != null){
                        if (req.isMaster()) {
                            ConnectionHandler currentMaster = null;
                            synchronized (handlers){
                                for (ConnectionHandler c : handlers) {
                                    if (c != this && c.isMaster() == true) {
                                        master = false;

                                        break;
                                    }
                                }
                                if(currentMaster == null){
                                master = true;
                                }
                            }
                        } else {
                            master = false;
                        }
                    }

                    boolean end = false;
                    synchronized (handlers){
                        if (master){
                            if (req.start != null && req.start) {
                                if(startMillis <= 0){
                                    startMillis = System.currentTimeMillis();
                                }
                            }
                            if (req.clear != null && req.clear) {
                                timeOffset = 0;
                                if (startMillis > 0) {
                                    startMillis = System.currentTimeMillis();
                                } 
                            }
                            if (req.stop != null && req.stop && startMillis > 0) {
                                timeOffset += System.currentTimeMillis() - startMillis;
                                startMillis = 0;
                            } 

                            if (req.end != null && req.end) {
                                end = true;
                            }
                        }
                    }
                    
                    final Response res = new Response(master, isTimerRunning(), getTimerMillis());
                    res.count = cnt;
                    res.master = master;
                    res.running = isTimerRunning();
                    res.time = getTimerMillis();
                    final String resString = gson.toJson(res);
                    out.write(resString + '\n');
                    out.flush();
                    if(end){
                        serverSocket.close();
                        return;
                    }
                    
                }
            } catch (Exception ex) {
                    new Exception("SocketHandler Thread fails...", ex).printStackTrace(System.err);

            } finally {
                try{
                    if(socket != null && !socket.isClosed()){
                        socket.close();
                    }
                } catch (Exception ex){
                    new Exception("closing socket fails...", ex).printStackTrace(System.err);
                } finally {
                    if(socket != null){
                        System.out.println("Serverhandler Thread beendet ("+ socket + ")");
                    } else {
                        System.out.println("Serverhandler thread beendet");
                    }
                }
                socket = null;
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
        private long count;
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
