/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclientserver;

/**
 *
 * @author Alerz
 */
import java.io.Serializable;

//Book class implements Task interface and Serializable
public class Book implements Task, Serializable {
    private String title;
    private int nbrPages;
    //This property will be computed on the server.
    private double cost;


    public Book() {

    }
    
    public Book(String title,int nbrPages){
        this.title = title;
        this.nbrPages = nbrPages;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //implement the method from the interface
    public String viewDetails() {
        return  String.format("Title:%s \nNumber of pages:%d \nTotal cost:$%.2f",title,nbrPages,cost);
    }
    
    //implement the method from the interface
    public void calculateCost() {
        final double COST_PER_PAGE = 0.15;

        cost = nbrPages*COST_PER_PAGE;
    }
}
