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
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

// Each connection made from a client and the server makes a connection.
public class Connection extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private PublicKey pubKey;
    private PrivateKey privKey;
    private DatabaseConnection db;
    // Database name is: Assignment2DB
    private String url = "jdbc:derby://localhost:1527/Assignment2DB";

    public Connection(PublicKey pubKey, PrivateKey prKey, Socket aClientSocket) {
        try {
            this.pubKey = pubKey;
            this.privKey = prKey;
            // Connect to client.
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            // Connect to database.
            db = new DatabaseConnection(url);
            
            // Start thread.
            this.start();
            
        } catch(IOException e) {
            System.out.println("Connection Start Failed: " + e.getMessage());
        }
    }

    @Override
    public void run(){
        try {
            // Write out the public key to client.
            out.writeObject(pubKey);
            
            // Keep computing the objects sent from the client.
            while(true) {
                // Read in client encoded username and password.
                byte [] encodedmessage = (byte[])in.readObject();
                String message = Cryptography.decrypt(privKey, encodedmessage);
                String[] splitMessage = message.split(":");
                String userID = splitMessage[0];
                String password = splitMessage[1];
                // Check username first, then username and password.
                if (db.checkUser(userID)) {
                    if (db.checkLogin(userID, password)) {
                        // Successfully logged in.
                        out.writeObject(0);
                    } else {
                        // Incorrect Password, go back to top of loop.
                        out.writeObject(1);
                        continue;
                    }
                } else {
                    // Incorrect Username, go back to top of loop.
                    out.writeObject(2);
                    continue;
                }
                
                while(true) {
                    // Read Object
                    Task t = (Task)in.readObject();

                    // Compute tasks/maths
                    t.executeTask();

                    // Write the object back to the client.
                    out.writeObject(t);
                    
                    // Log to screen what type of task was executed.
                    if(t instanceof Fibonacci){
                        System.out.println("Fibonnaci task received and computed.");
                    } else if (t instanceof Factorial) {
                        System.out.println("Factorial task received and computed.");
                    } else {
                        System.out.println("GCD task received and computed.");
                    }
                }
            }
        // Catch Errors
        } catch (EOFException e) { 
            System.out.println("Closed connection.");
        } catch (IOException e) {
            System.out.println("readline: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Close Failed: " + e.getMessage());
            }
        }
    }
}
