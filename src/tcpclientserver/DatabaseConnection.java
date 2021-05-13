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

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private PreparedStatement queryCheckUser;
    private PreparedStatement queryCheckPassword;
    private ResultSet rs;
    private java.sql.Connection connection;
    
    // Constructor.
    public DatabaseConnection(String url) {
        try {
            // Connection does not require username or password, only url.
            connection = DriverManager.getConnection( url );
            queryCheckUser = connection.prepareStatement("SELECT USERID FROM USERS WHERE USERID = ?");
            queryCheckPassword = connection.prepareStatement("SELECT USERID FROM USERS WHERE USERID = ? AND USERPASSWORD = ?");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Checkuser query
    public boolean checkUser(String userID) {
        rs = null;
        boolean result = false; 
        try {
            queryCheckUser.setString(1, userID);
            rs = queryCheckUser.executeQuery();
            // If a match is returned, return true otherwise false.
            if (rs.next() ) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try
            {
               rs.close();
            } // end try
            catch ( SQLException sqlException )
            {
               sqlException.printStackTrace();
               close();
            } 
        } 
        
        return result;
    }
    
    // Check login query.
    public boolean checkLogin(String userID, String password) {
        rs = null;
        boolean result = false; 
        try {
            // Set parameters
            queryCheckPassword.setString(1, userID);
            queryCheckPassword.setString(2, password);
            rs = queryCheckPassword.executeQuery();
            // If a match is returned, return true otherwise false.
            if (rs.next()) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try
            {
               rs.close();
            } // end try
            catch ( SQLException sqlException )
            {
               sqlException.printStackTrace();
               close();
            } // end catch
        } // end finally
        return result;
    }
    
    public void close() {
        try {
           connection.close();
        } 
        catch ( SQLException sqlException ) {
           sqlException.printStackTrace();
        } 
   } 
}
