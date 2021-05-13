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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {
    private static final int PORT_NUMBER = 8888;
    private static final String SERVER_NAME = "localhost";
    private static PublicKey publicKey;
    
    public static void main (String args[]) throws InvalidKeySpecException, Exception {
        Socket s = null;
        byte []  bytesPublicKey = null; 
        String name = null;
        
        try {
            // Bind the socket with the server name and port number
            s = new Socket(SERVER_NAME, PORT_NUMBER);
            
            // Establish object in and output streams
            ObjectInputStream in = null;
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream( s.getInputStream());
            
            // Recieve publicKey as object and cast it to PublicKey
            // Save bytecode of public key to object.
            publicKey = (PublicKey)in.readObject();
            bytesPublicKey = publicKey.getEncoded();
            
            // Scanner instance to receive user input
            Scanner sa = new Scanner(System.in);

            // Client is engaged with the server until he chooses to exit
            OUTER: while(true)
            {   
                System.out.println("Enter Username: ");
                String userName = sa.nextLine();
                if (userName.equals("") || userName == null) {
                    System.out.println("Username must be entered");
                    continue;
                }
                System.out.println("Enter Password: ");
                String password = sa.nextLine();
                if (password.equals("") || password == null) {
                    System.out.println("Password must be entered");
                    continue;
                }
                
                // Combine message into one string with delimiter.
                String message = userName + ":" + password;
                
                // Get specification and encode message.
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytesPublicKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(pubKeySpec);
                byte[] encodedmessage = Cryptography.encrypt(publicKey, message);
                
                // Send the encoded username and password.
                out.writeObject(encodedmessage);
                
                // Receive response as integer.
                switch ((int)in.readObject()) {
                    case 0:
                        System.out.println("**********Success***********");
                        name = userName;
                        while (true) {
                            // Pick selection of task.
                            System.out.println("Please make your selection " + name);
                            System.out.println("*********************");
                            System.out.println("1. Factorial");
                            System.out.println("2. Fibonacci");
                            System.out.println("3. Greatest Common Denominator");
                            System.out.println("4. Exit");
                            System.out.println("*********************");
                            System.out.println("Enter your option: ");
                            
                            // Reads input as integer
                            int choice = sa.nextInt();
                                
                            // Choice in switch for readability.
                            switch (choice) {
                                case 1:
                                    // Factorial
                                    System.out.println("Enter the range limit: ");
                                    Task factorial = new Factorial(sa.nextInt());

                                    //write object ot the server
                                    out.writeObject(factorial);

                                    //read object sent by the server
                                    factorial = (Task)in.readObject();

                                    System.out.println("The Received Factorial Details:");
                                    System.out.println("====================================");
                                    System.out.println( ((Factorial)factorial).getResult());
                                    System.out.println("====================================");
                                    break;
                                case 2: 
                                    // Fibonacci
                                    System.out.println("Enter the range limit: ");
                                    Task fibonacci = new Fibonacci(sa.nextInt());

                                    //write object ot the server
                                    out.writeObject(fibonacci);

                                    //read object sent by the server
                                    fibonacci = (Task)in.readObject();

                                    System.out.println("The Received Fibonacci Details:");
                                    System.out.println("====================================");
                                    System.out.println( ((Fibonacci)fibonacci).getResult());
                                    System.out.println("====================================");
                                    break;
                                case 3:
                                    // GCD
                                    System.out.println("Enter the first number: ");
                                    int num1 = sa.nextInt();
                                    System.out.println("Enter the second number: ");
                                    int num2 = sa.nextInt();
                                    Task gcd = new Gcd(num1,num2);

                                    // Write object to Server to execute task for us.
                                    out.writeObject(gcd);

                                    // Read object back in.
                                    gcd = (Task)in.readObject();

                                    System.out.println("The Received Fibonacci Details:");
                                    System.out.println("====================================");
                                    System.out.println( ((Gcd)gcd).getResult());
                                    System.out.println("====================================");
                                    break;
                                case 4:
                                    System.out.println("Exiting System.");
                                    // Breaks out of whole loop.
                                    break OUTER;
                                default:
                                    System.out.println("Incorrect input. Try again.");
                                    break;
                            }
                        }
                    case 1:
                        System.out.println("**********Incorrect Password***********");
                        break;
                    case 2: 
                        System.out.println("**********Incorrect Username***********");
                        break;
                    default:
                        break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
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

