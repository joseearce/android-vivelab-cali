package vivelab.lifecycle;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	// private static final String TAG = "HomeActivity";
	private static int i = 0;
	private ArrayList<String> list;
	private TextView TVMap;
	public static String lastCallPhoneNumber;
	
	// write startup logic
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		// instantiate class-scope variables
		TVMap = (TextView) findViewById(R.id.TVMap);
		list = new ArrayList<String>();
		list.add(++i + " Created");
		lastCallPhoneNumber = null;
	}
	
	// recover user's state using meta-data stored on InstanceState
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    
	    // Restore state members from saved instance
	    // value = savedInstanceState.getInt(key);
	}
	
	// write counterpart of onStop
	@Override
    protected void onStart() {
        super.onStart();
        list.add(++i + " Started");
        
        // enable application features
    }

	// perform tasks that need to be performed only after being on stopped state
    @Override
    protected void onRestart() {
        super.onRestart();
        list.add(++i + " onRestart() called");
    }

    // write running tasks (main application state)
    @Override
    protected void onResume() {
        super.onResume();
        list.add(++i + " Resumed");
        
        // activity logic
        String msg = list.toString();
        TVMap.setText(msg);
        
        // show last call phone number if exists
        if (lastCallPhoneNumber != null) {
        	print("Last Call from: \n" + lastCallPhoneNumber, this);
        }
    }
    
    // save user's state
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        // savedInstanceState.putInt(key, value);
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    // stop consuming processes (Keep this method simple)
    @Override
    protected void onPause() {
        super.onPause();
        list.add(++i + " State: Paused");
        
        // release system resources
    }
    
    // write intensive tasks because activity is not visible
    @Override
    protected void onStop() {
        super.onStop();
        list.add(++i + " State: Stopped");
        
        // release system resources
        
        // commit unsaved changes (database for instance)
    }

    // kill long-running services (such us threads)
    // Also called when phone is rotated from portrait/landscape to landscape/portrait
    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.add(++i + " State: Destroyed");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	// handler for onClick events
	public void buttonHandler(View view) {
		
		// decide which button was pressed
		switch ( view.getId() ) {
			
			// go to web site
			case R.id.BtnGoWebsite:
				String url = "http://www.mintic.gov.co";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
				break;
			
			case R.id.BtonStartService:
				startService(new Intent(this, ExampleService.class));
				print("Starting service...", this);
				break;
			
			case R.id.BtnStopService:
				stopService(new Intent(this, ExampleService.class));
				print("Service stopped. Count: " + ExampleService.count, this);
				break;
				
			default:
				break;
		}
	}
	
	// print message
	public static void print(String message, Context activityContext){
	    Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show();
	}

}