/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_12;

class Counter2212{
    int cnt;

    public Counter2212(int cnt) {
        this.cnt = cnt;
    }
    
    public synchronized int nextNumber(){
        int ret = cnt;
        //Hier erfolgen ein paar zeitaufwendige Berechnungen, um
        //so zu tun, als sei das Errechnen des Nachfolgezählers
        //eine langwierige Operation, die leicht durch den
        //Scheduler unterbrochen werden kann.
        double x = 1.0, y, z;
        for(int i = 0; i<1000; ++i){
            x = Math.sin((x*i%35)*1.33);
            y = Math.log(x+10.0);
            z = Math.sqrt(x+y);
        }
        //Jetzt ist der Wert gefunden
        cnt++;
        return ret;
    }
}


public class Listing2212 extends Thread {
    private String name;
    private Counter2212 counter;

    public Listing2212(String name, Counter2212 counter) {
        this.name = name;
        this.counter = counter;
    }
    
    public static void main(String[] args) {
        Thread[] t = new Thread[5];
        Counter2212 cnt = new Counter2212(10);
        for(int i = 0; i<5;++i){
            t[i]= new Listing2212("Thread-"+i, cnt);
            t[i].start();
        }
    }
    
    public void run(){
        while(true){
            System.out.println(counter.nextNumber()+" for "+name);
        }
    }
}