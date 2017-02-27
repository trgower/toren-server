package com.toren.server.handlers;

import com.toren.client.TorenClient;

public abstract class LoginPacketHandler implements PacketHandler {
	
	@Override
	public boolean validateState(TorenClient c) {
		return !c.isLoggedIn();
	}

}
