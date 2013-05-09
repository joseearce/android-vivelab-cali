package vivelab.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver{
	private static final String TAG = "NetworkReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		
		boolean isNetworkDown = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		
		// start and stop service
		if (isNetworkDown) {
			Log.d(TAG, "onReceive: NOT CONNECTED, stopping service...");
			context.stopService(new Intent(context, UpdaterService.class));
		} else {
			Log.d(TAG, "onReceive: CONNECTED, starting service...");
			context.startService(new Intent(context, UpdaterService.class));
		}
	}

}
