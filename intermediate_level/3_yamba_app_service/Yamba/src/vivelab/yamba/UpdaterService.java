package vivelab.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class UpdaterService extends Service {
	private static final String TAG = "ServiceName";

	// service fields
	public static int DELAY = 60000;	// ms
	private MainThread mainThread;
	private boolean isRunning;
	private YambaApplication yamba;

	// startup logic
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");

		this.mainThread = new MainThread();
		isRunning = false;
		
		// create application object
		yamba = (YambaApplication) getApplication();
	}

	// write long-live operation (Bounded Service)
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// write long-live operation (Unbounded Service)
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStarted");
        
		// start thread
		this.mainThread.start();
		isRunning = true;
		yamba.setUpdaterRunning(true);
        
        // We want this service to continue running until it is explicitly stopped, so return START_STICKY.
        return Service.START_STICKY;
    }

	// release resources (example: threads, listeners, connections)
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d(TAG, "onDestroyed");
    	
    	this.mainThread.interrupt();
    	this.mainThread = null;		// avoid loitering
    	isRunning = false;
    	yamba.setUpdaterRunning(false);
    }
    
    private class MainThread extends Thread {
    	
    	List<Twitter.Status> timeline;

		public MainThread() {
			super("MainThread");
		}

		@Override
		public void run() {
			UpdaterService updater = UpdaterService.this;
			while (updater.isRunning) {
				Log.d(TAG, "updater running...");
				try {
					// pull timeline
					try {
						timeline = yamba.getTwitter().getFriendsTimeline();
					} catch (TwitterException e) {
						Log.d(TAG, e.toString());
					}
					
					// print out friends' statuses
					if (!timeline.isEmpty()) {
						for (Twitter.Status status : timeline) {
							Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
						}
					}
					
					// wait DELAY ms
					Log.d(TAG, "updater ran");
					Thread.sleep(DELAY);
					
				} catch (InterruptedException e) {
					updater.isRunning = false;
					Log.e(TAG, e.toString());
				}
			}
		}
	}
}