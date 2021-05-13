/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclientserver;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Alerz
 */
// Helper Factorial class.
// Finds the factorial number of a number.
public class Factorial implements Task, Serializable{
    private int num;
    private String result;
    
    // Constructor.
    public Factorial(int num) {
        this.num = num;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void executeTask() {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
            
        this.result = String.format("Factorial of %d = %s", num, result);
    }
    
}
