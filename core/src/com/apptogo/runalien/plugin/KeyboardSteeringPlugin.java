package com.apptogo.runalien.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardSteeringPlugin extends SteeringPlugin {
	@Override
	public void run() {
		super.run();
				
		if(running.isStarted()){			
			if(Gdx.input.isKeyJustPressed(Keys.L))
				jump();
			if(Gdx.input.isKeyJustPressed(Keys.A))
				chargeDown();
		}
		if(!deathPlugin.dead && Gdx.input.isKeyJustPressed(Keys.SPACE)){
			if(!running.isStarted())
				startRunning();
			else{
				stopRunning();
			}
		}
	}
}
