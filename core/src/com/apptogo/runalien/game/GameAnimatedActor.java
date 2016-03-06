package com.apptogo.runalien.game;

import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.AnimationActor;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameAnimatedActor extends Actor {

	private Body body;
	private Vector2 bodySize;
	
	private AnimationActor animation;
	
	public GameAnimatedActor(String name) {
		super();

		animation = Animation.get(0.03f, "run", PlayMode.LOOP).position(0, 0);
		animation.scaleFramesBy(1 / UnitConverter.PPM);

		setName(name);
		setDebug(true);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		animation.act(delta);

		if (body != null) {
			float xOffset = ((AtlasRegion)animation.getCurrentFrame()).originalWidth/2;
			float yOffset = ((AtlasRegion)animation.getCurrentFrame()).originalHeight/2;
			animation.setPosition(getX() - UnitConverter.toBox2dUnits(xOffset), getY() - UnitConverter.toBox2dUnits(yOffset));
			setPosition(body.getPosition().x - bodySize.x / 2, body.getPosition().y - bodySize.y / 2);
		}

		setWidth(animation.getWidth());
		setHeight(animation.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		animation.draw(batch, parentAlpha);
	}

	/**
	 * @param params
	 *            in box2d units
	 */
	public void setTransform(float x, float y, float angle) {
		super.setPosition(x, y);

		if (body != null)
			body.setTransform(x, y, angle);
	}

	public void setGraphicOffset(Vector2 offset) {
		animation.setCustomOffset(offset);
	}

	public void createBoxBody(BodyType bodyType, Vector2 size) {
		this.bodySize = new Vector2(size.x * 2, size.y * 2);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.x, size.y);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		body = GameScreen.getWorld().createBody(bodyDef);
		body.createFixture(fixtureDef);

		body.setUserData(getName());
	}

	// ------ GETTERS/SETTERS ------//
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public AnimationActor getAnimation() {
		return animation;
	}

	public void setAnimation(AnimationActor animation) {
		this.animation = animation;
	}

	public Vector2 getBodySize() {
		return bodySize;
	}

}
