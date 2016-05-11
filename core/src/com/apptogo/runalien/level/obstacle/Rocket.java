package com.apptogo.runalien.level.obstacle;

import org.omg.CORBA.portable.StreamableValue;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.level.segment.SegmentFieldDefinitions;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Rocket extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 8123133757302816575L;
	
	private final float BASE_OFFSET = 25f;
	private final int MIN_LEVEL = 0;
	private final int MAX_LEVEL = 5;

	private Body body;
	
	private World world;

	int counter = 0;
	
	public Rocket(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		final float x = 0;
		final float y = -2.9f;
		
		addAvailableAnimation(Animation.get(0.02f, "rocket", PlayMode.LOOP));
		queueAnimation("rocket");
		
		body = BodyBuilder.get().type(BodyType.KinematicBody).addFixture("killingTop").box(1f, 0.5f).friction(0.5f).position(x, y).sensor(true).create();	
		
		setBody(body);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		counter++;
		if(counter % 10 == 0){
			body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
		}
	}

	@Override
	public float getBaseOffset() {
		return BASE_OFFSET;
	}

	@Override
	public int getMinLevel() {
		return MIN_LEVEL;
	}

	@Override
	public int getMaxLevel() {
		return MAX_LEVEL;
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	public void init() {
		super.init();
		body.setTransform(getBody().getPosition().x, getBody().getPosition().y, 0);
		body.setLinearVelocity(-15, 1);
	}


}
