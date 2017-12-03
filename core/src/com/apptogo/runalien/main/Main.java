package com.apptogo.runalien.main;

import com.apptogo.runalien.app42.Callback;
import com.apptogo.runalien.app42.CloudHandler;
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
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import jdk.nashorn.internal.codegen.CompilerConstants;

public class Main extends Game {
    // 20x40 in box2d units
    public static final float SCREEN_WIDTH = 1280f, SCREEN_HEIGHT = 800f;
    public final static float GROUND_LEVEL = -3.5f;
	public static final int DAYTIME_CHANGE_INTERVAL = 120; //seconds
    public static final int MAX_SPEED_LEVEL = 14;
	public static final int SPEEDUP_INTERVAL = 100;
	public static final short GROUND_BITS = 2;
	public static final short PLAYER_BITS = 4;
    
    public static GameCallback gameCallback;

    public static CloudHandler cloudHandler;

    private Screen screenToSet;

    public static Main getInstance() {
    	return (Main)Gdx.app.getApplicationListener();
    }
    
    public Main(GameCallback gameCallback) {
    	super();
    	Main.gameCallback = gameCallback;

    	//TODO remove this temp section
        cloudHandler = new CloudHandler("60e7bc7bbade5df33eaf9f5f7a430e481c6a3d490fdbc1fa7f8aa0400908e0be", "aac11a07b0b6aea604fe66e5cdb2ba79407b3176f27a993815288578ff30aa15");
        cloudHandler.register("theusername", "password", () -> {});
    	//end of section
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
		Gdx.app.setLogLevel(Application.LOG_ERROR);

		//set handle back button
        Gdx.input.setCatchBackKey(true);
        
        ResourcesManager.create();
        CustomActionManager.create();
//        ResourcesManager.getInstance().loadResources();
//        ResourcesManager.getInstance().manager.finishLoading();
//        ResourcesManager.getInstance().loadSkin();
//        this.setScreen(new GameScreen(this));
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
