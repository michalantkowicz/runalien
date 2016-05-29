package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CutBottom extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 6444715985674444198L;
	
	private final float BASE_OFFSET = 25f;
	public static final int MIN_LEVEL = 5;
	public static final int MAX_LEVEL = 10;

	Body body;
	
	private World world;

	public CutBottom(String name) {
		super(name);
		world = Main.getInstance().getGameScreen().getWorld();
		
		final float x = 0;
		final float y = Main.GROUND_LEVEL + 113/64f;
		
		body = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("killingBottom").
				loop(new float[]{0, -113/64f, 38/64f, -78/64f, 38/64f, 113/64f, -38/64f, 113/64f, -38/64f, -78/64f}).
				friction(0.5f).position(x, y).maskBits(Main.GROUND_BITS).create();
		
		MassData md = new MassData();
		md.I = 1;
		body.setMassData(md);
				
		setBody(body);
		setStaticImage("bottomcut");
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
		getBody().setTransform(getBody().getPosition().x - 1000,  getBody().getPosition().y, 0);
	}

	@Override
	public void init() {
		super.init();
		getBody().setTransform(getBody().getPosition().x,  Main.GROUND_LEVEL + 113/64f, MathUtils.degRad * 0.2f);
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
