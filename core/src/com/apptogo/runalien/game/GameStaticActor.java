package com.apptogo.runalien.game;

import com.apptogo.runalien.scene2d.ImageActor;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameStaticActor extends Actor {

	private Body body;
	private Vector2 bodySize;

	private ImageActor image;

	public GameStaticActor(String name) {
		super();

		image = new ImageActor(name);
		image.scaleFramesBy(1 / UnitConverter.PPM);
		
		setName(name);
		setDebug(true);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		image.act(delta);

		if (body != null) {
			image.setPosition(body.getPosition().x - bodySize.x / 2, body.getPosition().y - bodySize.y / 2);
			setPosition(body.getPosition().x - bodySize.x / 2, body.getPosition().y - bodySize.y / 2);
		}

		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		image.draw(batch, parentAlpha);
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

	public Vector2 getBodySize() {
		return bodySize;
	}

	public ImageActor getImage() {
		return image;
	}

	public void setImage(ImageActor image) {
		this.image = image;
	}

}
