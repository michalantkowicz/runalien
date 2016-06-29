package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Weasel extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 8123133757302816575L;
	
	private final float BASE_OFFSET = 10f;
	public static final int MIN_LEVEL = 2;
	public static final int MAX_LEVEL = 9;

	float animationProgress = 0;
	
	private AtlasRegion weasel;
		
	private Body body;
	private SoundPlugin soundHandler;
	
	public Weasel(String name) {
		super(name);
		
		final float x = 0;
		final float y = -2.5f;
				
		weasel = ResourcesManager.getInstance().getAtlasRegion("weasel");
				
		setScale(1/UnitConverter.PPM);
		
		setAvailableAnimations("molehill");
		
		body = BodyBuilder.get().type(BodyType.StaticBody).addFixture("killingBottom", "weasel").box(0.5f, 1.8f).friction(0.5f).position(x, y).sensor(true).create();	
		
		setBody(body);
		
		addPlugin(new SoundPlugin("weasel"));
		soundHandler = getPlugin(SoundPlugin.class);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(getCurrentAnimation().isFinished()) {
			batch.draw(weasel, 
					   getX() + 0.2f, 
					   getY() + Interpolation.elasticOut.apply(0, 0.4f, animationProgress), 
					   getOriginX(), 
					   getOriginY(), 
					   weasel.getRegionWidth(), 
					   weasel.getRegionHeight(), 
					   getScaleX(), getScaleY(), 
					   getRotation());
			animationProgress += 0.0075f;
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
		getBody().setTransform(getBody().getPosition().x - 100, getBody().getPosition().y, getBody().getAngle());		
	}
	
	@Override
	public void init(int speedLevel, float arg) {
		super.init();
		soundHandler.playSound("weasel");
		body.setTransform(getBody().getPosition().x - (speedLevel < 5 ? 1 : 0), Main.GROUND_LEVEL + 0.88f, 0);
		queueAnimation("molehill");
		animationProgress = 0;

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
