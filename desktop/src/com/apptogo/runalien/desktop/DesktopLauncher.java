package com.apptogo.runalien.desktop;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = (int)Main.SCREEN_WIDTH/2;
		config.height = (int)Main.SCREEN_HEIGHT/2;
		
		new LwjglApplication(new Main(), config);
	}
}
