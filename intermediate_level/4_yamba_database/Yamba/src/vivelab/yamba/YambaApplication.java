package vivelab.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Oracle's Documents - Java Synchronized Methods
 * 
 * If an object is visible to more than one thread, all reads or writes to that 
 * object's variables are done through synchronized methods. (An important 
 * exception: final fields, which cannot be modified after the object is constructed, 
 * can be safely read through non-synchronized methods, once the object is constructed) 
 * */
public class YambaApplication extends Application implements 
	OnSharedPreferenceChangeListener {
	private static final String TAG = "YambaApplication";
	
	// application fields
	private Twitter jtwitter;
	private SharedPreferences prefs;
	private Resources resources;
	private boolean isUpdaterRunning;
	private StatusData statusData;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
		
		// setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		// setup resources
		resources = getResources();
		
		// updater setup
		isUpdaterRunning = false;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminated");
	}
	
	public synchronized Twitter getTwitter() {
		if (jtwitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");
			
			this.jtwitter = new Twitter(username, password);
			this.jtwitter.setAPIRootUrl(apiRoot);
		}
		return this.jtwitter;
	}
	
	public synchronized StatusData getStatusData() {
		if (statusData == null) {
			return new StatusData(getApplicationContext());
		}
		return this.statusData;
	}
	
	public synchronized Resources getAppResources() {
		return this.resources;
	}

	
	public synchronized boolean isUpdaterRunning() {
		return this.isUpdaterRunning;
	}

	public synchronized void setUpdaterRunning(boolean isUpdaterRunning) {
		this.isUpdaterRunning = isUpdaterRunning;
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		jtwitter = null;
	}
	
	// gets status updates from cloud and counts new statuses
	public synchronized int fetchStatusUpdates() {
		Log.d(TAG, "Fetching status updates");
		Twitter jtwitter = this.getTwitter();
		if (jtwitter == null) {
			Log.d(TAG, "Twitter connection not initialized");
			return 0;
		}
		
		// get friends' statuses
		try {
			List<Twitter.Status> statuses = jtwitter.getFriendsTimeline();
			long latestStatusTimeStamp = this.getStatusData()
					.getLatestStatusCreatedAtTime();
			int count = 0;
			ContentValues values = new ContentValues();
			for (Twitter.Status status : statuses) {
				// counts new statuses
				long createdAt = status.getCreatedAt().getTime();
				if (latestStatusTimeStamp < createdAt) count++;
				
				// insert values
				values.put(StatusData.C_ID, status.getId());
				values.put(StatusData.C_USER, status.getUser().getName());
				values.put(StatusData.C_TEXT, status.getText());
				values.put(StatusData.C_CREATED_AT, createdAt);
				this.getStatusData().insertOrIgnore(values);
				
			}
			Log.d(TAG, count > 0 ? "got" + count + "new statuses" : 
				"No new status updates");
			return count;
		} catch (RuntimeException e) {
			Log.e(TAG, e.toString());
			return 0;
		}
	}
	
	// helper tools
	public static boolean isConnected(Context context) {
	    ConnectivityManager connMgr = (ConnectivityManager) 
	    		context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
	    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}

}
