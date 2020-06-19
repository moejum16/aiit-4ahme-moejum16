/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.net.Socket;
import javax.swing.SwingWorker;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import stoppuhr.server.Server;
import stoppuhr.server.Server.Response;
/**
 *
 * @author Julian
 */
public class ConnectionWorker extends SwingWorker<Object, Response>{

    private Socket socket;

    public ConnectionWorker(int port, String hostName) {
        
    }

    @Override
    protected String doInBackground() throws Exception{
         /*System.out.println("Do in Background" + Thread.currentThread().getId());
         Thread.sleep(1000);*/
         
         final Gson gson = new Gson();
         final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
         while(true){
             try{
                 final Server.Request req = new Server.Request();
                 final String reqString = gson.toJson(req);
                 writer.write(reqString);
                 writer.flush();
                 
                 final String respString = reader.readLine();
                 Response resp = gson.fromJson(respString, Response.class);
                 publish(resp);
                 
                 Thread.sleep(1000);
             } catch(Exception ex){
                 ex.printStackTrace();
             }
         }
    }
}
