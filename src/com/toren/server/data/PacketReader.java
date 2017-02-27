package com.toren.server.data;

import java.io.ByteArrayInputStream;

public class PacketReader {
	
	private ByteArrayInputStream bs;
	
	public PacketReader(byte[] packet) {
		bs = new ByteArrayInputStream(packet);
	}
	
	public byte readByte() {
		return (byte) bs.read();
	}
	
	public int readInt() {
		return bs.read() + (bs.read() << 8) + (bs.read() << 16) + (bs.read() << 24);
	}
	
	public short readShort() {
		return (short) (bs.read() + (bs.read() << 8));
	}
	
	public char readChar() {
		return (char) readShort();
	}
	
	public long readLong() {
		long result = bs.read();
		int s = 8;
		for (int i = 0; i < 7; i++) {
			result += (bs.read() << s);
			s += 8;
		}
		return result;
	}
	
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}
	
	public final String readString(int n) {
		char result[] = new char[n];
		for (int i = 0; i < n; i++) {
			result[i] = (char) readByte();
		}
		return String.valueOf(result);
	}
	
	public String readTorenString() {
		return readString(readShort());
	}
	
	public boolean readBoolean() {
		return bs.read() == 1;
	}
	
	public long available() {
		return bs.available();
	}
	
}
