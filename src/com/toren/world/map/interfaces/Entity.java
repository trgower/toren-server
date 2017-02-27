package com.toren.world.map.interfaces;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Entity {
	
	byte[] serialize();
	byte[] serializeRemove();
	Vector2 getPosition();
	void setPosition(float x, float y);
	Rectangle getBounds();
	int getId();
	void update(float delta);

}
