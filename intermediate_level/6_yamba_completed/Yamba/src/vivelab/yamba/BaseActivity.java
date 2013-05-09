package vivelab.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	//private static final String TAG = "BaseActivity";
	
	YambaApplication yamba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create application object
		yamba = (YambaApplication) getApplication();
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
			case R.id.itemStatus:
				startActivity(new Intent(getApplicationContext(), StatusActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				break;
			case R.id.itemTimeline:
				startActivity(new Intent(getApplicationContext(), TimelineActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				break;
			case R.id.itemPrefs:
				startActivity(new Intent(getApplicationContext(), PrefsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				break;
			case R.id.itemToggleService:
				if ( yamba.isUpdaterRunning() ) {
					stopService(new Intent(getApplicationContext(), UpdaterService.class));
				} else {
					startService(new Intent(getApplicationContext(), UpdaterService.class));
				}
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
		if (yamba.isUpdaterRunning()) {
			toggleItem.setTitle(R.string.stopService);
			toggleItem.setIcon(android.R.drawable.ic_media_pause);
		} else {
			toggleItem.setTitle(R.string.startService);
			toggleItem.setIcon(android.R.drawable.ic_media_play);
		}
		return true;
	}
	
}
