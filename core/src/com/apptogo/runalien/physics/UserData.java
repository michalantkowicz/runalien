package com.apptogo.runalien.physics;

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
}
