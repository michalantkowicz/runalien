package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraFollowing extends AbstractPlugin {

	OrthographicCamera camera;
	
	@Override
	public void postSetActor() {
		camera = (OrthographicCamera)actor.getStage().getCamera();
		camera.position.set(0, actor.getBody().getPosition().y - GameScreen.getGroundLevel(), 0);
	}
	
	@Override
	public void run() { 
		camera.position.set(actor.getBody().getPosition().x, camera.position.y, camera.position.z);
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub
		
	}

}
