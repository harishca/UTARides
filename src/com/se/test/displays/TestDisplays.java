package com.se.test.displays;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDisplays {
	static Connection connection;
	static PreparedStatement p;
	
	public static boolean DBConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://192.168.0.13:3306/store2", "root", "san30aks");
			
			return true;
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			return false;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		} finally{
			System.out.println("ah crap!");
		}
	}
	
	public static void Display(String args){
		System.out.println("com.se.test.displays " + "displaying String");
		System.out.println(args);
	}
	public static void Display(int args){
		System.out.println("com.se.test.displays " + "displaying int");
		System.out.println(args);
	}	
}