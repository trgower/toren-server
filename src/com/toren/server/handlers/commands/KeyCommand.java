package com.toren.server.handlers.commands;

import com.toren.Console;
import com.toren.world.life.TorenPlayer;

public class KeyCommand implements Command {
  
  private TorenPlayer player;
  private boolean pressed;
  private int keycode, tick, cmdNum;

  public KeyCommand(TorenPlayer player, boolean pressed, int keycode, int tick,
      int cmdNum) {
    
    this.player = player;
    this.pressed = pressed;
    this.keycode = keycode;
    this.tick = tick;
    this.cmdNum = cmdNum;
    
  }
  
  @Override
  public void process() {
    if (pressed) {
      player.keyPressed(tick, cmdNum, keycode);
    } else {
      player.keyReleased(tick, cmdNum, keycode);
    }
    //Console.println("Processed command number: " + cmdNum);
  }

  @Override
  public int getTick() {
    return tick;
  }

}
