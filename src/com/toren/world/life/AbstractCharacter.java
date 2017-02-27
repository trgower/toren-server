package com.toren.world.life;

import com.toren.world.map.TorenMap;

public abstract class AbstractCharacter extends AbstractLife {
	
	private short skin, hair, body;
	
	public AbstractCharacter(float x, float y, int health, int direction, TorenMap map, 
			short skin, short hair, short body) {
		super(x, y, 10f, 10f, health, direction, map); // set these bounds to match clients or whatever
		
		this.skin = skin;
		this.hair = hair;
		this.body = body;
		
	}

	public short getSkin() {
		return skin;
	}

	public short getHair() {
		return hair;
	}

	public short getBody() {
		return body;
	}

}
