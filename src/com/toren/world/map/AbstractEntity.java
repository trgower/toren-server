package com.toren.world.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toren.world.map.interfaces.Entity;

public abstract class AbstractEntity implements Entity {
	
	private Vector2 position;
	private Rectangle bounds;
	
	public AbstractEntity(float x, float y, float boundWidth, float boundHeight) {
		position = new Vector2(x, y);
		
		bounds = new Rectangle();
		bounds.setCenter(position);
		bounds.setSize(boundWidth, boundHeight);
	}
	
	@Override
	public synchronized Vector2 getPosition() {
		return position;
	}
	
	@Override
	public synchronized void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	@Override
	public synchronized Rectangle getBounds() {
		return bounds;
	}

}
