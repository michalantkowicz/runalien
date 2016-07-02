package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.screen.GameScreen;
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
	
	private final float BASE_OFFSET = 14f;
	public static final int SHORT_MIN_LEVEL = 3;
	public static final int SHORT_MAX_LEVEL = Main.MAX_SPEED_LEVEL;
	public static final int LONG_MIN_LEVEL = 1;
	public static final int LONG_MAX_LEVEL = Main.MAX_SPEED_LEVEL;
	
	private boolean isShort;
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
	private SoundPlugin soundHandler;
	
	private boolean playSound = true;
	
	public Sphere(String name, boolean isShort) {
		super(name);
		this.isShort = isShort;
		world = Main.getInstance().getGameScreen().getWorld();
		
		if(isShort) {
			ROPE_WIDTH -= 1.2f;
		}
		
		ballPositionOffset = (new Vector2(0, -ROPE_WIDTH)).rotate(-25f);
		
		final float x = 10;
		final float y = 6.2f;
		
		anchor = BodyBuilder.get().type(BodyType.StaticBody).addFixture("AnchorBall").box(UnitConverter.toBox2dUnits(51), UnitConverter.toBox2dUnits(20)).friction(0.5f).position(x, y).sensor(true).create();
		setStaticImage("anchor");
		
		ball = ResourcesManager.getInstance().getAtlasRegion("ball");
		ballSize = new Vector2(UnitConverter.toBox2dUnits(ball.getRegionWidth()), UnitConverter.toBox2dUnits(ball.getRegionHeight()));
		
		chain = ResourcesManager.getInstance().getAtlasRegion("chain");
		
		ballBody = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("killingTop", "ball").circle(1).friction(0.5f).position(x + ballPositionOffset.x, ballPositionOffset.y).sensor(true).create();
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
		
		addPlugin(new SoundPlugin("creak"));
		soundHandler = getPlugin(SoundPlugin.class);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		ropeLength = ballBody.getPosition().cpy().add(joint.getLocalAnchorB()).sub(anchor.getPosition()).len();

		handleBallSound();
		
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
		if(isShort)
			return SHORT_MIN_LEVEL;
		else
			return LONG_MIN_LEVEL;
	}

	@Override
	public int getMaxLevel() {
		if(isShort)
			return SHORT_MAX_LEVEL;
		else
			return LONG_MAX_LEVEL;
	}

	@Override
	public void reset() {
		super.reset();
		getBody().setTransform(getBody().getPosition().x - 100, getBody().getPosition().y, getBody().getAngle());
		ballBody.setTransform(ballBody.getPosition().x - 100, ballBody.getPosition().y, ballBody.getAngle());
	}

	@Override
	public void init(int speedLevel, float arg) {
		super.init();
		getBody().setTransform(getBody().getPosition().x + 3f + (speedLevel>5 ? 5f : 0f), getBody().getPosition().y, getBody().getAngle());
		ballBody.setTransform(getBody().getPosition().x + ballPositionOffset.x, getBody().getPosition().y + ballPositionOffset.y, 0);
		ballBody.setLinearVelocity((new Vector2(-25, 0)).rotate(-25f));
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
	
	private long currentlyPlayedSoundId;
	private void handleBallSound(){
		if(ballBody.getPosition().cpy().sub(anchor.getPosition()).angle() < 280 && ballBody.getPosition().cpy().sub(anchor.getPosition()).angle() > 260 && playSound){
			currentlyPlayedSoundId = soundHandler.playSound("creak");
			playSound = false;
		}
		
		if(!playSound && Math.abs(ballBody.getLinearVelocity().x) < 1){
			playSound = true;
		}
		
		
		float yFactor = Main.getInstance().getGameScreen().getGameworldStage().getRoot().getY() + anchor.getPosition().y - 6f;	
		float distance = Main.getInstance().getGameScreen().getGameworldStage().getCamera().position.dst(anchor.getPosition().x, yFactor, 0);
		SoundPlugin.setVolume("creak", currentlyPlayedSoundId, distance > 15 ? 0 : 1 - distance/15);
		
	}
	
	@Override
	public void clear() {
		super.clear();
		soundHandler.stopAllSounds();
	}
}
