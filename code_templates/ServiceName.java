package package.name;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class ServiceName extends Service {
	private static final String TAG = "ServiceName";
	
	// service fields
	public static int DELAY = 60000;	// ms
	private MainThread mainThread;
	private boolean isRunning;

	// startup logic
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
		
		this.mainThread = new MainThread();
		isRunning = false;
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
    }
    
    private class MainThread extends Thread {

		public MainThread() {
			super("MainThread");
		}

		@Override
		public void run() {
			ServiceName service = ServiceName.this;
			while (service.isRunning) {
				Log.d(TAG, "service running...");
				try {
					// TODO: long-live process logic
					
					Log.d(TAG, "service ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					service.isRunning = false;
					Log.e(TAG, e.toString());
				}
			}
		}
	}
}