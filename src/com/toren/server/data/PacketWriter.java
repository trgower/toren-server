package com.toren.server.data;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class PacketWriter {

	private ByteArrayOutputStream bs;
	private static Charset ASCII = Charset.forName("US-ASCII");
	
	public PacketWriter() {
		bs = new ByteArrayOutputStream();
	}
	
	public byte[] getPacket() {
		return bs.toByteArray();
	}
	
	
	public void write(byte b) {
		bs.write(b);
	}
	
	public void write(int b) {
		bs.write((byte) b);
	}
	
	public void write(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			bs.write(b[i]);
		}
	}
	
	public void writeShort(short s) {
		bs.write((byte) (s & 0xFF));
		bs.write((byte) ((s >>> 8) & 0xFF));
	}
	
	public void writeInt(int i) {
		bs.write((byte) (i & 0xFF));
		bs.write((byte) ((i >>> 8) & 0xFF));
		bs.write((byte) ((i >>> 16) & 0xFF));
		bs.write((byte) ((i >>> 24) & 0xFF));
	}
	
	public void writeFloat(float f) {
		writeInt(Float.floatToIntBits(f));
	}
	
	public void writeString(String s) {
		write(s.getBytes(ASCII));
	}
	
	public void writeTorenString(String s) {
		writeShort((short) s.length());
		writeString(s);
	}
	
	public void writeLong(long l) {
		bs.write((byte) (l & 0xFF));
		int s = 8;
		for (int i = 0; i < 7; i++) {
			bs.write((byte) ((l >>> s) & 0xFF));
			s += 8;
		}
	}
	
	public void writeBool(final boolean b) {
		write(b ? 1 : 0);
	}
	
}
