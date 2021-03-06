package vivelab.supporting;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.TextView;

public class HomeActivity extends Activity {

    // fields
    // private static final String TAG = "HomeActivity";
    DisplayMetrics displayMetrics;
    TextView TVScreenSize;
    TextView TVScreenDensity;
    TextView TVSizeDp;

	// write startup logic
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// instantiate class-scope variables
		TVScreenSize = (TextView) findViewById(R.id.TVScreenSize);
		TVScreenDensity = (TextView) findViewById(R.id.TVScreenDensity);
		TVSizeDp = (TextView) findViewById(R.id.TVSizeDp);
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

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
		int widthPx = displayMetrics.widthPixels;
		int heightPx = displayMetrics.heightPixels;
		int densityDpi = displayMetrics.densityDpi;
		int widthDp = widthPx / Tools.getConversionFactor(densityDpi);
		int heightDp = heightPx / Tools.getConversionFactor(densityDpi);
		
		TVScreenSize.setText(widthPx + " x " + heightPx + " px");
		TVSizeDp.setText(widthDp + " x " + heightDp + " dp");
		String density = densityDpi + " dpi";
		switch (densityDpi) {
			case 120:
				density += " (ldpi)";
				break;
			case 160:
				density += " (mdpi)";
				break;
			case 240:
				density += " (hdpi)";
				break;
			case 320:
				density += " (xhdpi)";
				break;
			case 480:
				density += " (xxhdpi)";
				break;
			
			default:
				break;
		}
		TVScreenDensity.setText(density);
		
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
