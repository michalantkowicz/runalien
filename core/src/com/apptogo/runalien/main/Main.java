package com.apptogo.runalien.main;

import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.screen.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Main extends Game {
	public static final float SCREEN_WIDTH = 1280f, SCREEN_HEIGHT = 800f;
	
	@Override
	public void setScreen (Screen screen) {
		if(this.screen != null)
			this.screen.dispose();
		
		super.setScreen(screen);
	}
	
	@Override
	public void create() {
		ResourcesManager.create();
		//ResourcesManager.getInstance().loadResources();
		///ResourcesManager.getInstance().manager.finishLoading();
		//ResourcesManager.getInstance().loadSkin();
		//this.setScreen(new MenuScreen(this));
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		ResourcesManager.destroy();
	}
}