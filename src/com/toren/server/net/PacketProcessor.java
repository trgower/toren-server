package com.toren.server.net;

import com.toren.Console;
import com.toren.server.handlers.*;
import com.toren.server.handlers.login.*;
import com.toren.server.handlers.world.KeyPressedHandler;
import com.toren.server.handlers.world.KeyReleasedHandler;
import com.toren.server.handlers.world.PingHandler;
import com.toren.server.handlers.world.PongHandler;

public final class PacketProcessor {

	private PacketHandler[] handlers;

	public PacketProcessor() {

		int maxRecieveCode = 0;
		for (RecieveOpcodes op : RecieveOpcodes.values()) {
			if (op.getValue() > maxRecieveCode) {
				maxRecieveCode = op.getValue();
			}
		}
		handlers = new PacketHandler[maxRecieveCode + 1];

		registerHandlers();

	}

	public PacketHandler getHandler(short opcode) {

		if (opcode > handlers.length) {
			return null;
		}
		PacketHandler handler = handlers[opcode];
		if (handler != null) {
			return handler;
		}
		return null;

	}

	public void registerHandler(RecieveOpcodes code, PacketHandler handler) {

		try {
			handlers[code.getValue()] = handler;
		} catch (ArrayIndexOutOfBoundsException e) {
			Console.printError("Failed to register opcode " + code.name());
		}

	}

	public void registerHandlers() {

	  registerHandler(RecieveOpcodes.PING, new PingHandler());
	  registerHandler(RecieveOpcodes.PONG, new PongHandler());
		registerHandler(RecieveOpcodes.LOGIN_REQUEST, new LoginRequestHandler());
		registerHandler(RecieveOpcodes.REGISTER_REQUEST, new RegisterRequestHandler());
		registerHandler(RecieveOpcodes.KEY_PRESSED, new KeyPressedHandler());
		registerHandler(RecieveOpcodes.KEY_RELEASED, new KeyReleasedHandler());

	}

}
