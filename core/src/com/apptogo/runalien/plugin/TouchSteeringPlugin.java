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
				//if(Gdx.input.justTouched() && Gdx.input.getX() >  CENTER)
				if(Main.getInstance().getGameScreen().RIGHT)
					jump();
				//if(Gdx.input.justTouched() && Gdx.input.getX() <= CENTER)
				else if(Main.getInstance().getGameScreen().LEFT)
					chargeDown();
			}
			//if(!deathPlugin.dead && Gdx.input.justTouched()){
			else if(!deathPlugin.dead && (Main.getInstance().getGameScreen().LEFT || Main.getInstance().getGameScreen().RIGHT)) {
				if(!running.isStarted())
					startRunning();
			}
			
			Main.getInstance().getGameScreen().LEFT = false;
			Main.getInstance().getGameScreen().RIGHT = false;
		}
	}
}
