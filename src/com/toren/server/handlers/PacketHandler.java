package com.toren.server.handlers;

import com.toren.client.TorenClient;
import com.toren.server.data.PacketReader;

public interface PacketHandler {

	void handlePacket(PacketReader pr, TorenClient c);
	boolean validateState(TorenClient c);
	
}
