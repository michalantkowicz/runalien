package com.apptogo.runalien.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class UserData {	
	public String key;
	//TODO make a 'type' enum instance
	public String type;
	public float width;
	public float height;
	
	public boolean ignore = false;
	
	public UserData() {
		type = "default";
	}
	
	public UserData(String key)
	{
		this();
		this.key = key;
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
