/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclientserver;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private PreparedStatement queryCheckUser;
    private PreparedStatement queryCheckPassword;
    private ResultSet rs;
    private java.sql.Connection connection;
    
    private static final String USERNAME = "APP";
    private static final String PASSWORD = "APP"; 
    
    public DatabaseConnection(String url) {
        try {
            connection =
            DriverManager.getConnection( url );
            queryCheckUser = connection.prepareStatement("SELECT USERID FROM USERS WHERE USERID = ?");
            queryCheckPassword = connection.prepareStatement("SELECT USERID FROM USERS WHERE USERID = ? AND USERPASSWORD = ?");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkUser(String userID) {
        rs = null;
        boolean result = false; 
        try {
            queryCheckUser.setString(1, userID);
            rs = queryCheckUser.executeQuery();
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
            } // end catch
        } // end finally
        
        return result;
    }
    
    public boolean checkLogin(String userID, String password) {
        rs = null;
        boolean result = false; 
        try {
            queryCheckPassword.setString(1, userID);
            queryCheckPassword.setString(2, password);
            rs = queryCheckPassword.executeQuery();
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
