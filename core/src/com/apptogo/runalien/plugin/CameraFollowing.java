package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraFollowing extends AbstractPlugin {

	OrthographicCamera camera;
	
	@Override
	public void postSetActor() {
		camera = (OrthographicCamera)actor.getStage().getCamera();
	}
	
	@Override
	public void run() { 
		camera.position.set(actor.getBody().getPosition().x, actor.getBody().getPosition().y - GameScreen.getGroundLevel(), camera.position.z);
	}

}
