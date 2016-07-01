package com.apptogo.runalien.desktop;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;

public class DesktopLauncher {

	private static final Logger logger = new Logger("com.apptogo.runalien.desktop.DesktopLauncher", Logger.DEBUG);
	public static GameCallback gameCallback = new GameCallback() {

		@Override
		public void showLeaderboard() {
			logger.debug("showLeaderboard() is available only in Android");
		}

		@Override
		public void showAchievements() {
			logger.debug("showAchievements() is available only in Android");
		}

		@Override
		public void shareOnGooglePlus() {
			logger.debug("shareOnGooglePlus() is available only in Android");
		}

		@Override
		public void submitScore(int score) {
			logger.debug("submitScore() is available only in Android");
		}
		
		@Override
		public void setBannerVisible(boolean visible) {
			logger.debug("setBannerVisible() is available only in Android");
		}

		@Override
		public void showFullscreenAd() {
			logger.debug("showFullscreenAd() is available only in Android");
			Main.getInstance().setScreen(new GameScreen(Main.getInstance()));
		}

		@Override
		public void vibrate() {
			logger.debug("vibrate() is available only in Android");
		}

		@Override
		public void incrementAchievement(String achievementId) {
			logger.debug("incrementAchievement() is available only in Android");
		}

		@Override
		public void incrementAchievement(String achievementId, int step) {
			logger.debug("incrementAchievement() is available only in Android");
		}

		@Override
		public void unlockAchievement(String achievementId) {
			logger.debug("unlockAchievement() is available only in Android");
		}

	};

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) Main.SCREEN_WIDTH / 2;
		config.height = (int) Main.SCREEN_HEIGHT / 2;

		new LwjglApplication(new Main(gameCallback), config);
	}

}
