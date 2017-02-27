package com.toren.world.life;

import com.toren.world.life.interfaces.Life;
import com.toren.world.map.AbstractMovableEntity;
import com.toren.world.map.TorenMap;

public abstract class AbstractLife extends AbstractMovableEntity implements Life {
	
	private int health;
	private int spriteDirection;

	public AbstractLife(float x, float y, float boundWidth, float boundHeight, int health, 
			int direction, TorenMap map) {
		super(x, y, boundWidth, boundHeight, direction, map);
		this.health = health;
		this.spriteDirection = direction;
	}
	
	@Override
	public synchronized int getHealth() {
		return health;
	}
	
	@Override
	public synchronized void damage(int health) {
		this.health -= health;
	}
	
	@Override
	public synchronized void heal(int health) {
		this.health += health;
	}
	
	@Override
	public synchronized int getSpriteDirection() {
		return spriteDirection;
	}
	
	@Override
	public synchronized void setSpriteDirection(int d) {
		spriteDirection = d;
	}

}
