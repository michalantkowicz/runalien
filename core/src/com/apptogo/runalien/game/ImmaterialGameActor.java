package com.apptogo.runalien.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.apptogo.runalien.procedures.AbstractProcedure;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class ImmaterialGameActor extends AbstractActor {
	private Map<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	private float customOffsetX, customOffsetY;

	
	public ImmaterialGameActor(String name) {
		super(name);
	}

	LinkedList<AbstractProcedure> procedures = new LinkedList<AbstractProcedure>();

	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor
		
		setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

		for (AbstractProcedure procedure : procedures)
			procedure.run();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}

	public void addProcedure(AbstractProcedure procedure) {
		procedure.setActor(this);
		procedures.add(procedure);
	}

	public void modifyCustomOffsets(float deltaX, float deltaY) {
		customOffsetX += deltaX;
		customOffsetY += deltaY;
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
}
