package package.name;

import android.app.Application;
import android.util.Log;

public class ApplicationName extends Application {
	private static final String TAG = "ApplicationName";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminated");
	}

}