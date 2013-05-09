package vivelab.yamba;

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
	static final String NEW_STATUS_INTENT = "vivelab.yamba.NEW_STATUS";
	static final String NEW_STATUSES_COUNT = "vivelab.yamba.NEW_STATUS_COUNT";

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
        
		// start thread
		if (!isRunning) {
			this.mainThread.start();
			isRunning = true;
			yamba.setUpdaterRunning(true);
			Log.d(TAG, "onStarted");
		}
        
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
    	
    	static final String RECEIVE_TIMELINE_NOTIFICATIONS = 
    			"vivelab.yamba.RECEIVE_TIMELINE_NOTIFICATIONS";
    	
		public MainThread() {
			super("MainThread");
		}

		@Override
		public void run() {
			UpdaterService updater = UpdaterService.this;
			while (updater.isRunning) {
				Log.d(TAG, "updater running...");
				try {
					// fetch messages if connected
					if(YambaApplication.isConnected(getApplicationContext())) {
						int newUpdates = yamba.fetchStatusUpdates();
						if (newUpdates > 0) {
							Log.d(TAG, "We have new statuses");
							
							// send custom broadcast for time-line receiver
							// detect event, create Intent and send a broadcast
							Intent intent = new Intent(NEW_STATUS_INTENT);
							intent.putExtra(NEW_STATUSES_COUNT, newUpdates);
							updater.sendBroadcast(intent, RECEIVE_TIMELINE_NOTIFICATIONS);
						}
					} else {
						Log.e(TAG, "Not connected");
					}
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updater.isRunning = false;
					Log.e(TAG, e.toString());
				}
			}
		}
	}
}