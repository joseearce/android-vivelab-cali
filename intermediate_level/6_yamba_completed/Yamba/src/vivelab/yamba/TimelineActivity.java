package vivelab.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity {
	private static final String TAG = "TimelineActivity";
	
	// fields
	private Cursor cursor;
	private TimelineAdapter adapter;
	private ListView LVTimeline;
	private TimelineReceiver receiver;
	private IntentFilter filter;
	static final String SEND_TIMELINE_NOTIFICATIONS = 
			"vivelab.yamba.SEND_TIMELINE_NOTIFICATIONS";
	
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
		
		// receiver info
		receiver = new TimelineReceiver();
		filter = new IntentFilter("vivelab.yamba.NEW_STATUS");
		
		Log.d(TAG, "onCreated");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.setupTimelineList();
		super.registerReceiver(receiver, filter, SEND_TIMELINE_NOTIFICATIONS, null);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
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
	
	// NewStatus Receiver Class
	class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			cursor = yamba.getStatusData().getStatusUpdates();
			adapter.changeCursor(cursor);	// change to update
			adapter.notifyDataSetChanged();
			Log.d(TAG, "onReceived");
		}
		
	}

}
