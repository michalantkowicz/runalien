package com.apptogo.runalien.plugin;

import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class GroundRepeating extends AbstractPlugin {

	Body groundBody;
	
	//if ground image width at least of screen width there should be 3 grounds in array
	Image[] ground = new Image[]{Image.get("ground").scale(1/UnitConverter.PPM), Image.get("ground").scale(1/UnitConverter.PPM), Image.get("ground").scale(1/UnitConverter.PPM)};
	
	@Override
	public void postSetActor() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.set( new Vector2(0, GameScreen.getGroundLevel()) );
		
		FixtureDef fixtureDef = new FixtureDef();
		
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(5, 0.1f);
		
		fixtureDef.shape = groundShape;
		
		groundBody = GameScreen.getWorld().createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
		
		groundShape.dispose();
		
		for(int i = 0; i < ground.length; i++) {
			ground[i].position((i-(ground.length-1))*ground[i].getWidth(), GameScreen.getGroundLevel() -ground[0].getHeight() + 0.1f).debug();
			GameScreen.getGameworldStage().addActor(ground[i]);
		}
	}
	
	@Override
	public void run() { 
		groundBody.setTransform(body.getPosition().x, GameScreen.getGroundLevel(), groundBody.getAngle());
		
		for(int i = 0; i < ground.length; i++)
			if(actor.getX() - ground[i].getX() > (ground.length-1)*ground[i].getWidth())
				ground[i].position(ground[i].getX() + (ground.length)*ground[i].getWidth(), ground[i].getY());
	}

}
