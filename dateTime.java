/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.erpsystem;
import java.time.LocalDate; 
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author shadeer
 */
public class dateTime {
    
    public static String formattedDate = "";
    public static String formattedTime = "";
    
    public static void main(String[] args) {
    
    LocalDate date = LocalDate.now(); // Create a date object
    LocalTime time = LocalTime.now();
    
    DateTimeFormatter myDateFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");  
    formattedDate = date.format(myDateFormatObj);
    
    DateTimeFormatter myTimeFormatObj = DateTimeFormatter.ofPattern("hh:mm a");  
    formattedTime = time.format(myTimeFormatObj);
     
    
  }
}
