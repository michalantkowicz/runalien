package com.apptogo.runalien.plugin;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraFollowingPlugin extends AbstractPlugin {

	OrthographicCamera camera;
	
	@Override
	public void postSetActor() {
		camera = (OrthographicCamera)actor.getStage().getCamera();
		camera.position.set(0, actor.getBody().getPosition().y - Main.GROUND_LEVEL, 0);
	}
	
	@Override
	public void run() { 
		camera.position.set(actor.getBody().getPosition().x + 5f, camera.position.y, camera.position.z);
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub
		
	}

}
