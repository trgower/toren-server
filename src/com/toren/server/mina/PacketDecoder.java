package com.toren.server.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PacketDecoder extends CumulativeProtocolDecoder {

	private static final String DECODER_STATE_KEY = PacketDecoder.class.getName() + ".STATE";

	private static class DecoderState {
		public short packetLength = -1;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
		if (decoderState == null) {
			decoderState = new DecoderState();
			session.setAttribute(DECODER_STATE_KEY, decoderState);
		}
		if (in.remaining() >= 2 && decoderState.packetLength == -1) {
			decoderState.packetLength = Short.reverseBytes(in.getShort()); // uuhhh, yeah
		} else if (in.remaining() < 2 && decoderState.packetLength == -1) {
			return false;
		}
		if (in.remaining() >= decoderState.packetLength) {
			byte packet[] = new byte[decoderState.packetLength];
			in.get(packet, 0, decoderState.packetLength);
			decoderState.packetLength = -1;
			out.write(packet);
			return true;
		}
		return false;

	}

}
