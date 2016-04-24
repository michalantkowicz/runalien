package com.apptogo.runalien;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements OnConnectionFailedListener, ConnectionCallbacks{
	
	private GoogleApiClient mGoogleApiClient;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		
		initialize(new Main(), config);
		
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
	public void onConnectionFailed(ConnectionResult arg0) {
		System.out.println("COS SIE ZJEBALO");
	}

	@Override
	public void onConnected(Bundle arg0) {
		System.out.println("COS SIE POLACZYLO");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		System.out.println("COS SIE SUSPENDOWALO");		
	}
}
