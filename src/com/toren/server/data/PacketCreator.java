package com.toren.server.data;

import com.badlogic.gdx.math.Vector2;
import com.toren.server.net.SendOpcodes;
import com.toren.world.life.TorenPlayer;

public class PacketCreator {

	public static byte[] loginStatus(int status, TorenPlayer player) {

		PacketWriter pw = new PacketWriter();

		if (status != 0)
			pw.writeShort((short) 4); // Don't send character information
		else
			pw.writeShort((short) 24); // Success, so send it mmk

		pw.writeShort((short) SendOpcodes.LOGIN_RESPONSE.getValue());
		pw.writeShort((short) status);

		if (status == 0) {
			pw.writeFloat(player.getPosition().x);
			pw.writeFloat(player.getPosition().y);
			pw.writeShort((short) player.getSpriteDirection());
			pw.writeInt(player.getHealth());
			pw.writeShort(player.getSkin());
			pw.writeShort(player.getHair());
			pw.writeShort(player.getBody());
		}

		return pw.getPacket();

	}

	public static byte[] registerStatus(int status) {

		PacketWriter pw = new PacketWriter();
		pw.writeShort((short) 4);
		pw.writeShort((short) SendOpcodes.REGISTER_RESPONSE.getValue());
		pw.writeShort((short) status);

		return pw.getPacket();

	}
	
	public static byte[] serverRec(int tick, Vector2 pos) {
	  
	  PacketWriter pw = new PacketWriter();
	  
	  pw.writeShort((short) 14);
	  pw.writeShort((short) SendOpcodes.STATE_UPDATE.getValue());
	  pw.writeInt(tick);
	  pw.writeFloat(pos.x);
	  pw.writeFloat(pos.y);
	  
	  return pw.getPacket();
	  
	}

}
