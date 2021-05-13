/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclientserver;

import java.io.Serializable;

/**
 *
 * @author Alerz
 */
// Helper fibonacci class.
// Finds the fibonacci of a given range.
public class Fibonacci implements Task, Serializable {
    private int num;
    private String result;
    
    // Constructor.
    public Fibonacci(int num) {
        this.num = num;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void executeTask() {
        int[] feb = new int[num];
        feb[0] = 0;
        feb[1] = 1;
        StringBuilder sb = new StringBuilder();

        for(int i = 2; i < num; i++) {
           feb[i] = feb[i-1] + feb[i-2];
        } 
        for(int i = 0; i < num; i++) {
           sb.append(feb[i]).append(" + ");
        }
        result = sb.toString();
    }
    
}
