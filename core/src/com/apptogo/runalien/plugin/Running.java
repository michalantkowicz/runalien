package com.apptogo.runalien.plugin;

import com.badlogic.gdx.math.Vector2;

public class Running extends AbstractPlugin {

	@Override
	public void run() { 
		body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
	}

}
