package com.apptogo.runalien.game;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.procedures.AbstractProcedure;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ImmaterialGameActor extends AbstractActor {

	Map<String, AbstractProcedure> procedures = new HashMap<String, AbstractProcedure>();
	
	public ImmaterialGameActor(String name) {
		super(name);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);

		setCurrentAnimation();

		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor

		if(getWidth() == 0 && getHeight() == 0)
			setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

		procedures.forEach((k, v) -> v.run());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}

	public void addProcedure(AbstractProcedure procedure) {
		procedure.setActor(this);
		procedures.put(procedure.getClass().getSimpleName(), procedure);
	}
	
	/**
	 * @param plugin name. Always getSimpleName() of plugin class
	 * @return plugin
	 * @throws PluginException 
	 */
	public <T extends AbstractProcedure> T getProcedure(String name) throws PluginException{
		@SuppressWarnings("unchecked")
		T plugin = (T) procedures.get(name);
		if(plugin == null)
			throw new PluginException("Actor: '" + getName() + "' doesn't have procedure: '" + name);
		return plugin;
	}
}
