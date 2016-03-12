package com.apptogo.runalien.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.apptogo.runalien.exception.AnimationException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.plugin.AbstractPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class GameActor extends AbstractActor {

	private Body body;
	private Map<String, Animation> availableAnimations = new HashMap<String, Animation>();
	private Queue<Animation> animationQueue = new LinkedList<Animation>();
	private Animation currentAnimation;
	private float customOffsetX, customOffsetY;

	public GameActor(String name) {
		super(name);
	}

	Map<String, AbstractPlugin> plugins = new HashMap<String, AbstractPlugin>();

	@Override
	public void act(float delta) {
		super.act(delta);

		setCurrentAnimation();

		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor

		setPosition(body.getPosition().x + customOffsetX + currentAnimation.getDeltaOffset().x, body.getPosition().y + customOffsetY + currentAnimation.getDeltaOffset().y);
		setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

		plugins.forEach((k, v) -> v.run());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}

	public void addPlugin(AbstractPlugin plugin) {
		plugin.setActor(this);
		plugins.put(plugin.getClass().getSimpleName(), plugin);
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

		customOffsetX = -((UserData) body.getUserData()).width / 2f;
		customOffsetY = -((UserData) body.getUserData()).height / 2f;
	}
	
	/**
	 * @param plugin name. Always getSimpleName() of plugin class
	 * @return plugin
	 */
	public <T extends AbstractPlugin> T getPlugin(String name){
		@SuppressWarnings("unchecked")
		T plugin = (T) plugins.get(name);
		if(plugin == null)
			throw new PluginException("Actor: '" + getName() + "' doesn't have plugin: '" + name);
		return plugin;
	}

	/**-------- ANIMATIONS -------- **/

	public Map<String, Animation> getAvailableAnimations() {
		return availableAnimations;
	}

	/**
	 * queue animation to be displayed after current one finishes
	 * @throws AnimationException if animation is not in availableAnimation map
	 */
	public void queueAnimation(String animationName) {
		Animation animation = availableAnimations.get(animationName);
		if (animation == null) {
			throw new AnimationException("Animation: '" + animationName + "' is not available for GameActor: '" + getName() + "' Possible choices are: " + availableAnimations.keySet());
		}

		animationQueue.add(animation);
	}

	/**
	 * queue animation to be displayed after current one finishes
	 * @param count queue multiple times
	 * @throws AnimationException if animation is not in availableAnimation map
	 */
	public void queueAnimation(String animationName, int count) {
		for (int i = 0; i < count; i++) {
			queueAnimation(animationName);
		}
	}

	/**
	 * defines all possible animations for this actor
	 */
	public void setAvailableAnimations(String... animationNames) {
		this.availableAnimations = Animation.getAnimations(animationNames);
		availableAnimations.forEach((k, v) -> v.scaleFrames(1 / UnitConverter.PPM));
		calculateAverageOffset();
	}

	/**
	 * adds another animation which is defined before. 
	 * Mostly used for custom cases.
	 * @throws AnimationException if name is not provided.
	 */
	public void addAvailableAnimation(Animation animation) {
		if (animation.getName() == null || animation.getName().isEmpty())
			throw new AnimationException("Animation name is not provided");
		if (availableAnimations.containsKey(animation.getName()))
			throw new AnimationException("Animation with name: '" + animation.getName() + "' already exists");

		this.availableAnimations.put(animation.getName(), animation);
		animation.scaleFrames(1 / UnitConverter.PPM);
		calculateAverageOffset();
	}

	/**
	 * immediately changes animation. Be careful, any previously queued animation will be removed 
	 */
	public void changeAnimation(String animationName) {
		Animation animation = availableAnimations.get(animationName);
		if (animation == null) {
			throw new AnimationException("Animation: '" + animationName + "' is not available for GameActor: '" + getName() + "' Possible choices are: " + availableAnimations.keySet());
		}
		animationQueue.clear();
		animationQueue.add(animation);
		currentAnimation.setFinished(true);
	}
	
	/**
	 * handles animationQueue and sets currentAnimation
	 */
	private void setCurrentAnimation() {
		//we should never allow situation when there's no current animation and queue is empty
		//invisible actors can be handled by immaterialActor
		if (currentAnimation == null && animationQueue.isEmpty())
			throw new AnimationException("Animation queue is empty. Add an animation");

		//when there's no current animation or something is queued
		if (currentAnimation == null || (animationQueue.size() > 0 && currentAnimation.isFinished())) {
			currentAnimation = animationQueue.poll();
			currentAnimation.setFinished(true);
			currentAnimation.start();
		}
	}

	/**
	 * we calculate average offset of all 1st frames from all animations.
	 * thanks that every animation will be positioned in the same place
	 */
	private void calculateAverageOffset() {
		Vector2 averageOffset = new Vector2();
		Vector2 offsetSum = new Vector2();
		for (Animation animation : availableAnimations.values()) {
			AtlasRegion atlasRegion = ((AtlasRegion) animation.getGdxAnimation().getKeyFrames()[0]);
			offsetSum.x += UnitConverter.toBox2dUnits(atlasRegion.offsetX);
			offsetSum.y += UnitConverter.toBox2dUnits(atlasRegion.offsetY);
		}
		averageOffset.x = offsetSum.x / availableAnimations.size();
		averageOffset.y = offsetSum.y / availableAnimations.size();
		availableAnimations.forEach((k, v) -> v.setDeltaOffset(new Vector2(averageOffset)));
	}
}
