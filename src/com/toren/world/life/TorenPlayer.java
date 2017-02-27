package com.toren.world.life;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toren.Console;
import com.toren.client.TorenClient;
import com.toren.server.data.PacketCreator;
import com.toren.server.data.PacketWriter;
import com.toren.server.handlers.commands.Command;
import com.toren.world.map.TorenMap;

public class TorenPlayer extends AbstractCharacter {

  private TorenClient client;
  private String name;
  private int id;
  private Rectangle culling;
  private ArrayList<TorenPlayer> playersInCulling;

  private ConcurrentLinkedQueue<Command> commandBuffer;
  private int tick = 0;
  private boolean wait = true;
  private int buffer = 10;
  private int waitTicks = buffer;
  private int waited = 0;

  public TorenPlayer(int id, String name, float x, float y, int health, int direction, 
      TorenMap map, short skin, short hair, short body, TorenClient client) {
    super(x, y, health, direction, map, skin, hair, body);

    this.client = client;
    this.name = name;
    this.id = id;

    this.culling = new Rectangle();
    this.culling.setSize(416, 248);
    this.culling.setCenter(getPosition());

    playersInCulling = new ArrayList<TorenPlayer>();

    commandBuffer = new ConcurrentLinkedQueue<Command>();

  }

  @Override
  public void update(float delta) {
    if (!wait) {
      boolean c = false;
      Command cmd = commandBuffer.peek();
      if (cmd != null) {
        if (cmd.getTick() == tick) {
          commandBuffer.poll().process();
          c = true;
        }
      }
      
      super.update(delta);
      this.culling.setCenter(getPosition());
      updateCulling();
      
      if (c) System.out.println(tick + ": " + getPosition());
      
      // Send world update!
      client.sendPacket(PacketCreator.serverRec(tick, getPosition()));
      
      tick++;
    } else {
      waited++;
      if (waited >= waitTicks) {
        waited = 0;
        waitTicks = 0;
        wait = false;
      }
    }
  }
  
  public void addCommand(Command cmd) {
    commandBuffer.add(cmd);
  }

  public void keyPressed(int tick, int cmdNum, int keycode) {
    if (tick < this.tick)
      Console.println("Late: " + cmdNum + ", " + keycode);
      
    switch (keycode) {
    case Keys.W:
    case Keys.UP:
      addDirection(0);
      break;
    case Keys.A:
    case Keys.LEFT:
      addDirection(6);
      break;
    case Keys.S:
    case Keys.DOWN:
      addDirection(4);
      break;
    case Keys.D:
    case Keys.RIGHT:
      addDirection(2);
      break;
    }
    
    
  }

  public void keyReleased(int tick, int cmdNum, int keycode) {
    if (tick < this.tick)
      Console.println("Late: " + cmdNum + ", " + keycode);
    
    switch (keycode) {
    case Keys.W:
    case Keys.UP:
      removeDirection(0);
      break;
    case Keys.A:
    case Keys.LEFT:
      removeDirection(6);
      break;
    case Keys.S:
    case Keys.DOWN:
      removeDirection(4);
      break;
    case Keys.D:
    case Keys.RIGHT:
      removeDirection(2);
      break;
    }
    
  }

  @Override
  public synchronized void setPosition(float x, float y) {
    super.setPosition(x, y);
    this.culling.setCenter(getPosition());
  }

  public synchronized ArrayList<TorenPlayer> getPlayersInCulling() {
    return playersInCulling;
  }

  public synchronized TorenClient getClient() {
    return client;
  }

  public synchronized void setClient(TorenClient client) {
    this.client = client;
  }

  @Override
  public synchronized int getId() {
    return id;
  }

  public synchronized void setId(int id) {
    this.id = id;
  }

  public synchronized String getName() {
    return name;
  }

  public synchronized void setName(String name) {
    this.name = name;
  }

  public synchronized Rectangle getCulling() {
    return this.culling;
  }

  public synchronized void updateCulling() {
    for (TorenPlayer chr : getMap().getPlayers()) {
      if (chr != this && culling.contains(chr.getPosition())) {
        if (!playersInCulling.contains(chr)) {
          playersInCulling.add(chr);
          //addUpdate(new CullingUpdate(chr, true));
        }
      } else if (chr != this && !culling.contains(chr.getPosition())) {
        if (playersInCulling.contains(chr)) {
          playersInCulling.remove(chr);
          //addUpdate(new CullingUpdate(chr, false));
        }
      }
    }
  }

  @Override
  public synchronized byte[] serialize() {
    PacketWriter pw = new PacketWriter();

    pw.write((byte) 1); // 1 - player
    pw.writeInt(id);
    pw.writeTorenString(name);
    pw.writeFloat(getPosition().x);
    pw.writeFloat(getPosition().y);
    pw.write((byte) getSpriteDirection());
    pw.writeInt(getHealth());
    pw.write((byte) getSkin());
    pw.write((byte) getHair());
    pw.write((byte) getBody());

    return pw.getPacket();
  }

  @Override
  public byte[] serializeRemove() {
    PacketWriter pw = new PacketWriter();

    pw.write((byte) 1); // 1 - player
    pw.writeInt(id);

    return pw.getPacket();
  }
  
  // controller physics shit
  
  

}
