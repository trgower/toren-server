package com.toren.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.session.IoSession;

import com.toren.Console;
import com.toren.Server;
import com.toren.server.data.Passwords;
import com.toren.server.net.DBConnection;
import com.toren.world.life.TorenPlayer;

public class TorenClient {

	public static final String CLIENT_KEY = "CLIENT";
	private final Lock mutex = new ReentrantLock(true);
	private IoSession session;
	private boolean loggedIn;
	private TorenPlayer player;

	public TorenClient(IoSession session) {
		this.session = session;
		loggedIn = false;
		player = null;
	}

	public synchronized IoSession getSession() {
		return session;
	}

	public synchronized void sendPacket(final byte[] packet) {
		session.write(packet);
	}

	public final Lock getLock() {
		return mutex;
	}

	public int login(String user, String pass) {

		if (Server.world.isLoggedIn(user))
			return 3;
		
		Connection con = DBConnection.getNewConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("SELECT password,id FROM accounts WHERE username = ?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			if (rs.next()) {
				// check if banned mmk

				String stored = rs.getString("password");
				int id = rs.getInt("id");
				ps.close();
				rs.close();

				ps = con.prepareStatement("INSERT INTO iplog (accountid, ip) VALUES (?, ?)");
				ps.setInt(1, id);
				ps.setString(2, getIpAddress());
				ps.executeUpdate();
				ps.close();

				con.close();

				if (Passwords.check(pass, stored)) {
					loggedIn = true;
					player = loadCharacterFromDB(user);
					return 0;
				} else {
					return 1;
				}

			} else {
				return 2;
			}
		} catch (SQLException sqle) {
			Console.printException(sqle);
		}
		return 5; // 5 = Database error sorry folks

	}

	public int register(String user, String pass) {

		Connection con = DBConnection.getNewConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("SELECT id FROM accounts WHERE username = ?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			if (!rs.next()) {
				ps.close();
				rs.close();

				ps = con.prepareStatement("INSERT INTO accounts (username, password, health, pos_x, pos_y) VALUES (?, ?, ?, ?, ?)");
				ps.setString(1, user);
				ps.setString(2, pass);
				ps.setInt(3, 100);
				ps.setFloat(4, 20);
				ps.setFloat(5, 20);
				ps.executeUpdate();
				ps.close();

				con.close();

				return 0;

			} else {
				return 1;
			}
		} catch (SQLException sqle) {
			Console.printException(sqle);
		}
		return 5; // 5 = Database error sorry folks

	}
	
	public TorenPlayer loadCharacterFromDB(String user) {
		TorenPlayer tc = null;
		
		Connection con = DBConnection.getNewConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("SELECT id,pos_x,pos_y,direction,health,skin,hair,body,mapid FROM accounts WHERE username = ?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				float pos_x = rs.getFloat("pos_x");
				float pos_y = rs.getFloat("pos_y");
				short dir = rs.getShort("direction");
				int health = rs.getInt("health");
				short skin = rs.getShort("skin");
				short hair = rs.getShort("hair");
				short body = rs.getShort("body");
				int mapid = rs.getInt("mapid");
				
				ps.close();
				rs.close();
				con.close();
				
				return new TorenPlayer(id, user, pos_x, pos_y, health, dir, Server.world.getMaps().get(mapid), skin, hair, body, this);
			}
			
		} catch (SQLException slqe) {
			Console.printException(slqe);
		}
		
		return tc;
	}

	public String getIpAddress() {
		return session.getRemoteAddress().toString();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public TorenPlayer getPlayer() {
		return player;
	}
}
