package com.apptogo.runalien;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.PlusShare;
import com.google.example.games.basegameutils.BaseGameUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class AndroidLauncher extends AndroidApplication implements OnConnectionFailedListener, ConnectionCallbacks, GameCallback{
	
	//result codes
	public static final int ACHIEVEMENTS_RESULT_CODE = 1;
	public static final int LEADERBOARDS_RESULT_CODE = 2;
	
	private static final String LEADERBOARD_ID = "CgkIpZ2MjMkXEAIQAA";
	
	private GoogleApiClient mGoogleApiClient;
	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;

	//View for AdMob banner
	private AdView gameBannerAdView;
	
	private static final int HIDE_BANNER = 0;
	private static final int SHOW_BANNER = 1;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		
//		initialize(new Main(this), config);
		
	    // Create the Google Api Client with access to the Play Games services
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	            .addConnectionCallbacks(this)
	            .addOnConnectionFailedListener(this)
	            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
	            // add other APIs and scopes here as needed
	            .build();
		
		mGoogleApiClient.connect();
		
		
		/*------------ ADS ------------ */
		// Create the layout
        FrameLayout layout = new FrameLayout(this);

        // Create the libgdx View
        View gameView = initializeForView(new Main(this), config);

        // Create and setup the AdMob view
        gameBannerAdView = new AdView(this);
        gameBannerAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        gameBannerAdView.setAdSize(AdSize.BANNER);        
        gameBannerAdView.setVisibility(AdView.GONE);
        gameBannerAdView.refreshDrawableState();
        
	    AdRequest gameBannerAdRequest = new AdRequest.Builder().build();
	    
	    //Load ad to view
	    gameBannerAdView.loadAd(gameBannerAdRequest);
        
        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view 
        FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER | Gravity.BOTTOM);
        
	    //Add view to layout
        layout.addView(gameBannerAdView, adParams);

        // Hook it all up
        setContentView(layout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mGoogleApiClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		System.out.println("COS SIE ZJEBALO");
		if (mResolvingConnectionFailure) {
	        // already resolving
	        return;
	    }

	    // if the sign-in button was clicked or if auto sign-in is enabled,
	    // launch the sign-in flow
	    if (mSignInClicked || mAutoStartSignInFlow) {
	        mAutoStartSignInFlow = false;
	        mSignInClicked = false;
	        mResolvingConnectionFailure = true;
	        
	        // Attempt to resolve the connection failure using BaseGameUtils.
	        // The R.string.signin_other_error value should reference a generic
	        // error string in your strings.xml file, such as "There was
	        // an issue with sign-in, please try again later."
	        if (!BaseGameUtils.resolveConnectionFailure(this,
	                mGoogleApiClient, connectionResult,
	                RC_SIGN_IN, getResources().getString(R.string.signin_other_error))) {
	            mResolvingConnectionFailure = false;
	        }
	    }

	    // Put code here to display the sign-in button
	}

	@Override
	public void onConnected(Bundle arg0) {
		System.out.println("COS SIE POLACZYLO");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		System.out.println("COS SIE SUSPENDOWALO");		
		// Attempt to reconnect
	    mGoogleApiClient.connect();
	}
	
	//GameCallback methods implementations
	@Override
	public void showLeaderboard() {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, LEADERBOARD_ID), LEADERBOARDS_RESULT_CODE);
	}
	
	@Override
	public void showAchievements() {
		startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), ACHIEVEMENTS_RESULT_CODE);
	}


	@Override
	public void shareOnGooglePlus() {
	      Intent shareIntent = new PlusShare.Builder(this)
	              .setType("text/plain")
	              .setText("I just played Run Alien and it was awesome! You should try it!")
	              .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.apptogo.runalien"))
	              .getIntent();

	          startActivityForResult(shareIntent, 0);
	}
	
	@Override
	public void setBannerVisible(boolean visible) {
		if(visible)
			handler.sendEmptyMessage(SHOW_BANNER);
		else
			handler.sendEmptyMessage(HIDE_BANNER);
	}
	
	//Ads thread handler
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
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
