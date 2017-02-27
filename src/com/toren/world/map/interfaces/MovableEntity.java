package com.toren.world.map.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface MovableEntity extends Entity {
	
	boolean isMoving();
	Vector2 getVelocity();
	void setVelocity(float x, float y);

}
