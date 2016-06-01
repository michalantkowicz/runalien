package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
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
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Sphere extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 6444715985674444198L;
	
	private float ROPE_WIDTH = 8.7f;
	
	private final float BASE_OFFSET = 25f;
	public static final int MIN_LEVEL = 3;
	public static final int MAX_LEVEL = 7;

	private AtlasRegion ball;
	private AtlasRegion chain;
	private Vector2 ballSize;
	private Vector2 ballPositionOffset;
	
	private RopeJoint joint;
	private float ropeLength, startU2Length;
	
	Body anchor;
	private Body ballBody;
	//private Body ropeBody;
	
	private World world;

	public Sphere(String name, boolean shortRope) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		if(shortRope) {
			ROPE_WIDTH -= 1;
		}
		
		ballPositionOffset = (new Vector2(0, -ROPE_WIDTH)).rotate(7.5f);
		
		final float x = 10;
		final float y = 6.2f;
		
		anchor = BodyBuilder.get().type(BodyType.StaticBody).addFixture("AnchorBall").box(UnitConverter.toBox2dUnits(51), UnitConverter.toBox2dUnits(20)).friction(0.5f).position(x, y).sensor(true).create();
		setStaticImage("anchor");
		
		ball = ResourcesManager.getInstance().getAtlasRegion("ball");
		ballSize = new Vector2(UnitConverter.toBox2dUnits(ball.getRegionWidth()), UnitConverter.toBox2dUnits(ball.getRegionHeight()));
		
		chain = ResourcesManager.getInstance().getAtlasRegion("chain");
		
		ballBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("killingTop").circle(1).friction(0.5f).position(x + ballPositionOffset.x, ballPositionOffset.y).sensor(true).create();
		//ropeBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("RopeBall").box(ROPE_WIDTH, 0.1f).sensor(true).position(x + ROPE_WIDTH/2f, y).create();
		
		setBody(anchor);
		
		RopeJointDef jointDef = new RopeJointDef();
		
		jointDef.bodyA = anchor;
    	jointDef.bodyB = ballBody;
    	    	
    	jointDef.localAnchorA.set(0, 0);    	
    	jointDef.localAnchorB.set(0, 0);    	
    	
    	jointDef.maxLength = ROPE_WIDTH;
		
		joint = (RopeJoint) world.createJoint(jointDef);
		
		startU2Length = chain.getU2() - chain.getU();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		ropeLength = ballBody.getPosition().cpy().add(joint.getLocalAnchorB()).sub(anchor.getPosition()).len();
		
//		float rightEdge = Main.getInstance().getGameScreen().getGameworldStage().getCamera().position.x + UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH/2f);
//		
//		if(!ballBody.isActive() && ballBody.getPosition().x <= rightEdge + 4f) {
//			ballBody.setActive(true);
//		}
		//ballBody.setLinearDamping(0.3f);
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
		getBody().setTransform(getBody().getPosition().x - 1000,  getBody().getPosition().y, 0);
		ballBody.setTransform(getBody().getPosition().x + ballPositionOffset.x, getBody().getPosition().y + ballPositionOffset.y, 0);
	}

	@Override
	public void init() {
		super.init();
		ballBody.setTransform(getBody().getPosition().x + ballPositionOffset.x, getBody().getPosition().y + ballPositionOffset.y, 0);
		ballBody.setLinearVelocity(35, 0);
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
