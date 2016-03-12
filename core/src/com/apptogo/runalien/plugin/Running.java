package com.apptogo.runalien.plugin;

public class Running extends AbstractPlugin {

	private boolean started;
	
	@Override
	public void run() { 
		if(started){
			if (body.getLinearVelocity().x < 5)
				body.setLinearVelocity(5, 0);

			body.setLinearVelocity((body.getLinearVelocity().x < 24) ? body.getLinearVelocity().x + 0.01f : 24, body.getLinearVelocity().y);
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		actor.changeAnimation("run");
		this.started = started;
	}

}
