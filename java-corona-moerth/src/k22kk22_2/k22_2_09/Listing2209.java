/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_09;

/**
 *
 * @author Julian
 */
public class Listing2209 extends Thread {
    static int cnt = 0;
    
    public static void main(String[] args) {
        Thread t1 = new Listing2209();
        Thread t2 = new Listing2209();
        t1.start();
        t2.start();
    }
    
    public void run(){
        while(true){
            System.out.println(cnt++);
        }
    }
}
