package com.toren.server.handlers.login;

import com.toren.client.TorenClient;
import com.toren.server.data.PacketCreator;
import com.toren.server.data.PacketReader;
import com.toren.server.handlers.LoginPacketHandler;

public final class RegisterRequestHandler extends LoginPacketHandler {

	@Override
	public void handlePacket(PacketReader pr, TorenClient c) {

		String user = pr.readTorenString();
		String pass = pr.readTorenString();

		int status = c.register(user, pass);

		c.sendPacket(PacketCreator.registerStatus(status));

	}

}
