package com.toren.world;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.toren.Console;
import com.toren.server.data.PacketCreator;
import com.toren.world.life.TorenPlayer;
import com.toren.world.map.Maps;
import com.toren.world.map.TorenMap;

public class World implements Runnable {

  private final ReadLock chrRLock;
  private final WriteLock chrWLock;

  private ArrayList<TorenMap> maps;
  private ArrayList<TorenPlayer> players;

  private boolean shutdown = false;

  private double physicsStep = 1.0 / 20.0;
  private long loop = 17;

  public World() {

    final ReentrantReadWriteLock chrLock = new ReentrantReadWriteLock(true);
    chrRLock = chrLock.readLock();
    chrWLock = chrLock.writeLock();

    Console.println("Loading Maps...");
    maps = new ArrayList<TorenMap>();
    loadMaps();

    players = new ArrayList<TorenPlayer>();

  }

  public void loadMaps() {
    int max = 0;
    for (Maps m : Maps.values()) {
      if (m.getValue() > max)
        max = m.getValue();
    }

    for (int i = 0; i < max + 1; i++) {
      maps.add(new TorenMap(i + ".json"));
    }
  }

  public void removePlayer(TorenPlayer player) {
    if (player != null) {
      chrWLock.lock();
      try {
        players.remove(player);
      } finally {
        chrWLock.unlock();
      }
      player.getMap().removePlayer(player);
    }
  }

  public void addPlayer(TorenPlayer player) {
    chrWLock.lock();
    try {
      players.add(player);
    } finally {
      chrWLock.unlock();
    }
  }

  public boolean isLoggedIn(String name) {
    chrRLock.lock();
    try {
      for (TorenPlayer chr : players) {
        if (chr.getName().equals(name))
          return true;
      }
    } finally {
      chrRLock.unlock();
    }
    return false;
  }

  public synchronized ArrayList<TorenMap> getMaps() {
    return maps;
  }
  
  public void update(float delta) {
    chrWLock.lock();
    try {
      for (TorenPlayer chr : players) {
        chr.update(delta);
      }
    } finally {
      chrWLock.unlock();
    }
  }

  @Override
  public void run() {

    double currentTime = System.currentTimeMillis() / 1000.0;
    double physAccumulator = 0;

    while (!shutdown) {
      double newTime = System.currentTimeMillis() / 1000.0;
      double delta = Math.min(newTime - currentTime, 0.25);
      physAccumulator += delta;
      currentTime = newTime;

      while (physAccumulator >= physicsStep) {
        update((float) physicsStep);
        physAccumulator -= physicsStep;
      }
      
      double endTime = System.currentTimeMillis() / 1000.0;
      long thisFrame = (long) ((endTime - newTime) * 1000.0);
      try {
        Thread.sleep(loop - thisFrame);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

}
