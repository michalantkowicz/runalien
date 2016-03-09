package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class GroundGenerating extends AbstractPlugin {

	Body groundBody;
	
	@Override
	public void postSetActor() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.set( new Vector2(0, 0) );
		
		FixtureDef fixtureDef = new FixtureDef();
		
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(5, 0.1f);
		
		fixtureDef.shape = groundShape;
		
		groundBody = GameScreen.getWorld().createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
		
		groundShape.dispose();
	}
	
	@Override
	public void run() { 
		groundBody.setTransform(body.getPosition().x, 0, groundBody.getAngle());
	}

}
