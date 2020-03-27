/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22kk22_2.k22_2_5;

import java.io.*;

public class Listening2206 {
    public static void main(String[] args) {
        PrimeNumberTools pt = new PrimeNumberTools();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        new DataInputStream(System.in)));
        int num;
        
        try{
            while(true){
                System.out.println("Bitte Zahl eingeben: ");
                System.out.flush();
                num = (new Integer(in.readLine())).intValue();
                if(num == -1){
                    break;
                }
                pt.printPrimeFactors(num);
            }
        } catch (IOException ex){
            
        }
    }
}
