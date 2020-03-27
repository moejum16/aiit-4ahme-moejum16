/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22k22_2.k22_2_2;

/**
 *
 * @author Julian
 */
public class MyTread2202 extends Thread {
    public void run(){
        int i = 0;
        while (true){
            System.out.println(i++);
        }
    }
}


public class Listening2202 {
    public static void main(String[] args) {
        MyTread2202 t = new MyTread2202();
        t.start();
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            //nichts
        }
        t.stop();
    }
}
