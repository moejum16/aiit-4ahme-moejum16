/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_5;

/**
 *
 * @author Julian
 */
public class ThreadedPrimeNumberTools extends PrimeNumberTools implements Runnable{
    private int arg;
    private int func;
    
    public void printPrimeFactors(int num){
        exeAsynchron(1, num);
    }
    public void printPrime(int cnt){
        exeAsynchron(2, cnt);
    }
    public void run(){
        if(func == 1){
            super.printPrimeFactors(arg);
        } else if(func == 2){
            int result = super.getPrime(arg);
            System.out.println("prime number #" +arg+" is: "+ result);
        }
    }
    
    private void exeAsynchron(int func, int arg){
        Thread t = new Thread(this);
        this.func = func;
        this.arg = arg;
        t.start();
    }
}
