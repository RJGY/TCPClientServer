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
import java.util.*;

public class TCPClient {
    public static void main (String args[]) {

        Socket s = null;

        try {
            int serverPort = 8888;

            // bind the socket with the server name and port number
            s = new Socket("localhost", serverPort);

            //establish inout and output streams
            ObjectInputStream in = null;
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream( s.getInputStream());

            //Scanner instance to receive user input
            Scanner sa = new Scanner(System.in);

            //client is engaged with the server until he chooses to exit
            while(true)
            {
                System.out.println("1.ComputeBook");
                System.out.println("2. Exit");
                //reads input as integer
                int choice = sa.nextInt();
                if(choice == 2) {
                    break;
                }
                System.out.println("Enter Book title:");
                //reads title as one word
                String title = sa.next();
                System.out.println("Enter number of pages:");
                //reads pages as integer
                int pages = sa.nextInt();

                //Creating an instance of Book type to be written to the stream
                Task t = new Book(title,pages);

                //write object ot the server
                out.writeObject(t);


                //read object sent by the server
                t = (Task)in.readObject();

                System.out.println("The Received Book Details:");
                System.out.println("====================================");
                System.out.println( ((Book)t).viewDetails());
                System.out.println("====================================");
            }

        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" +e .getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                   System.out.println("close:"+e.getMessage());
                }
            }
        }
    }
}

