package com.shx.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	/**
	 * 查网
	 */
	public static boolean checkNetworkConnection(Context context) {
       
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

		if (activeInfo == null || (!activeInfo.isConnected())) {
			return false;
		} else {
			return true;
		}
      
	}
}
