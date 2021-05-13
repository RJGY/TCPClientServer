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
// GCD helper class. 
// Calculates the gcd of 2 numbers.
public class Gcd implements Task, Serializable {
    private int num1;
    private int num2;
    private String result;
    
    // Constructor.
    public Gcd (int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }
    
    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void executeTask() {
        int gcd = 1;
        for (int i = 1; i <= num1 && i <= num2; i++) {
            if (num1 % i == 0 && num2 % i == 0) {
                gcd = i;
            }
        }
        this.result = String.format("GCD of %d & %d numbers is: %d", num1, num2, gcd);
    }
    
}
