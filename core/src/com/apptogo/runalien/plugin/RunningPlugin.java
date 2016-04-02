package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.main.Main;

public class RunningPlugin extends AbstractPlugin {
	private boolean started;
	private DeathPlugin deathPlugin;
	private final float MAX_SPEED = 25;
	
	@Override
	public void run() {
		if (deathPlugin.dead && started) {
			setStarted(false);
		}
		if (started) {
			if(body.getLinearVelocity().x >= MAX_SPEED - 1){
				body.setLinearVelocity(MAX_SPEED, body.getLinearVelocity().y);
			}
			else{
				body.setLinearVelocity(12.5f + body.getPosition().x * 0.01f , body.getLinearVelocity().y);
				System.out.println("POS: " + body.getPosition().x + " SPEED: " + body.getLinearVelocity().x);
			}
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {

		if (started) {
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
		try {
			deathPlugin = actor.getPlugin(DeathPlugin.class.getSimpleName());
		} catch (PluginException e) {
			throw new PluginDependencyException("Actor must have DeathPlugin plugin attached!");
		}
	}
}
