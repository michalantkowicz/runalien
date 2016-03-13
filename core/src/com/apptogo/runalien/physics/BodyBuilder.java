package com.apptogo.runalien.physics;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BodyBuilder {

	World world;
	
	BodyDef bodyDef;
	Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
	Array<String> fixturePostfixes = new Array<String>();
	
	UserData userData;
	
	public static BodyBuilder get()
	{
		return new BodyBuilder(GameScreen.getWorld());
	}
	
	public BodyBuilder(World world)
	{
		this.world = world;
		this.bodyDef = new BodyDef();
		fixtureDefs.add(new FixtureDef());
		fixturePostfixes.add("");
		this.userData = new UserData();
	}
	
	public BodyBuilder name(String name)
	{
		userData.key = name;
		return this;
	}
	
	public BodyBuilder type(BodyType type)
	{
		bodyDef.type = type;
		return this;
	}
	
	public BodyBuilder position(float x, float y)
	{
		bodyDef.position.set(x, y);
		return this;
	}
	
	public BodyBuilder circle(float radius)
	{
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		fixtureDefs.peek().shape = circle;
		
		userData.width = radius;
		userData.height = radius;
		
		return this;
	}
	
	public BodyBuilder box(float width, float height)
	{
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(width/2f, height/2f);
		
		fixtureDefs.peek().shape = polygon;
		
		userData.width = width;
		userData.height = height;
		
		return this;
	}
	
	public BodyBuilder loop(float[] vertices)
	{
		ChainShape chain = new ChainShape();
		chain.createLoop(vertices);
		
		fixtureDefs.peek().shape = chain;
		
		//Calculating width/height (looking for min/max x and y in the vertices array)
		float min_x = vertices[0], max_x = vertices[0];
		float min_y = vertices[1], max_y = vertices[1];
		
		for(int i = 0; i < vertices.length/2; i++)
		{
			if(vertices[i] < min_x) min_x = vertices[i];
			if(vertices[i] > max_x) max_x = vertices[i];
			if(vertices[i + 1] < min_y) min_y = vertices[i];
			if(vertices[i + 1] > max_y) max_y = vertices[i];				
		}
			
		userData.width = max_x - min_x;
		userData.height = max_y - min_y;
		
		return this;
	}
	
	public BodyBuilder friction(float friction)
	{
		fixtureDefs.peek().friction = friction;
		return this;
	}
	
	public BodyBuilder restitution(float restitution)
	{
		fixtureDefs.peek().restitution = restitution;
		return this;
	}
	
	public BodyBuilder density(float density)
	{
		fixtureDefs.peek().density = density;
		return this;
	}
	
	public BodyBuilder sensor(boolean sensor)
	{
		fixtureDefs.peek().isSensor = sensor;
		return this;
	}
	
	public BodyBuilder origin(float x, float y)
	{
		//set fixture position
		return this;
	}
	
	public BodyBuilder addFixture(String postfix)
	{
		fixtureDefs.add(new FixtureDef());
		fixturePostfixes.add(postfix);
		return this;
	}
	
	public Body create()
	{
		Body body = world.createBody(bodyDef);
		body.setUserData(userData);
		
		String name = userData.key;
		
		for(int i = 0; i < fixtureDefs.size; i++) {
			userData.key = name + fixturePostfixes.get(i);
			body.createFixture(fixtureDefs.get(i)).setUserData(userData);
		}
		
		return body;
	}
}
