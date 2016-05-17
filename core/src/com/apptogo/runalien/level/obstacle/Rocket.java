package com.apptogo.runalien.level.obstacle;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Rocket extends GameActor implements Spawnable, Poolable {
	private static final long serialVersionUID = 8123133757302816575L;
	
	private float BASE_OFFSET = 25f;
	private final int MIN_LEVEL = 0;
	private final int MAX_LEVEL = 5;

	private final float ROCKET_SPEED = -15;
	
	private AtlasRegion alert;
		
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
		
		alert = new AtlasRegion(ResourcesManager.getInstance().getAtlasRegion("alert"));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		float rightEdge = Main.getInstance().getGameScreen().getGameworldStage().getCamera().position.x + UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH/2f);
		
		if(getX() > rightEdge) {
			batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
			batch.draw(alert, rightEdge - 2f, getY(), getOriginX(), getOriginY(), alert.getRotatedPackedWidth()/64f, alert.getRegionHeight()/64f, 1, 1, getRotation());
			batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, 1);
		}
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
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
	public void init(int speedLevel) {
		super.init();
		System.out.println("ROCKET!");
		float DELAY = (-ROCKET_SPEED + speedLevel + 12);
		
		body.setTransform(getBody().getPosition().x + DELAY, getBody().getPosition().y, 0);
		body.setLinearVelocity(ROCKET_SPEED, 0);
		
		this.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.2f), Actions.alpha(0, 0.2f), Actions.alpha(1, 0.2f), Actions.alpha(0, 0.2f), Actions.alpha(1, 0.2f)));
		
		BASE_OFFSET = 25 + DELAY;
	}
}
