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
		
		float vx = body.getLinearVelocity().x;
		float vy = body.getLinearVelocity().y;
		
		if(started){
			body.setLinearVelocity( (vx < 10) ? 10 : ((vx < 24) ? vx + 0.005f : 24), vy );
						
			//animation and sound speed
			//TODO funkcja kwadratowa. Musimy zrobi mechanizm zwiekszajacy szybkosc czasowo i wtedy bedzie dzialac jak
			//TODO obliczymy wartosc funkcji. Narazie jak zmienia sie co klatke to animacja sie buguje.
			//actor.getAvailableAnimations().get("run").setFrameDuration(-0.002f * body.getLinearVelocity().x + 0.07f);
		}
		else{
			body.setLinearVelocity( (vx < 0.5f) ? 0 : 0.9f * vx, (vy > 0) ? 0 : vy);
			//TODO test czy 0.9 to wystarczajacy wspolczynnik tlumienia!
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		if(!deathPlugin.dead)
		{
			if(started)
				actor.changeAnimation("run").setFrameDuration(0.017f);
			else
				actor.changeAnimation("idle");
			
			this.started = started;
		}
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
