package com.apptogo.runalien.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class UserData {
	private static int nextId = 0;
	private int id;
	
	public String key;
	public float width;
	public float height;
	
	public boolean ignore = false;
	
	public UserData()
	{
		id = nextId++;
	}
	
	public UserData(String key)
	{
		this();
		this.key = key;
	}
	
	public int getId() {
		return id;
	}	
	
	public static UserData get(Fixture fixture)
	{
		return ((UserData)fixture.getUserData());
	}
	
	public static UserData get(Body body)
	{
		return ((UserData)body.getUserData());
	}
}
