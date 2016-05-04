package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.level.segment.SegmentFieldDefinitions;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Sphere extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 6444715985674444198L;
	
	private final float BASE_OFFSET = 25f;
	private final int MIN_LEVEL = 0;
	private final int MAX_LEVEL = 5;
	
	private final float ROPE_WIDTH = 8.5f;

	private AtlasRegion ball;
	private Vector2 ballSize;
	
	private Body ballBody;
	private Body ropeBody;
	
	private World world;

	public Sphere(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		final float x = 10;
		final float y = 6;
		
		Body anchor = BodyBuilder.get().type(BodyType.StaticBody).addFixture("AnchorBall").box(SegmentFieldDefinitions.OBSTACLE_SIZE, SegmentFieldDefinitions.OBSTACLE_SIZE).friction(0.5f).position(x, y).sensor(true).create();
		setStaticImage("crate");
		
		ball = ResourcesManager.getInstance().getAtlasRegion("ball");
		ballSize = new Vector2(UnitConverter.toBox2dUnits(ball.getRegionWidth()), UnitConverter.toBox2dUnits(ball.getRegionHeight()));
		
		ballBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("killingTop").circle(1).friction(0.5f).position(x + ROPE_WIDTH, y).sensor(true).create();	
		ropeBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("RopeBall").box(ROPE_WIDTH, 0.1f).sensor(true).position(x + ROPE_WIDTH/2f, y).create();

		setBody(anchor);
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.localAnchorB.set(-ROPE_WIDTH / 2f, 0);
		jointDef.collideConnected = false;
		jointDef.bodyA = anchor;
		jointDef.bodyB = ropeBody;

		world.createJoint(jointDef);

		jointDef.localAnchorB.set(ROPE_WIDTH / 2f, 0);
		jointDef.collideConnected = false;
		jointDef.bodyA = ballBody;
		jointDef.bodyB = ropeBody;

		world.createJoint(jointDef);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(ball, ballBody.getPosition().x - ballSize.x/2, ballBody.getPosition().y - ballSize.y/2, getOriginX(), getOriginY(), ballSize.x, ballSize.y, getScaleX(), getScaleY(), getRotation());
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
		ballBody.setTransform(getBody().getPosition().x + ROPE_WIDTH, getBody().getPosition().y, 0);
		ropeBody.setTransform(getBody().getPosition().x + ROPE_WIDTH/2f, getBody().getPosition().y, 0);
	}


}
