package com.apptogo.runalien.desktop;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;

public class DesktopLauncher {

	private static final Logger logger = new Logger("com.apptogo.runalien.desktop.DesktopLauncher", Logger.DEBUG);
	public static GameCallback gameCallback = new GameCallback() {

		@Override
		public void showLeaderboard() {
			logger.debug("Available only in Android");
		}

		@Override
		public void showAchievements() {
			logger.debug("Available only in Android");
		}

		@Override
		public void shareOnGooglePlus() {
			logger.debug("Available only in Android");
		}

		@Override
		public void setBannerVisible(boolean visible) {
			logger.debug("Available only in Android");
		}
	};

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) Main.SCREEN_WIDTH / 2;
		config.height = (int) Main.SCREEN_HEIGHT / 2;

		new LwjglApplication(new Main(gameCallback), config);
	}

}
