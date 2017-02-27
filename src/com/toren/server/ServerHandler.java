package com.toren.server;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.toren.Console;
import com.toren.Server;
import com.toren.client.TorenClient;
import com.toren.server.data.PacketReader;
import com.toren.server.handlers.PacketHandler;
import com.toren.server.net.PacketProcessor;

public class ServerHandler extends IoHandlerAdapter {

	private PacketProcessor processor;

	public ServerHandler() {
		processor = new PacketProcessor();
	}

	@Override
	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception {
		Console.printException(cause);
	}

	@Override
	public void messageReceived( IoSession session, Object msg ) throws Exception {

		TorenClient client = (TorenClient) session.getAttribute(TorenClient.CLIENT_KEY);
		PacketReader pr = new PacketReader((byte[]) msg);
		short opcode = pr.readShort();
		final PacketHandler packetHandler = processor.getHandler(opcode);
		if (packetHandler != null && packetHandler.validateState(client)) {
			packetHandler.handlePacket(pr, client);
		}

	}

	@Override
	public void sessionOpened(IoSession session) {

		TorenClient client = new TorenClient(session);
		session.setAttribute(TorenClient.CLIENT_KEY, client);
		// Write hello stuff
		Console.println("Session Opened: " + session.getRemoteAddress());

	}

	@SuppressWarnings("deprecation")
	@Override
	public void sessionClosed(IoSession session) {

		TorenClient client = (TorenClient) session.getAttribute(TorenClient.CLIENT_KEY);
		Console.println(client.getPlayer().getName() + " Session Closed: " + session.getRemoteAddress());
		session.close();
		session.removeAttribute(TorenClient.CLIENT_KEY);

		if (client != null) {
			Server.world.removePlayer(client.getPlayer());
		}
	}

	@Override
	public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
		//Console.print("IDLE " + session.getIdleCount(status));
	}

}
