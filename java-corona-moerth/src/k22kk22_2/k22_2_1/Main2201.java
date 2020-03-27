package k22k22_2.k22_2_1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Julian
 */
public class MyThread2201 extends Thread {
    
    public void run() {
        int i = 0;
        while(true){
                System.out.println(i++);
        }
    }
}

public class Main2201 {
    public static void main(String[] args) {
        MyThread2201 t = new MyThread2201();
        t.start();
    }
}
