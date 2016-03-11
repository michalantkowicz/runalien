package com.apptogo.runalien.physics;

import com.apptogo.runalien.main.Main;

public class UserData {
	private static int nextId = 0;
	private int id;
	
	public String key;
	public float width;
	public float height;
	
	public UserData()
	{
		id = nextId++;
		
		if(nextId >= Main.MAX_BODIES_COUNT)
			throw new RuntimeException("You cannot create more than " + String.valueOf(Main.MAX_BODIES_COUNT) + " objects!");
	}
	
	public UserData(String key)
	{
		this();
		this.key = key;
	}
	
	public int getId() {
		return id;
	}	
}
