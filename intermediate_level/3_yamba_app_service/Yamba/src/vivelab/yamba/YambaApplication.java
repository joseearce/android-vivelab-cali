package vivelab.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
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
	OnSharedPreferenceChangeListener{
	private static final String TAG = "YambaApplication";
	
	// application fields
	private Twitter jtwitter;
	private SharedPreferences prefs;
	private Resources resources;
	private boolean isUpdaterRunning;
	
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
