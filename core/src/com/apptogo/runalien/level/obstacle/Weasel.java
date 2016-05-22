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

public class Weasel extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 8123133757302816575L;
	
	private final float BASE_OFFSET = 25f;
	public static final int MIN_LEVEL = 0;
	public static final int MAX_LEVEL = 5;

	float a = 0;
	
	private AtlasRegion weasel;
		
	private Body body;
	
	private World world;

	public Weasel(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		final float x = 0;
		final float y = -2.5f;
				
		weasel = ResourcesManager.getInstance().getAtlasRegion("weasel");
				
		setScale(1/UnitConverter.PPM);
		
		setAvailableAnimations("molehill");
		queueAnimation("molehill");
		
		
		body = BodyBuilder.get().type(BodyType.StaticBody).addFixture("killingBottom").box(0.5f, 1.8f).friction(0.5f).position(x, y).sensor(true).create();	
		
		setBody(body);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(getCurrentAnimation().isFinished()) {
			batch.draw(weasel, 
					   getX() + 0.2f, 
					   getY() + Interpolation.elasticOut.apply(0, 0.4f, a), 
					   getOriginX(), 
					   getOriginY(), 
					   weasel.getRegionWidth(), 
					   weasel.getRegionHeight(), 
					   getScaleX(), getScaleY(), 
					   getRotation());
			a += 0.01f;
		}
		super.draw(batch, parentAlpha);
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
		body.setTransform(getBody().getPosition().x - 4, getBody().getPosition().y - 0.12f, 0);
	}

int poolIndex;
	
	@Override
	public void setPoolIndex(int poolIndex) {
		this.poolIndex = poolIndex;
	}

	@Override
	public int getPoolIndex() {
		return poolIndex;
	}
}
