package com.toren.server.handlers.login;

import com.toren.Server;
import com.toren.client.TorenClient;
import com.toren.server.data.PacketCreator;
import com.toren.server.data.PacketReader;
import com.toren.server.handlers.LoginPacketHandler;
public final class LoginRequestHandler extends LoginPacketHandler {

	@Override
	public void handlePacket(PacketReader pr, TorenClient c) {

		String user = pr.readTorenString();
		String pass = pr.readTorenString();

		int status = c.login(user, pass);

		c.sendPacket(PacketCreator.loginStatus(status, c.getPlayer()));
		
		if (status == 0) {
			c.getPlayer().getMap().addPlayer(c.getPlayer());
			Server.world.addPlayer(c.getPlayer());
		}

	}

}
