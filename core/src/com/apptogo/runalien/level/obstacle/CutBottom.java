package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CutBottom extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 6444715985674444198L;
	
	private final float BASE_OFFSET = 15f;
	public static final int MIN_LEVEL = 3;
	public static final int MAX_LEVEL = Main.MAX_SPEED_LEVEL;

	Body body;
	
	private World world;
	private SoundPlugin soundHandler;
	
	public CutBottom(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		final float x = 0;
		final float y = Main.GROUND_LEVEL + 113/64f;
		
		body = BodyBuilder.get().type(BodyType.DynamicBody)
				.addFixture("killingBottom")
				.loop(new float[]{0, -113/64f, 38/64f, -78/64f, 38/64f, 113/64f, -38/64f, 113/64f, -38/64f, -78/64f})
				.friction(0.5f).position(x, y)
				.maskBits(Main.GROUND_BITS)
				.addFixture("killingBottom", "cutBottom")
				.loop(new float[]{0, -113/64f, 38/64f, -78/64f, 38/64f, 113/64f, -38/64f, 113/64f, -38/64f, -78/64f})
				.friction(0.5f).position(x, y)
				.sensor(true)
				.create();
		
		MassData md = new MassData();
		md.I = 1;
		body.setMassData(md);
				
		setBody(body);
		setStaticImage("bottomcut");
		
		
		addPlugin(new SoundPlugin("fallingTree"));
		soundHandler = getPlugin(SoundPlugin.class);
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		
		setOrigin(Align.center);
		setRotation(body.getAngle() * MathUtils.radDeg);
		
		currentAnimation.setOrigin(Align.center);
		currentAnimation.setRotation(body.getAngle() * MathUtils.radDeg);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
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
		getBody().setTransform(getBody().getPosition().x - 100, getBody().getPosition().y, getBody().getAngle());
	}

	@Override
	public void init(int speedLevel, float angle) {
		super.init();
		getBody().setTransform(getBody().getPosition().x + (angle>0 ? 3:0),  Main.GROUND_LEVEL + 113/64f, MathUtils.degRad * angle);
		soundHandler.playSound("fallingTree");
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
