package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.manager.CustomAction;
import com.apptogo.runalien.manager.CustomActionManager;

public class RunningPlugin extends AbstractPlugin {

	private boolean started;
	private DeathPlugin deathPlugin;
	private float runningSpeed = 10;
	private CustomAction speedingAction;

	public RunningPlugin() {
		super();
		speedingAction = new CustomAction(3f, 15) {

			@Override
			public void perform() {
				runningSpeed++;
			}
		};
	}

	@Override
	public void run() {
		if (deathPlugin.dead && started) {
			setStarted(false);
		}
		if (started) {
			body.setLinearVelocity(runningSpeed, body.getLinearVelocity().y);
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {

		if (started) {
			actor.changeAnimation("run").setFrameDuration(0.017f);
			CustomActionManager.getInstance().registerAction(speedingAction);
		} else {
			if(!deathPlugin.dead)
				actor.changeAnimation("idle");
			CustomActionManager.getInstance().unregisterAction(speedingAction);
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

	public float getRunningSpeed() {
		return runningSpeed;
	}

}
