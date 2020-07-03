/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import javax.swing.SwingWorker;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import stoppuhr.server.Server;
import stoppuhr.server.Server.Request;
import stoppuhr.server.Server.Response;

/**
 *
 * @author Julian
 */
public class ConnectionWorker extends SwingWorker<Object, Response> {

    private Socket socket;
    private boolean tryToStart;
    private boolean tryToStop;
    private boolean tryToClear;
    private boolean tryToEnd;
    private boolean cancel;
    
    private int sliderValue = 0;

    public ConnectionWorker(int port, String host) throws IOException {
        socket = new Socket(port, host);
    }

    public synchronized void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public void setTryToStart(boolean tryToStart) {
        this.tryToStart = tryToStart;
    }

    public void setTryToStop(boolean tryToStop) {
        this.tryToStop = tryToStop;
    }

    public void setTryToClear(boolean tryToClear) {
        this.tryToClear = tryToClear;
    }

    public void setTryToEnd(boolean tryToEnd) {
        this.tryToEnd = tryToEnd;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    protected String doInBackground() throws Exception {
        /*System.out.println("Do in Background" + Thread.currentThread().getId());
         Thread.sleep(1000);*/

        final Gson gson = new Gson();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        while (true) {
            try {
                Server server = new Server();
                final Server.Request req = server.new Request();
                final String reqString = gson.toJson(req);
                writer.write(reqString + "\n");
                writer.flush();
                
                tryToStart = false;
                tryToStop = false;
                tryToClear = false;
                tryToEnd = false;

                final String respString = reader.readLine();
                final Response resp = gson.fromJson(respString, Response.class);
                publish(resp);
                
                
                synchronized(this){
                    int localSliderState = sliderValue;
                    Thread.sleep(1000- localSliderState);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
