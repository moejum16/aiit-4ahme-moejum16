/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stoppuhr.gui;

import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
import java.io.IOException;
import stoppuhr.server.Server.Response;
import stoppuhr.server.Server.Request;
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
         System.out.println("Do in Background" + Thread.currentThread().getId());
         Thread.sleep(1000);
         
         publish(1);
       
         Thread.sleep(1000);
         
         publish(2);
         
         Thread.sleep(1000);
        return "OK";

    }
    
}
