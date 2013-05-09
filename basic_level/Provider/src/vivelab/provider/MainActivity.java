package vivelab.provider;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

    // fields
	private ListView LVContacts;

	// write startup logic
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// instantiate class-scope variables
		LVContacts = (ListView) findViewById(R.id.LVContacts);
		
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
        
        // display contacts names
        String[] mProjection = new String[]
        {
        	ContactsContract.Contacts._ID,
        	ContactsContract.Contacts.DISPLAY_NAME
        };
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String mSortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
        
        Cursor mCursor = getContentResolver().query(
    	    ContactsContract.Contacts.CONTENT_URI,
    	    mProjection,
    	    mSelectionClause,
    	    mSelectionArgs,
    	    mSortOrder
        );
        
        // analyze cursor
        if (mCursor == null) {
        	// Error or No Connection
        } else if (mCursor.getCount() < 1) {
        	// no contacts
        } else {
        	// contacts on ListView
        	ArrayList<String> list = new ArrayList<String>();
            while (mCursor.moveToNext()) {
    			list.add(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
            }
            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), list);
            LVContacts.setAdapter(adapter);
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
