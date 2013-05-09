package vivelab.yamba;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity {
	private static final String TAG = "TimelineActivity";
	
	// fields
	private Cursor cursor;
	private TimelineAdapter adapter;
	private ListView LVTimeline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		// check if preferences are set up
		if (yamba.getPreferences().getString("username", null) == null ) {
			startActivity(new Intent(getApplicationContext(), PrefsActivity.class));
			Toast.makeText(getApplicationContext(), yamba.getAppResources().getString(
					R.string.noPrefs), Toast.LENGTH_LONG).show();
		}
		
		// link views
		LVTimeline = (ListView) findViewById(R.id.listTimeline);
		
		Log.d(TAG, "onCreated");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.setupTimelineList();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		yamba.getStatusData().close();
		
		Log.d(TAG, "onDestroyed");
	}
	
	@SuppressWarnings("deprecation")
	private void setupTimelineList() {
		
		// get data from database (Android gingerbread)
		cursor = yamba.getStatusData().getStatusUpdates();
		startManagingCursor(cursor);
		
		// adapter
		adapter = new TimelineAdapter(getApplicationContext(), cursor);
		LVTimeline.setAdapter(adapter);
	}

}
