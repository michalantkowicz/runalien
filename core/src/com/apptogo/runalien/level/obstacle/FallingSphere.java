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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FallingSphere extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 6444715985674444198L;
	static int id = 0; int iid = 0;
	private final float BASE_OFFSET = 25f;
	public static final int MIN_LEVEL = 0;
	public static final int MAX_LEVEL = 5;
	
	private final float ROPE_WIDTH = 8.5f;
	
	private final float MAX_ROPE_LENGTH = 6.6f;
	private final int FALL_INTERVAL = 20;
	private final float STEP = 2.2f;
	
	private boolean doFall = false;
	private int interval = 0;	

	private AtlasRegion ball;
	private AtlasRegion chain;
	private Vector2 ballSize;
	
	private Body anchor, ballBody;
	
	private RopeJoint joint;
	private float ropeLength, startU2Length;
	
	private World world;

	public FallingSphere(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		iid = ++id;
		final float x = 10;
		final float y = 6;
		
		anchor = BodyBuilder.get().type(BodyType.StaticBody).addFixture("AnchorBall").box(SegmentFieldDefinitions.OBSTACLE_SIZE, SegmentFieldDefinitions.OBSTACLE_SIZE).friction(0.5f).position(x, y).sensor(true).create();
		setStaticImage("crate");
		
		ball = ResourcesManager.getInstance().getAtlasRegion("fallingSphere");
		ballSize = new Vector2(UnitConverter.toBox2dUnits(ball.getRegionWidth()), UnitConverter.toBox2dUnits(ball.getRegionHeight()));
		
		chain = ResourcesManager.getInstance().getAtlasRegion("chain");
		
		ballBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("killingTop").circle(0.7f).friction(0.5f).position(x + ROPE_WIDTH, y).sensor(true).create();	
		ballBody.setTransform(ballBody.getPosition(), MathUtils.degRad * 30f);
		ballBody.setAngularDamping(5f);
		
		setBody(anchor);    	
    	    	
    	RopeJointDef jointDef = new RopeJointDef();
    	jointDef.bodyA = anchor;
    	jointDef.bodyB = ballBody;
    	    	
    	jointDef.localAnchorA.set(0, 0);    	
    	jointDef.localAnchorB.set(0.5f, 0);    	
    	
    	jointDef.maxLength = 2.2f;
    	    	
    	joint = (RopeJoint)world.createJoint(jointDef);
    	
    	startU2Length = chain.getU2() - chain.getU();
	}
	
	@Override 
	public void act(float delta) {
		super.act(delta);
    	
    	ropeLength = ballBody.getPosition().cpy().add(joint.getLocalAnchorB()).sub(anchor.getPosition()).len();
    	
    	if(!doFall && ropeLength >= joint.getMaxLength() && ropeLength < MAX_ROPE_LENGTH)
    		doFall = true;
    	    	
    	if(doFall && interval > FALL_INTERVAL) {
    		joint.setMaxLength(joint.getMaxLength() + STEP);
    		doFall = false;
    		interval = 0;
    	}
    	else
    		interval++;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//Backuping U2 - we are operating on the original region and want it to be reusable
		float u2Backup = chain.getU2();
		chain.setU2(chain.getU() + startU2Length * ((64f * ropeLength) / 1024f));
		batch.draw(chain, anchor.getPosition().x - chain.getRegionHeight()/64f/2f, anchor.getPosition().y, getOriginX(), getOriginY(), ropeLength, chain.getRegionHeight() / 64f , getScaleX(), getScaleY(), ballBody.getPosition().cpy().sub(anchor.getPosition()).angle());
		chain.setU2(u2Backup);
				
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
		ballBody.setTransform(getBody().getPosition().x, getBody().getPosition().y - 0.5f, 0);
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
