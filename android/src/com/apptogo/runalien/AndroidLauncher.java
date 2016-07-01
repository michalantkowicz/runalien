package com.apptogo.runalien;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.PlusShare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class AndroidLauncher extends AndroidApplication implements OnConnectionFailedListener, ConnectionCallbacks, GameCallback {
	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);

	//result codes
	public static final int ACHIEVEMENTS_RESULT_CODE = 1;
	public static final int LEADERBOARDS_RESULT_CODE = 2;

	private GoogleApiClient mGoogleApiClient;
	private FrameLayout layout;
	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;

	//View for AdMob banner
	private AdView gameBannerAdView;
	private InterstitialAd mInterstitialAd;

	private static final int HIDE_BANNER = 0;
	private static final int SHOW_BANNER = 1;

	private static final int FULLSCREEN_AD_INTERVAL = 7;
	private int fullscreenAdCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;

		// Create the Google Api Client with access to the Play Games services
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Games.API).addScope(Games.SCOPE_GAMES)
				// add other APIs and scopes here as needed
				.build();

		mGoogleApiClient.connect();

		// Create the layout
		layout = new FrameLayout(this);

		// Create the libgdx View
		View gameView = initializeForView(new Main(this), config);

		// Add the libgdx view
		layout.addView(gameView);

		/*------------ ADS ------------ */
		createBanner();
		createFullscreenAd();

		// Hook it all up
		setContentView(layout);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10001) {
			//The GoogleApiClient is in an inconsistent state and must reconnect to the service to resolve the issue.
			//caused by sign off
			mGoogleApiClient.disconnect();
		}
	}

	private void createBanner() {
		// Create and setup the AdMob view
		gameBannerAdView = new AdView(this);
		gameBannerAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
		gameBannerAdView.setAdSize(AdSize.BANNER);
		gameBannerAdView.setVisibility(AdView.GONE);
		gameBannerAdView.refreshDrawableState();

		AdRequest gameBannerAdRequest = new AdRequest.Builder().addTestDevice("7712162DDB4F4C5C4A3FF71455D368A4").addTestDevice("D23AB5C0EB6156E6B120419ABB943C21").build();

		//Load ad to view
		gameBannerAdView.loadAd(gameBannerAdRequest);

		// Add the AdMob view 
		FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER | Gravity.BOTTOM);

		//Add view to layout
		layout.addView(gameBannerAdView, adParams);
	}

	private void createFullscreenAd() {
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
		requestNewFullscreenAd();

		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				requestNewFullscreenAd();
				resumeGame();
			}
		});
	}

	private void requestNewFullscreenAd() {
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("7712162DDB4F4C5C4A3FF71455D368A4").addTestDevice("D23AB5C0EB6156E6B120419ABB943C21").build();
		mInterstitialAd.loadAd(adRequest);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mGoogleApiClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		logger.debug("OnConnectionFailed, result: " + result);
		if (result.hasResolution()) {
			try {
				logger.debug("ErrorCode: " + result.getErrorCode());
				result.startResolutionForResult(this, result.getErrorCode());
			} catch (SendIntentException e) {
				logger.debug("Exception during startResultionForResult: ", e);
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		logger.debug("Connection to Google Play has been established");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		logger.debug("Connection to Google Play has been suspended");
		// Attempt to reconnect
		mGoogleApiClient.connect();
	}

	//GameCallback methods implementations
	@Override
	public void showLeaderboard() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getResources().getString(R.string.leaderboard_id)), LEADERBOARDS_RESULT_CODE);
		}
	}

	@Override
	public void showAchievements() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), ACHIEVEMENTS_RESULT_CODE);
		}
	}

	@Override
	public void shareOnGooglePlus() {
		Intent shareIntent = new PlusShare.Builder(this).setType("text/plain").setText("I just played Run Alien and it was awesome! You should try it!")
				.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.apptogo.runalien")).getIntent();

		startActivityForResult(shareIntent, 0);
	}

	@Override
	public void submitScore(int score) {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			logger.debug("Submitting score");
			Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_id), score);
			showLeaderboard();
		}
	}

	@Override
	public void setBannerVisible(boolean visible) {
		logger.debug("Setting banner visible: " + visible);
		if (visible)
			handler.sendEmptyMessage(SHOW_BANNER);
		else
			handler.sendEmptyMessage(HIDE_BANNER);
	}

	@Override
	public void showFullscreenAd() {
		fullscreenAdCounter++;

		if (fullscreenAdCounter >= FULLSCREEN_AD_INTERVAL) {
			logger.debug("Trying to show fullscreen ad");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mInterstitialAd.isLoaded()) {
						logger.debug("Showing fullscreen ad");
						mInterstitialAd.show();
						fullscreenAdCounter = 0;
					} else {
						resumeGame();
					}
				}
			});
		} else {
			resumeGame();
		}
	}

	@Override
	public void vibrate() {
		if (Gdx.app.getPreferences("SETTINGS").getBoolean("VIBRATIONS"))
			Gdx.input.vibrate(200);
	}

	@Override
	public void incrementAchievement(String achievementId) {
		incrementAchievement(achievementId, 1);
	}

	@Override
	public void incrementAchievement(String achievementId, int step) {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			Games.Achievements.increment(mGoogleApiClient, achievementId, step);
		} else {
			logger.debug("Google API is not connected");
		}
	}

	@Override
	public void unlockAchievement(String achievementId) {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			Games.Achievements.unlock(mGoogleApiClient, achievementId);
		} else {
			logger.debug("Google API is not connected");
		}
	}

	private void resumeGame() {
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Main.getInstance().setScreen(new GameScreen(Main.getInstance()));
			}
		});
	}

	//Ads thread handler
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_BANNER:
				gameBannerAdView.setVisibility(View.VISIBLE);
				break;
			case HIDE_BANNER:
				gameBannerAdView.setVisibility(View.GONE);
				break;
			}
		}
	};

}
