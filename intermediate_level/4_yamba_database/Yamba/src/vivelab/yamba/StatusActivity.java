package vivelab.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, 
	TextWatcher {
	private static final String TAG = "StatusActivity";
	
    // fields
	private EditText ETStatus;
	private Button buttonUpdate;
	private TextView TVCount;
	private YambaApplication yamba;

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
		
		// create application object
		yamba = (YambaApplication) getApplication();
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
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(getApplicationContext(), PrefsActivity.class));
			break;
		case R.id.itemServiceStart:
			if ( !yamba.isUpdaterRunning() ) {
				startService(new Intent(getApplicationContext(), UpdaterService.class));
			}
			break;
		case R.id.itemStopService:
			if ( yamba.isUpdaterRunning() ) {
				stopService(new Intent(getApplicationContext(), UpdaterService.class));
			}
			break;
			
		default:
			break;
		}
		return true;
	}
	
	// async post to twitter-like server
	private class PostToTwitter extends AsyncTask<String, Integer, String> {

		private ProgressDialog progressDialog = new ProgressDialog(StatusActivity.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle(yamba.getAppResources().getString(R.string.dialogStatusTitle));
			progressDialog.setMessage(yamba.getAppResources().getString(R.string.dialogStatusMessage));
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... statuses) {
			try {
				yamba.getTwitter().updateStatus(statuses[0]);
				return yamba.getAppResources().getString(R.string.postUpdated);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return yamba.getAppResources().getString(R.string.failedPost);
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		String status = ETStatus.getText().toString();
		if( YambaApplication.isConnected(getApplicationContext()) ) {
			if (status.length() > 0) {
				new PostToTwitter().execute(status);
			} else {
				Toast.makeText(getApplicationContext(), yamba.getAppResources().getString(R.string.statusEmpty), Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), yamba.getAppResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
		}									   
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
			else            TVCount.setTextColor(Color.GREEN);
		}
		Log.d(TAG, "Status Text changed");
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
}
