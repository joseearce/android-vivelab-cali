package vivelab.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
// import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {

	// ui elements
	private EditText ETMessage;
	// private Button BtnSend;
	// private Button BtnOpenCamera;
	
	// intents
	public static final String HELLO = "vivelab.hello";
	public static final int IMAGE_CAPTURE = 1;
	
	// entry method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		// link ui elements on java
		ETMessage = (EditText) findViewById(R.id.ETMessage);
		// BtnSend = (Button) findViewById(R.id.BtnSend);
		// BtnOpenCamera = (Button) findViewById(R.id.BtnOpenCamera);
	}

	// handler for onClick events
	public void buttonHandler(View view) {
		
		// decide which button was pressed
		switch ( view.getId() ) {
			
			// send message (Explicit Intent)
			case R.id.BtnSend:	
				
				// build explicit intent
				Intent intent = new Intent(this, MessageActivity.class);
				String msg = ETMessage.getText().toString();
				intent.putExtra(HELLO, msg);
				
				// call message activity
				startActivity(intent);
				break;
			
			// open device camera
			case R.id.BtnOpenCamera:
				
				// send implicit intent
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, IMAGE_CAPTURE);
				break;
				
			default:
				break;
		}
		
	}
	
	// activity result
	protected void  onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case IMAGE_CAPTURE:
					print("A new photo was captured", this);
					break;
			}
		}
	}
	
	/** Prints a message on the screen using a Toast view */
	public static void print(String message, Context activityContext){
    	Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
