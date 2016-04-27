package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DeathPlugin extends AbstractPlugin {

	public boolean dead;
	
	private final boolean DEBUG_IMMORTAL = false;
	
//	private SoundPlugin soundHandler;

	public DeathPlugin() {
		super();
		dead = false;
	}
	
	@Override
	public void run() {		
		if(!DEBUG_IMMORTAL && Main.getInstance().getGameScreen().getContactsSnapshot().containsKey("player") && Main.getInstance().getGameScreen().getContactsSnapshot().get("player").startsWith("killing")) {
			dead = true;
			
			//TODO soundHandler.stopSounds();
			//TODO soundHandler.playSound("bang"); 
			
			for(Fixture fixture : body.getFixtureList())
				UserData.get(fixture).ignore = true;
			
			if(Main.getInstance().getGameScreen().getContactsSnapshot().get("player").equals("killingTop"))
				actor.changeAnimation("dietop");
			else
				actor.changeAnimation("diebottom");
			
			Main.getInstance().getGameScreen().setEndGame(true);
			
			//TODO set up showing end screen with some delay - or rather pass info to gamescreen
		}
	}

	@Override
	public void setUpDependencies() {	
//		try {
//			soundHandler = actor.getPlugin(SoundPlugin.class.getSimpleName());
//		}
//		catch(PluginException e) {
//			throw new PluginDependencyException("Actor must have SoundHandler plugin attached!");
//		}
		
		if(body.getFixtureList().size <= 0)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
	}
}
