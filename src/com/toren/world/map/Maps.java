package com.toren.world.map;

public enum Maps {
	
	TEST_ISLAND(0);
	
	private int id = -1;
	private Maps(int i) {
		id = i;
	}
	
	public int getValue() {
		return id;
	}

}
