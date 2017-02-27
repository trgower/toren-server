package com.toren.server.net;

public enum RecieveOpcodes {

	LOGIN_REQUEST(1),
	REGISTER_REQUEST(2),
	KEY_PRESSED(3),
  KEY_RELEASED(4),
  PING(5),
  PONG(6);
	
	private int code = -1;
	
	private RecieveOpcodes(int code) {
		this.code = code;
	}
	
	public int getValue() {
		return code;
	}
	
}
