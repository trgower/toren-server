package com.toren.server.handlers.world;

import com.toren.client.TorenClient;
import com.toren.server.data.PacketReader;
import com.toren.server.handlers.WorldPacketHandler;
import com.toren.server.handlers.commands.KeyCommand;

public final class KeyReleasedHandler extends WorldPacketHandler {

	@Override
	public void handlePacket(PacketReader pr, TorenClient c) {
	  
	  int tick = pr.readInt();
    int cmdNum = pr.readInt();
    int keycode = pr.readInt();
    
    c.getPlayer().addCommand(new KeyCommand(c.getPlayer(), false, keycode, tick, cmdNum));
	  
	}

}
