package com.toren;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.toren.server.ServerHandler;
import com.toren.server.mina.CodecFactory;
import com.toren.world.World;

public class Server {
	
	public static int PORT = 5123;
	public static World world;

	public static void main (String args[]) throws IOException {
		
		world = new World();
		
		IoAcceptor acc = new NioSocketAcceptor();
		acc.getFilterChain().addLast("codec", (IoFilter) new ProtocolCodecFilter(new CodecFactory()));
		acc.setHandler(new ServerHandler());
		acc.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
		
		acc.bind(new InetSocketAddress(5123));
		
		Console.println("Server started on port: " + PORT);
		new Thread(world).start();
		Console.println("World is active!");
		
	}

}
