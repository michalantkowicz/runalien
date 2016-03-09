package com.apptogo.runalien.plugin;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraFollowing extends AbstractPlugin {

	OrthographicCamera camera;
	
	@Override
	public void postSetActor() {
		camera = (OrthographicCamera)actor.getStage().getCamera();
	}
	
	@Override
	public void run() { 
		camera.position.set(actor.getX(), actor.getY(), camera.position.z);
	}

}
