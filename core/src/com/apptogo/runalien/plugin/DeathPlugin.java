package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.screen.GameScreen;

public class DeathPlugin extends AbstractPlugin {

	protected boolean dead = false;

	private SoundHandler soundHandler;
	
	@Override
	public void run() {		
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("killingTop")) {
			dead = true;
			actor.changeAnimation("dietop");
		}
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("killingBottom")) {
			dead = true;
			actor.changeAnimation("diebottom");
		}
		
		if(dead)
		{
			//TO IMPLEMENT
			//soundHandler.stopSounds();
			//soundHandler.playSound("bang");
		}
	}

	@Override
	public void setUpDependencies() {	
		try {
			soundHandler = actor.getPlugin(SoundHandler.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have SoundHandler plugin attached!");
		}
		
		if(body.getFixtureList().size <= 0)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
	}
}
