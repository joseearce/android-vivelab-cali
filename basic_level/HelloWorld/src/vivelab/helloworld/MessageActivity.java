package vivelab.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MessageActivity extends Activity {

	// ui elements
	private TextView TVMsgReceived;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		
		// link ui in java
		TVMsgReceived = (TextView) findViewById(R.id.TVMsgRcvd);
		
		// handle main intent
		Intent intent = getIntent();
		String msgRvd = intent.getStringExtra(HomeActivity.HELLO);
		
		// display rcvd message on the ui
		TVMsgReceived.setText(msgRvd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
