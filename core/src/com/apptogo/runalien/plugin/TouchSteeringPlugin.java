package com.apptogo.runalien.plugin;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.Gdx;

public class TouchSteeringPlugin extends SteeringPlugin {
	private float CENTER = Gdx.app.getGraphics().getWidth()/2f;
	
	@Override
	public void run() {
		super.run();
		
		if(Gdx.input.getInputProcessor() != null) {
			if(running.isStarted()){			
				if(Gdx.input.justTouched() && Gdx.input.getX() >  CENTER)
					jump();
				if(Gdx.input.justTouched() && Gdx.input.getX() <= CENTER)
					chargeDown();
			}
			else if(!running.isStarted() && !deathPlugin.dead ){
				if(Gdx.input.justTouched() && !Main.getInstance().getGameScreen().tutorialButton.isPressed()) {
					float Ygrek = Gdx.input.getY();
					
					if(Ygrek > 50)
						startRunning();
				}
			}
		}
	}
}
