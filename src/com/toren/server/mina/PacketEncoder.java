package com.toren.server.mina;

import java.util.concurrent.locks.Lock;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.toren.client.TorenClient;

public class PacketEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object msg, ProtocolEncoderOutput out) throws Exception {

		final TorenClient client = (TorenClient) session.getAttribute(TorenClient.CLIENT_KEY);
		if (client != null) {
			final Lock mutex = client.getLock();
			mutex.lock();
			try {
				out.write(IoBuffer.wrap((byte[]) msg)); // literally just writes the message as is
			} finally {
				mutex.unlock();
			}
		} else {
			// No client? 
		}

	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// do stuff?
	}


}
