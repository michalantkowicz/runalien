package com.apptogo.runalien.scene2d;

import com.apptogo.runalien.manager.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;

public class Animation extends AnimationActor {
	public static Animation get(float frameDuration, String pattern)
	{
		return new Animation(frameDuration, ResourcesManager.getInstance().getRegions(pattern));
	}
	
	public static Animation get(float frameDuration, String pattern, PlayMode playMode)
	{
		return new Animation(frameDuration, ResourcesManager.getInstance().getRegions(pattern), playMode);
	}
	
	public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames)
	{
		super(frameDuration, keyFrames);
	}
	
	public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode)
	{
		super(frameDuration, keyFrames, playMode);
	}
	
	public Animation scaleFrames(float scale)
	{
		super.scaleFramesBy(scale);
		return this;
	}
	
	public Animation position(float x, float y)
	{
		setPosition(x, y);
		return this;
	}
	
	public Animation centerX()
	{
		setPosition(scaleBy * -animation.getKeyFrame(0).getRegionWidth()/2f, getY());
		return this;
	}
	
	public Animation centerX(float offset)
	{
		setPosition(scaleBy * -animation.getKeyFrame(0).getRegionWidth()/2f + offset, getY());
		return this;
	}
	
	public Animation centerY()
	{
		setPosition(getX(), scaleBy * -animation.getKeyFrame(0).getRegionHeight()/2f);
		return this;
	}
	
	public Animation centerY(float offset)
	{
		setPosition(getX(), scaleBy * -animation.getKeyFrame(0).getRegionHeight()/2f + offset);
		return this;
	}
	
	public Animation action(Action action)
	{
		addAction(action);
		return this;
	}
	
	public Animation stop()
	{
		doAnimate = false;
		return this;
	}
	
	public Animation start()
	{
		doAnimate = true;
		return this;
	}
}