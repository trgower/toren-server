package com.toren;

import java.sql.Timestamp;

public class Console {
	
	public static void println(String msg) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
    System.out.println("[" + ts.toString().substring(5,19) + "] " + msg);
	}
	
	public static void printNoTS(String msg) {
    System.out.println(msg);
	}
	
	public static void print(String msg) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
    System.out.print("[" + ts.toString().substring(5,19) + "] " + msg);
	}
	
	public static void printError(String msg) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
    System.out.println("[" + ts.toString().substring(5,19) + "] ERROR: " + msg);
	}
	
	public static void printException(Throwable cause) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
    System.out.println("[" + ts.toString().substring(5,19) + "] EXCEPTION");
    cause.printStackTrace();
	}

}
