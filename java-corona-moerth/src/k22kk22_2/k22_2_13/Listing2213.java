/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_13;

import java.util.Vector;


class Producer2213 extends Thread{
    private Vector v;

    public Producer2213(Vector v) {
        this.v = v;
    }
    
    public void run() {
        String s;
        
        while(true){
            synchronized(v){
                s = "Wert"+ Math.random();
                v.addElement(s);
                System.out.println("Produzent erzeugte "+ s);
                v.notify();
            }
            try{
                Thread.sleep((int)(100*Math.random()));
            } catch(InterruptedException ex){
                
            }
        }
    }
}

class COnsumer2213 extends Thread{
    private Vector v;

    public COnsumer2213(Vector v) {
        this.v = v;
    }
    
    public void run(){
        while(true){
            synchronized(v){
                if(v.size()<1){
                    try{
                        v.wait();
                    }catch(InterruptedException ex){
                        
                    }
                }
                System.out.print(" Knosument fand "+ (String)v.elementAt(0));
                v.removeElementAt(0);
                System.out.println(" (verbleibent: "+ v.size()+")");
            }
            try{
                Thread.sleep((int)(100*Math.random()));
            } catch(InterruptedException ex){
                
            }
        }
    }
}

public class Listing2213 {
    public static void main(String[] args) {
        Vector v = new Vector();
        
        Producer2213 p = new Producer2213(v);
        COnsumer2213 c = new COnsumer2213(v);
        p.start();
        c.start();
    }
}
