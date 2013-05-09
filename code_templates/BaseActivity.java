package package.name;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	private static final String TAG = "BaseActivity";

	// fields
	ApplicationObject appObject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create application object
		appObject = (ApplicationObject) getApplication();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item:
				startActivity(new Intent(getApplicationContext(), ActivityName.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
		toggleItem.setTitle(R.string.string);
		toggleItem.setIcon(android.R.drawable.img);
		return true;
	}
	
}
