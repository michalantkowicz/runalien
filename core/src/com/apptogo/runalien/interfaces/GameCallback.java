package com.apptogo.runalien.interfaces;

public interface GameCallback {
	public void showLeaderboard();
	public void showAchievements();
	public void shareOnGooglePlus();
	public void submitScore(int score);
	public void setBannerVisible(boolean visible);
	public void showFullscreenAd();
	public void vibrate();
}
