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
public class TCPServer {
    public static void main (String args[]) {
        try{
            int serverPort = 8888;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("TCP Server running...");

            while(true) {
                Socket clientSocket = listenSocket.accept();
                // creates a new thread to deal with each client(Thread-per-connection)
                Connection c = new Connection(clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen socket:"+e.getMessage());
        }
    }
}
