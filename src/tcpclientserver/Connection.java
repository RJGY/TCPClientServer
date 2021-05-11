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
import java.net.*;
import java.io.*;
public class Connection extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;

    public Connection(Socket aClientSocket) {

        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public void run(){
        try {
        //keep computing the objects sent from the client
            while(true) {
                //read object from the stream
                Task t = (Task)in.readObject();

                //compute cost for this book instance
                t.calculateCost();

                //write the object back to the stream.
                out.writeObject(t);
                System.out.println("Sent book with cost computed\n");
            }

        } catch (EOFException e) { 
            System.out.println("EOF: "+e.getMessage());
        } catch (IOException e) {
            System.out.println("readline: "+e.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally { 
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Close Failed: "+e.getMessage());
            }
        }
    }
}
