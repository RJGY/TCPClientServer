/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclientserver;

import java.security.*;
import javax.crypto.*;

/**
 *
 * @author Alerz
 */
// Cryptography helper class to encrypt, decrypt and build keypairs.
public class Cryptography {
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keySize);
        return keyPairGen.genKeyPair();
    }
    
    // Decrypt bytecode message to string
    public static String decrypt(PrivateKey privateKey, byte[] encodedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey, cipher.getParameters());
        return new String(cipher.doFinal(encodedMessage));
    } 
    
    // Encrypt string to bytecode
    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        
        byte [] cipherData = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherData;  
    }
}
