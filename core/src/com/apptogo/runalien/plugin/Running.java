package com.apptogo.runalien.plugin;

import com.badlogic.gdx.math.Vector2;

public class Running extends AbstractPlugin {

	@Override
	public void run() { 
		if(body.getLinearVelocity().x < 5)
			body.setLinearVelocity(5, 0);
		
		body.setLinearVelocity((body.getLinearVelocity().x < 24) ? body.getLinearVelocity().x + 0.01f : 24, body.getLinearVelocity().y);
	}

}
