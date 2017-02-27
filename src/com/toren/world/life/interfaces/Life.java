package com.toren.world.life.interfaces;

public interface Life {
	
	int getHealth();
	void damage(int health);
	void heal(int health);
	int getSpriteDirection();
	void setSpriteDirection(int d);

}
