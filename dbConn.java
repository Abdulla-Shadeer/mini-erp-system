/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.erpsystem;
import java.sql.*;
/**
 *
 * @author shadeer
 */
public class dbConn {
    private static String url = "jdbc:mysql://localhost:3306/erpdb";
    private static String uName = "root";
    private static String pass = "";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static Connection con;
    
    public static void main(String[] args){
        getConnection();
    }
    
    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, uName, pass);
                
            } catch (SQLException ex) {                
                System.out.println("Failed to create the database connection"); 
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found"); 
        }
        return con;
    }
    
}
 