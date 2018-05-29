package com.example.androidlayout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AndroidLayoutMainActivity extends Activity {
	public static String TEXT_MESSAGE="com.example.androidlayout.TEXT_MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_layout_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android_layout_main, menu);
		return true;
	}
	
	public void sendMessage(View view){
		Log.d("com.example.androidlayout.AndroidLayoutMainActivity", "Send Message Clicked");
		Intent displayMessageIntent = new Intent(this, DispalyMessageActivity.class);
		EditText sendEditText = (EditText) findViewById(R.id.edit_message);
		String message = sendEditText.getText().toString();
		displayMessageIntent.putExtra(TEXT_MESSAGE, message);
		startActivity(displayMessageIntent);
	}

}
