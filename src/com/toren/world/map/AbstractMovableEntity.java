package com.toren.world.map;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toren.world.map.interfaces.MovableEntity;

public abstract class AbstractMovableEntity extends AbstractEntity implements MovableEntity {

  private Vector2 velocity;
  private float speed = 100f;

  // controller physics shit
  private ArrayList<Integer> directions;
  private int direction;
  private Vector2 directionVector;
  private TorenMap map;


  public AbstractMovableEntity(float x, float y, float boundWidth, float boundHeight, int direction, 
      TorenMap map) {
    super(x, y, boundWidth, boundHeight);
    velocity = new Vector2(0, 0);

    this.directions = new ArrayList<Integer>();
    this.direction = direction;
    this.directionVector = new Vector2(0, 0);
    
    this.map = map;
  }

  @Override
  public void update(float delta) {
    Collision collision = getCollision(delta);
    getPosition().set(collision.position);
    getBounds().setCenter(getPosition());
  }

  @Override
  public Vector2 getVelocity() {
    return velocity;
  }

  @Override
  public void setVelocity(float x, float y) {
    velocity.set(x, y);
  }

  @Override
  public boolean isMoving() {
    return !velocity.isZero();
  }

  // OOOH
  public void changeVelocity() {
    getVelocity().set(speed * directionVector.x, speed * directionVector.y);
  }

  public void addDirection(int d) {
    if (!directions.contains(d)) {
      directions.add(d);
      updateDirection();
    }
  }
  
  public TorenMap getMap() {
    return map;
  }

  public void removeDirection(int d) {
    directions.remove(new Integer(d));
    updateDirection();
  }

  public Collision getCollision(float delta) {
    Vector2 futurePosition = new Vector2(getPosition());
    Rectangle futureBounds = new Rectangle(getBounds());

    futurePosition.add(getVelocity().cpy().scl(delta));
    futureBounds.setCenter(futurePosition);

    Rectangle x = null, y = null;
    ArrayList<Rectangle> xTiles = new ArrayList<Rectangle>();
    ArrayList<Rectangle> yTiles = new ArrayList<Rectangle>();

    float cX = futurePosition.x - futureBounds.getWidth() / 2;
    float cY = futurePosition.y - futureBounds.getHeight() / 2;

    switch (direction) {
    case 0:
      y = new Rectangle(cX + 2, cY + 2, futureBounds.getWidth() - 4, 128);
      break;
    case 1:
      x = new Rectangle(cX + 2, cY + 2, 128, futureBounds.getHeight() - 4);
      y = new Rectangle(cX + 2, cY + 2, futureBounds.getWidth() - 4, 128);
      break;
    case 2:
      x = new Rectangle(cX + 2, cY + 2, 128, futureBounds.getHeight() - 4);
      break;
    case 3:
      x = new Rectangle(cX + 2, cY + 2, 128, futureBounds.getHeight() - 4);
      y = new Rectangle(cX + 2, 0, futureBounds.getWidth() - 4, cY - 1);
      break;
    case 4:
      y = new Rectangle(cX + 2, 0, futureBounds.getWidth() - 4, cY - 1);
      break;
    case 5:
      x = new Rectangle(0, cY + 2, cX, futureBounds.getHeight() - 4);
      y = new Rectangle(cX + 2, 0, futureBounds.getWidth() - 4, cY - 1);
      break;
    case 6:
      x = new Rectangle(0, cY + 2, cX, futureBounds.getHeight() - 4);
      break;
    case 7:
      x = new Rectangle(0, cY + 2, cX, futureBounds.getHeight() - 4);
      y = new Rectangle(cX + 2, cY + 2, futureBounds.getWidth() - 4, 128);
      break;

    }

    for (int i = 0; i < map.getMapColliders().size(); i++) {
      Rectangle rect = map.getMapColliders().get(i);
      if (y != null && rect.overlaps(y)) {
        yTiles.add(rect);
      } else if (x != null && rect.overlaps(x)) {
        xTiles.add(rect);
      }
    }

    boolean collisionX = false;
    boolean collisionY = false;

    for (Rectangle r : yTiles) {
      if (r.overlaps(futureBounds)) {
        collisionY = true;
      }
    } 
    for (Rectangle r : xTiles) {
      if (r.overlaps(futureBounds)) {
        collisionX = true;
      }
    }

    boolean fX = collisionX, fY = collisionY;

    while (fX || fY) {
      futurePosition.sub(getVelocity().cpy().scl(delta / 5f));
      futureBounds.setCenter(futurePosition);
      boolean rY = false;
      for (Rectangle r : yTiles) {
        if (r.overlaps(futureBounds)) {
          rY = true;
        }
      } 
      fY = rY;
      boolean rX = false;
      for (Rectangle r : xTiles) {
        if (r.overlaps(futureBounds)) {
          rX = true;
        }
      }
      fX = rX;
    }
    
    return new Collision(collisionX, collisionY, futurePosition); 
  }

  public void updateDirection() {
    int od = direction;
    if (directions.size() == 1) {
      direction = directions.get(0);
      setDirectionVector();
    } else if (directions.size() > 1) {
      if (directions.get(0) == 0 && directions.get(1) == 6 
          || directions.get(0) == 6 && directions.get(1) == 0) {
        direction = 7;
        setDirectionVector();
      } else if (directions.get(0) == 0 && directions.get(1) == 2 
          || directions.get(0) == 2 && directions.get(1) == 0) {
        direction = 1;
        setDirectionVector();
      } else if (directions.get(0) == 2 && directions.get(1) == 4 
          || directions.get(0) == 4 && directions.get(1) == 2) {
        direction = 3;
        setDirectionVector();
      } else if (directions.get(0) == 4 && directions.get(1) == 6 
          || directions.get(0) == 6 && directions.get(1) == 4) {
        direction = 5;
        setDirectionVector();
      } 
    }
    

    if (directions.size() == 0 && !getVelocity().isZero()) {
      velocity.set(0, 0);
    } else if (od != direction || getVelocity().isZero()) {
      changeVelocity();
    }

  }

  public void setDirectionVector() {
    float splitSpeed = (float) (Math.sqrt((speed * speed) / 2) / speed);
    switch (direction) {
    case 0:
      directionVector.set(0, 1);
      break;
    case 2:
      directionVector.set(1, 0);
      break;
    case 4:
      directionVector.set(0, -1);
      break;
    case 6:
      directionVector.set(-1, 0);
      break;
    case 7:
      directionVector.set(-splitSpeed, splitSpeed);
      break;
    case 1:
      directionVector.set(splitSpeed, splitSpeed);
      break;
    case 3:
      directionVector.set(splitSpeed, -splitSpeed);
      break;
    case 5:
      directionVector.set(-splitSpeed, -splitSpeed);
      break;
    }
  }

  class Collision {

    boolean collisionX, collisionY;
    Vector2 position;

    public Collision(boolean cX, boolean cY, Vector2 pos) {

      collisionX = cX;
      collisionY = cY;
      position = pos;

    }

  }

}
