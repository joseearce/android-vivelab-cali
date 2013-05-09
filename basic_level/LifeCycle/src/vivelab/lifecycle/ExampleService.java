package vivelab.lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class ExampleService extends Service {
	
	// main service thread
	private static final String TAG = "ExampleService";
	private Hilo hilo;
	private boolean isRunning;
	public static int count;
	

	// startup logic
	@Override
	public void onCreate() {
		super.onCreate();
		this.hilo = new Hilo();
		count = 0;
		isRunning = false;
	}
	
	// write long-live operation (Unbounded Service)
	// call stopSelf() if need it
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "start...");
        
		// start thread
		this.hilo.start();
		isRunning = true;
        
        // We want this service to continue running until it is explicitly stopped, so return sticky.
        return Service.START_STICKY;
    }
	
	// write long-live operation (Bounded Service)
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// release resources (example: threads, listeners, connections)
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	this.hilo.interrupt();
    	this.hilo = null;		// avoid loitering
    	isRunning = false;
    	Log.i(TAG, "stop");
    }
    
    private class Hilo extends Thread {
		
		public Hilo() {
			super("Hilo");
		}
		
		@Override
		public void run() {
			
			// business logic
			while (isRunning) {
				try {
					Thread.sleep(1*1000);	// 1 s
					count++;
				} catch (InterruptedException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		
	}
	
}