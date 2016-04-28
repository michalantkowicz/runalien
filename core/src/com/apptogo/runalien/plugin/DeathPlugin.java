package com.apptogo.runalien.plugin;

import java.util.Map;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DeathPlugin extends AbstractPlugin {

	public boolean dead;
	
	private final boolean DEBUG_IMMORTAL = false;
	
	private SoundPlugin soundHandler;

	public DeathPlugin() {
		super();
		dead = false;
	}
	
	@Override
	public void run() {		
		Map<UserData, UserData> snapshot = Main.getInstance().getGameScreen().getContactsSnapshot();
		UserData playerUserData = UserData.get(actor.getBody());
		
		if(!DEBUG_IMMORTAL && snapshot.containsKey(playerUserData) && snapshot.get(playerUserData).key.startsWith("killing")) {
			dead = true;
			String obstacleType = snapshot.get(playerUserData).type;
			
			soundHandler.stopSound("run");
			soundHandler.stopSound("scream");
			if("bell".equals(obstacleType))
				soundHandler.playSound("bell"); 
			else if("log".equals(obstacleType))
				soundHandler.playSound("die"); 
			else if("crate".equals(obstacleType))
				soundHandler.playSound("die"); 
			
			for(Fixture fixture : body.getFixtureList())
				UserData.get(fixture).ignore = true;
			
			if(snapshot.get(playerUserData).key.equals("killingTop"))
				actor.changeAnimation("dietop");
			else
				actor.changeAnimation("diebottom");
			
			Main.getInstance().getGameScreen().setEndGame(true);
			
			//TODO set up showing end screen with some delay - or rather pass info to gamescreen
		}
	}

	@Override
	public void setUpDependencies() {	
		soundHandler = actor.getPlugin(SoundPlugin.class.getSimpleName());
		
		if(body.getFixtureList().size <= 0)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
	}
}
