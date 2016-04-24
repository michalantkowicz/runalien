package com.apptogo.runalien;

import com.apptogo.runalien.interfaces.GameCallback;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements OnConnectionFailedListener, ConnectionCallbacks, GameCallback{
	
	private GoogleApiClient mGoogleApiClient;
	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		
		initialize(new Main(this), config);
		
	    // Create the Google Api Client with access to the Play Games services
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	            .addConnectionCallbacks(this)
	            .addOnConnectionFailedListener(this)
	            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
	            // add other APIs and scopes here as needed
	            .build();
		
		mGoogleApiClient.connect();
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
		System.out.println("SHOW LEADERBOARD");
	}
	
	@Override
	public void showAchievements() {
		System.out.println("SHOW ACHIEVMENTS");
	}
}
