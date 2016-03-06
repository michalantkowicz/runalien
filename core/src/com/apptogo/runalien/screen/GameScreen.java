package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.scene2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class GameScreen extends BasicScreen {

	private ResourcesManager resourcesManager = ResourcesManager.getInstance();
	
	public GameScreen(Main game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	void prepare() {
		stage.addActor(Animation.get(0.03f, "run", PlayMode.LOOP).position(-0, -0));
	}

	@Override
	void step() {
		// TODO Auto-generated method stub
		
	}

}
