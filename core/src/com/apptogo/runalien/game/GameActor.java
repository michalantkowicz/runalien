package com.apptogo.runalien.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.plugin.AbstractPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class GameActor extends Actor {

	private Body body;
	private Map<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	private float customOffsetX, customOffsetY;

	
	public GameActor(String name) {
		setName(name);
		setDebug(true);
	}

	LinkedList<AbstractPlugin> plugins = new LinkedList<AbstractPlugin>();

	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor
		
		setPosition(body.getPosition().x + customOffsetX + currentAnimation.getDeltaOffset().x, body.getPosition().y + customOffsetY + currentAnimation.getDeltaOffset().y);
		setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

		for (AbstractPlugin plugin : plugins)
			plugin.run();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}

	public void addPlugin(AbstractPlugin plugin) {
		plugin.setActor(this);
		plugins.add(plugin);
	}

	public void modifyCustomOffsets(float deltaX, float deltaY) {
		customOffsetX += deltaX;
		customOffsetY += deltaY;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
		
		customOffsetX = -((UserData)body.getUserData()).width/2f;
		customOffsetY = -((UserData)body.getUserData()).height/2f;
	}

	public Map<String, Animation> getAnimations() {
		return animations;
	}

	public void setAnimations(Map<String, Animation> animations) {
		animations.forEach((k, v) -> v.scaleFrames(1 / UnitConverter.PPM));
		this.animations = animations;
		calculateAverageOffset();
	}
	
	/**
	 * we calculate average offset of all 1st frames from all animations.
	 * thanks that every animation will be positioned in the same place
	 */
	private void calculateAverageOffset(){
		Vector2 averageOffset = new Vector2();
		Vector2 offsetSum = new Vector2();
		for(Animation animation : animations.values()){
			AtlasRegion atlasRegion = ((AtlasRegion)animation.getGdxAnimation().getKeyFrames()[0]);
			offsetSum.x += UnitConverter.toBox2dUnits(atlasRegion.offsetX);
			offsetSum.y += UnitConverter.toBox2dUnits(atlasRegion.offsetY);
		}
		averageOffset.x = offsetSum.x/animations.size();
		averageOffset.y = offsetSum.y/animations.size();
		animations.forEach((k, v) -> v.setDeltaOffset(averageOffset));
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(String currentAnimationName) {
		this.currentAnimation = animations.get(currentAnimationName);
		this.currentAnimation.start();
	}

	public void setImage(TextureRegion texture) {
		this.animations.put("default", new Animation(0, new Array<TextureRegion>(new TextureRegion[] { texture })));
		setCurrentAnimation("default");
	}

}
