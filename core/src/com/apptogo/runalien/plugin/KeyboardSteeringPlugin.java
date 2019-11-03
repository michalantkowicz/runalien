package com.apptogo.runalien.plugin;

import com.apptogo.runalien.event.GameEventService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardSteeringPlugin extends SteeringPlugin {
    public KeyboardSteeringPlugin(GameEventService eventService) {
        super(eventService);
    }

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
