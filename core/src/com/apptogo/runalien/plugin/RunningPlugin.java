package com.apptogo.runalien.plugin;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.utils.Logger;

public class RunningPlugin extends AbstractPlugin {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);
	
	//set if you want to debug one level without speeding up
	private final int DEBUG_LEVEL = -1;
	
	//speed which alien starts with
	private final int BASIC_SPEED = 12;
	
	//interval in box2d meters after which run alien speeds up 
	private final int SPEED_EVERY_X_METERS = Main.SPEEDUP_INTERVAL;
	
	//max speed level
	private final float MAX_SPEED_LEVEL = Main.MAX_SPEED_LEVEL;
	
	//curent speed level (not the same what velocity)
	private int speedLevel = 0;
	
	//position when alien will speed up
	private int nextSpeedUp = 50;

	private boolean started;
	private DeathPlugin deathPlugin;
	
	@Override
	public void run() {
		if (deathPlugin.dead && started) {
			setStarted(false);
		}
		if (started) {
			
			//fix on one level if set
			if(DEBUG_LEVEL >= 0){
				body.setLinearVelocity(BASIC_SPEED + DEBUG_LEVEL , body.getLinearVelocity().y);
				speedLevel = DEBUG_LEVEL;
			}
			else{
				body.setLinearVelocity(BASIC_SPEED + speedLevel , body.getLinearVelocity().y);
				
				//speed every x meters
				if(speedLevel <= MAX_SPEED_LEVEL && body.getPosition().x > nextSpeedUp){
					nextSpeedUp += SPEED_EVERY_X_METERS;
					speedLevel++;
					logger.debug("speeding up. Current speed level: " + speedLevel);
				}
			}
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {

		if (started) {
			if(DEBUG_LEVEL >= 0)
				logger.debug("Starting with testing level: " + DEBUG_LEVEL);
			actor.changeAnimation("run").setFrameDuration(0.017f);
			Main.getInstance().getGameScreen().removeTutorialButton();
		} else {
			if(!deathPlugin.dead)
				actor.changeAnimation("idle");
		}

		this.started = started;
	}

	@Override
	public void setUpDependencies() {
		
			deathPlugin = actor.getPlugin(DeathPlugin.class);
	}

	public int getSpeedLevel() {
		return speedLevel;
	}
}
