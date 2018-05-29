package com.dk.example.check.mobile.internet.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MobileInternetConnectionDetector {
	private Context context;

	public MobileInternetConnectionDetector(Context context) {
		this.context = context;
	}

	public boolean checkInternetConnectivity() {
		ConnectivityManager connectivity = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity!=null){
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(info!=null){
				if(info.isConnected())
					return true;
			}
		}

		return false;
	}
}
