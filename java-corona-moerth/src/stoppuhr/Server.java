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
        while (true) {
            final Socket clientSocket = serverSocket.accept();
            /*if(handlers.size() < 3){
                final ConnectionHandler handler = new ConnectionHandler(clientSocket);
                new Thread(handlers).start();
            }*/
            //überprüfung von clients welche schon geschlossen sind
            
                        
            if (handlers.size() > 3) {
                ConnectionHandler h = new ConnectionHandler(clientSocket);
                new Thread(h).start();
                handlers.add(h);
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
            try {
                while (true) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String line = reader.readLine(); // zeichen werden in das attribut line gespeichert

                    final Gson gson = new Gson();
                    gson.toJson(line); // einkommenden Zeilen werden in ein Objekt gespeichert
                    final Request r = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeichen beinhaltet

                    if (r.isMaster()) {
                        for (ConnectionHandler c : handlers) {
                            master = master;
                            if (c != this && c.isMaster() == true) {
                                master = false;
                                
                                
                                break;
                            }
                        }
                    }
                    if (master == true) {
                        if (r.isStart()) {
                            startMillis = System.currentTimeMillis();
                        }
                        if (r.isClear()) {
                            if (isTimerRunning()) {
                                startMillis = System.currentTimeMillis();
                            }
                            timeOffset = 0;
                        }
                        if (r.isStop()) {
                            timeOffset = getTimerMillis();
                            startMillis = 0;
                        }
                        if (r.isEnd()) {
                            //serverapplication schließen
                            handlers.remove(this);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try{
                    socket.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
//-----------------------------------------------------------------------------

    public class Request {

        private boolean master;
        private boolean start;
        private boolean stop;
        private boolean clear;
        private boolean end;

        public boolean isMaster() {
            return master;
        }

        public boolean isStart() {
            return start;
        }

        public boolean isStop() {
            return stop;
        }

        public boolean isClear() {
            return clear;
        }

        public boolean isEnd() {
            return end;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public void setStart(boolean start) {
            this.start = start;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public void setClear(boolean clear) {
            this.clear = clear;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }

//-------------------------------------------------------------------------
    public class Response {

        private boolean master;
        private boolean count;
        private boolean running;
        private long time;

        public Response(boolean master, boolean count, boolean running, long time) {
            this.master = master;
            this.count = count;
            this.running = running;
            this.time = time;
        }
        
        public boolean isMaster() {
            return master;
        }

        public boolean isCount() {
            return count;
        }

        public boolean isRunning() {
            return running;
        }

        public long getTime() {
            return time;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public void setCount(boolean count) {
            this.count = count;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

}
