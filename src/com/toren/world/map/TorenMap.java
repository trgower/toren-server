package com.toren.world.map;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.math.Rectangle;
import com.toren.Console;
import com.toren.world.life.TorenPlayer;
import com.toren.world.map.interfaces.Entity;

public class TorenMap {

	private final ReadLock chrRLock;
	private final WriteLock chrWLock;
	
	private final ReadLock objectRLock;
	private final WriteLock objectWLock;

	private int width;
	private int height;
	private ArrayList<Entity> objects;
	private ArrayList<TorenPlayer> players;
	
	private ArrayList<Rectangle> colliders;

	public TorenMap(String jsonMap) {
	  
	  colliders = new ArrayList<Rectangle>();
		
		try {
			InputStream is = new FileInputStream(getClass().getClassLoader().getResource("maps/" + jsonMap).getPath());
			@SuppressWarnings("deprecation")
			String jsonTxt = IOUtils.toString(is);
			JSONObject json = new JSONObject(jsonTxt);

			width = json.getInt("width") * json.getInt("tilewidth");
			height = json.getInt("height") * json.getInt("tileheight");

			players = new ArrayList<TorenPlayer>();

			objects = new ArrayList<Entity>();
			JSONArray jObjects = json.getJSONArray("objects");
			for (int i = 0; i < jObjects.length(); i++) {
				JSONObject curr = jObjects.getJSONObject(i);
				if (curr != null) {
					int x = curr.getInt("x");
					int y = curr.getInt("y");
					int width = curr.getInt("width");
					int height = curr.getInt("height");
					
					colliders.add(new Rectangle(x, y, width, height));

					String type = curr.getString("type");

					if (type.equals("container")) {
						JSONObject props = curr.getJSONObject("properties");
						/*objects.add(new TorenBox(x, y, width, height, props.getString("type"),
								props.getString("material")));*/
					}
				}
			}

			Console.println("Map " + jsonMap.substring(0, jsonMap.indexOf('.')) + " loaded");

		} catch (IOException | JSONException e) {
			Console.printException(e);
		}

		final ReentrantReadWriteLock chrLock = new ReentrantReadWriteLock(true);
		chrRLock = chrLock.readLock();
		chrWLock = chrLock.writeLock();

		final ReentrantReadWriteLock objectLock = new ReentrantReadWriteLock(true);
		objectRLock = objectLock.readLock();
		objectWLock = objectLock.writeLock();

	}
	
	public ArrayList<Rectangle> getMapColliders() {
	  return colliders;
	}

	public void addPlayer(TorenPlayer player) {
		chrWLock.lock();
		try {
			players.add(player);
		} finally {
			chrWLock.unlock();
		}
		
		chrRLock.lock();
		try {
			player.updateCulling();
		} finally {
			chrRLock.unlock();
		}
		
	}

	public void removePlayer(TorenPlayer player) {
		chrWLock.lock();
		try {
			players.remove(player);
		} finally {
			chrWLock.unlock();
		}
	}

	public ArrayList<TorenPlayer> getPlayers() {
		return players;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
