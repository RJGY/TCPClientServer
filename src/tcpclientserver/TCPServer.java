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
public class TCPServer {
    private static final int SERVER_PORT = 8888;
    
    public static void main (String args[]) {
        // Server creates clients in database by running script.
        
        // Server starts
        try {
            ServerSocket listenSocket = new ServerSocket(SERVER_PORT);
            System.out.println("TCP Server running...");
            
            // Generate key pair
            KeyPair keyPair = Cryptography.buildKeyPair();
            PublicKey pubKey = keyPair.getPublic();
            PrivateKey prKey = keyPair.getPrivate();
            while(true) {
                Socket clientSocket = listenSocket.accept();
                // creates a new thread to deal with each client(Thread-per-connection)
                Connection c = new Connection(pubKey, prKey, clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Socket Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException Error: " + ex.getMessage());
        }
    }
}
