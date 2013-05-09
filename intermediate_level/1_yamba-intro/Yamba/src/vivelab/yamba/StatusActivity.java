package vivelab.yamba;

import winterwell.jtwitter.Twitter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, 
	TextWatcher {

    // fields
    private static final String TAG = "StatusActivity";
	private EditText ETStatus;
	private Button buttonUpdate;
	private TextView TVCount;
	private Twitter jtwitter;

	// write startup logic
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		// link views
		ETStatus = (EditText) findViewById(R.id.ETStatus);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
		
		// set count
		TVCount = (TextView) findViewById(R.id.TVCount);
		TVCount.setText(Integer.toString(140));
		TVCount.setTextColor(Color.GREEN);
		
		// set listeners
		buttonUpdate.setOnClickListener(this);
		ETStatus.addTextChangedListener(this);
		
		// setup jtwitter service
		jtwitter = new Twitter("nathanes", "123456");
		jtwitter.setAPIRootUrl("http://yamba.marakana.com/api");

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
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}
	
	// async post to twitter-like server
	class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) {
			try {
				Twitter.Status status =  jtwitter.updateStatus(statuses[0]);
				return status.text;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		}
	}
	
	/* Listener handlers */

	@Override
	public void onClick(View v) {
		String status = ETStatus.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(TAG, "updateButton clicked");
	}

	@Override
	public void afterTextChanged(Editable statusText) {
		int count = 140 - statusText.length();
		if (count <=  0) {
			TVCount.setText(Integer.toString(0));
			TVCount.setTextColor(Color.RED);
		} else {
			TVCount.setText(Integer.toString(count));
			if (count < 10) TVCount.setTextColor(Color.YELLOW);
		}
		Log.d(TAG, "Status Text changed");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

}
