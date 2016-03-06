package com.apptogo.runalien.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class AnimationActor extends Actor{

	com.badlogic.gdx.graphics.g2d.Animation animation;
	
	float frameWidth, frameHeight;
	float scaleBy = 1, stateTime = 0;
	TextureRegion currentFrame;
	
	boolean doAnimate = true;
	
	public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames)
	{
		this(frameDuration, keyFrames, PlayMode.NORMAL);
	}
	
	public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode)
	{
		animation = new com.badlogic.gdx.graphics.g2d.Animation(frameDuration, keyFrames, playMode);
	}
	
	public void scaleFramesBy(float scale)
	{
		scaleBy = scale;
	}
	
	@Override
	public void act (float delta) {
		super.act(delta);

		if(doAnimate)
			stateTime += delta;
		
		currentFrame = animation.getKeyFrame(stateTime);
		frameWidth = currentFrame.getRegionWidth();
		frameHeight = currentFrame.getRegionHeight();
		
		setSize(frameWidth * scaleBy, frameHeight * scaleBy);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
		batch.draw(currentFrame, 
				   getX() + ((AtlasRegion)currentFrame).offsetX, 
				   getY() + ((AtlasRegion)currentFrame).offsetY, 
				   getOriginX(), 
				   getOriginY(), 
				   getWidth(),
				   getHeight(), 
				   getScaleX(), 
				   getScaleY(), 
				   getRotation());
	}
}
