package com.toren.server.net;

import java.sql.Connection;
import java.sql.DriverManager;

import com.toren.Console;

public class DBConnection {

	// yeah so later this should be like a connection pool that multiple threads can use
	// or not, fuck it....
	
	public static Connection getNewConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/toren?useSSL=false", 
					"root",
					"XhVua*mgRl(GaWLuZ01~");
		//e2~(J9jIkk%2LER#&#$G
		} catch (Exception e) {
			Console.printException(e);
		}
		
		return null;
	}
	
}
