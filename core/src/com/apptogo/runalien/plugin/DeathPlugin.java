package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DeathPlugin extends AbstractPlugin {

	protected boolean dead = false;

	private SoundPlugin soundHandler;
	
	@Override
	public void run() {		
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").startsWith("killing")) {
			dead = true;
			
			//TODO soundHandler.stopSounds();
			//TODO soundHandler.playSound("bang"); 
			
			for(Fixture fixture : body.getFixtureList())
				UserData.get(fixture).ignore = true;
			
			if(GameScreen.contactsSnapshot.get("player").equals("killingTop"))
				actor.changeAnimation("dietop");
			else
				actor.changeAnimation("diebottom");
			
			//TODO set up showing end screen with some delay - or rather pass info to gamescreen
		}
	}

	@Override
	public void setUpDependencies() {	
		try {
			soundHandler = actor.getPlugin(SoundPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have SoundHandler plugin attached!");
		}
		
		if(body.getFixtureList().size <= 0)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
	}
}
