/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_14;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


class Producer2214 extends Thread{
    private PipedOutputStream pipe;

    public Producer2214(PipedOutputStream pipe) {
        this.pipe = pipe;
    }
    
    public void run(){
        byte b = (byte)(Math.random()*128);
        try{
            pipe.write(b);
            System.out.println("Produzent erzeugte "+b);
        } catch (IOException ex ){
            System.err.println(ex.toString());
        }
        try{
            Thread.sleep((int)(100*Math.random()));
        } catch(InterruptedException e){
            
        }
    }
}


class Consumer2214 extends Thread{
    private PipedInputStream pipe;

    public Consumer2214(PipedInputStream pipe) {
        this.pipe = pipe;
    }
    
    public void run(){
        while(true){
            try{
                byte b = (byte)pipe.read();
                System.out.println(" Konsument fnd "+b);
            } catch (IOException ex){
                System.err.println(ex.toString());
            }
            try{
                Thread.sleep((int)(100*Math.random()));
            } catch(InterruptedException e){
                
            }
        }
    }
}

public class Listing2214 {
    public static void main(String[] args) throws Exception{
        PipedInputStream inPipe = new PipedInputStream();
        PipedOutputStream outPipe = new PipedOutputStream(inPipe);
        Producer2214 p = new Producer2214(outPipe);
        Consumer2214 c = new Consumer2214(inPipe);
        p.start();
        c.start();
    }
}
