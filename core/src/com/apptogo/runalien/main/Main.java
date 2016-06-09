package com.apptogo.runalien.main;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.screen.BasicScreen;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.screen.SplashScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Main extends Game {
    // 20x40 in box2d units
    public static final float SCREEN_WIDTH = 1280f, SCREEN_HEIGHT = 800f;
    public final static float GROUND_LEVEL = -3.5f;
	public static final int DAYTIME_CHANGE_INTERVAL = 120; //seconds
    public static final int MAX_SPEED_LEVEL = 14;
	public static final int SPEEDUP_INTERVAL = 100;
	public static final short GROUND_BITS = 2;
    
    public static GameCallback gameCallback;
    
    private Screen screenToSet;

    public static Main getInstance() {
    	return (Main)Gdx.app.getApplicationListener();
    }
    
    public Main(GameCallback gameCallback) {
    	super();
    	Main.gameCallback = gameCallback;
    }
    
    public GameScreen getGameScreen() {
    	return (GameScreen)getScreen();
    }
    
    @Override
    public void setScreen(Screen screen) {
        if (this.screen != null && this.screen instanceof BasicScreen) {
        	((BasicScreen)this.screen).fadeOut(screen);
    	}
        else
        	doSetScreen(screen);
    }
    
    public void doSetScreen(Screen screen) {
        if (this.screen != null) {
        	this.screen.dispose();
    	}
        super.setScreen(screen);
    }

    @Override
    public void create() {
    	//use this to define log level. It overrides local settings
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//set handle back button
        Gdx.input.setCatchBackKey(true);
        
        ResourcesManager.create();
        CustomActionManager.create();
        //ResourcesManager.getInstance().loadResources();
        //ResourcesManager.getInstance().manager.finishLoading();
        //ResourcesManager.getInstance().loadSkin();
        //this.setScreen(new GameScreen(this));
        this.setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        ResourcesManager.destroy();
        CustomActionManager.destroy();
    }
}
