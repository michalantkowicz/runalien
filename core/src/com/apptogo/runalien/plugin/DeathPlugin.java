package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.manager.CustomAction;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DeathPlugin extends AbstractPlugin {

	public boolean dead = false;

	private SoundPlugin soundHandler;
	private GameScreen screen;
	
	public DeathPlugin() {
		super();
	}
	//TODO przerobic GameScreen do singletona lub zastosowac inny mechanizm - musi byc dostep do instancji GameScreen!
	public DeathPlugin(GameScreen screen) {
		this();
		this.screen = screen;
	}
	
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
			
			screen.endGame();
			
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
