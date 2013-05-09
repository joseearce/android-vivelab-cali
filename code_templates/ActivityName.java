package package.name;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivityName extends Activity {

    // fields
    private static final String TAG = "ActivityName";

	// write startup logic
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		// instantiate class-scope variables
		// Type variable = (Type) findViewById(R.id._id);
		// Type variable = ...;
        
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // For the main activity, make sure the app icon in the action bar
            // does not behave as a button
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(false);
        } 
		
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
        
        // enable application features
    }

	// perform tasks that need to be performed only after being on stopped state
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // write running tasks (main application state)
    @Override
    protected void onResume() {
        super.onResume();
        
        // activity logic
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
        
        // release system resources
    }
    
    // write intensive tasks because activity is not visible
    @Override
    protected void onStop() {
        super.onStop();
        
        // release system resources
        
        // commit unsaved changes (database for instance)
    }

    // kill long-running services (such us threads)
    // Also called when phone is rotated from portrait/landscape to landscape/portrait
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}