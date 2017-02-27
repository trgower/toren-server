package com.toren.server.handlers.commands;

public interface Command {
  
  void process();
  int getTick();

}
