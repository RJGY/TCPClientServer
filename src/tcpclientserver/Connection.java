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

public class Connection extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private PublicKey pubKey;
    private PrivateKey privKey;
    private DatabaseConnection db;
    private String url = "jdbc:derby://localhost:1527/Assignment2DB";

    public Connection(PublicKey pubKey, PrivateKey prKey, Socket aClientSocket) {
        try {
            this.pubKey = pubKey;
            this.privKey = prKey;
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            db = new DatabaseConnection(url);
            
            this.start();
            
        } catch(IOException e) {
            System.out.println("Connection Start Failed: " + e.getMessage());
        }
    }

    public void run(){
        try {
            // Write out the public key to client.
            out.writeObject(pubKey);
            
            //keep computing the objects sent from the client
            while(true) {
                
                byte [] encodedmessage = (byte[])in.readObject();
                String message = Cryptography.decrypt(privKey, encodedmessage);
                String[] splitMessage = message.split(":");
                String userID = splitMessage[0];
                String password = splitMessage[1];
                if (db.checkUser(userID)) {
                    if (db.checkLogin(userID, password)) {
                        // Successfully logged in.
                        out.writeObject(0);
                    } else {
                        // Incorrect Password
                        out.writeObject(1);
                        continue;
                    }
                } else {
                    // Incorrect Username
                    out.writeObject(2);
                    continue;
                }
                
                while(true) {
                    // Read Object
                    Task t = (Task)in.readObject();

                    // Compute tasks/maths
                    t.executeTask();

                    // write the object back to the stream.
                    out.writeObject(t);

                    if(t instanceof Fibonacci){
                        System.out.println("Fibonnaci task received and computed.");
                    } else if (t instanceof Factorial) {
                        System.out.println("Factorial task received and computed.");
                    } else {
                        System.out.println("GCD task received and computed.");
                    }
                }
            }

        } catch (EOFException e) { 
            System.out.println("EOF: " + e.getMessage());
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
