package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;

public class RunningPlugin extends AbstractPlugin {

	private boolean started;
	private DeathPlugin deathPlugin;
	
	@Override
	public void run() { 
		if(deathPlugin.dead) {
			started = false;
		}
		else if(started){
			if (body.getLinearVelocity().x < 10)
				body.setLinearVelocity(10, 0);

			body.setLinearVelocity((body.getLinearVelocity().x < 24) ? body.getLinearVelocity().x + 0.005f : 24, body.getLinearVelocity().y);
			
			//animation and sound speed
			//TODO funkcja kwadratowa. Musimy zrobi mechanizm zwiekszajacy szybkosc czasowo i wtedy bedzie dzialac jak
			//TODO obliczymy wartosc funkcji. Narazie jak zmienia sie co klatke to animacja sie buguje.
//			actor.getAvailableAnimations().get("run").setFrameDuration(-0.002f * body.getLinearVelocity().x + 0.07f);
		}
		else if(!started){
			body.setLinearVelocity(0, 0);
		}
			
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		if(started){
			actor.changeAnimation("run");
			actor.getAvailableAnimations().get("run").setFrameDuration(0.017f);
		}
		else{
			actor.changeAnimation("idle");
		}
		this.started = started;
	}

	@Override
	public void setUpDependencies() {
		try {
			deathPlugin = actor.getPlugin(DeathPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have DeathPlugin plugin attached!");
		}		
	}

}
