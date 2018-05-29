package com.dk.study.storageexample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	String MSG_KEY_FNAME="com.dk.study.storageexample.F_NAME";
	String MSG_KEY_LNAME="com.dk.study.storageexample.L_NAME";
	String MSG = "com.dk.study.storageexample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onSubmit(View view){
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(
				getString(R.string.preference_file), Context.MODE_PRIVATE);
		SharedPreferences.Editor contactEditor = sharedPref.edit();
		EditText fNameView = (EditText)findViewById(R.id.editfNameText);
		EditText lNameView = (EditText)findViewById(R.id.editlNameText);
		String fName = fNameView.getText().toString();
		String lName = lNameView.getText().toString();
		contactEditor.putString(getString(R.string.fname_key), fName);
		contactEditor.putString(getString(R.string.lname_key), lName);
		contactEditor.commit();
		
	}
	
	public void readData(View view){
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
		String fName = sharedPref.getString(getString(R.string.fname_key), "First Name Not Available!");
		String lName = sharedPref.getString(getString(R.string.lname_key), "Last Name Not Available!");
		
		Log.d(MSG_KEY_FNAME, fName);
		Log.d(MSG_KEY_LNAME, lName);
	}
	public void redirectSavingFileExmpl(View view){
		Log.d(MSG,"Redirecting to Saving Files Example Page");
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
		String fName = sharedPref.getString(getString(R.string.fname_key), "First Name Not Available!");
		String lName = sharedPref.getString(getString(R.string.lname_key), "Last Name Not Available!");
		
		Intent redirectToSavingFileExmplIntent = new Intent(this, SavingFilesExampleActivity.class);
		redirectToSavingFileExmplIntent.putExtra("f_name", fName);
		redirectToSavingFileExmplIntent.putExtra("l_name", lName);
		startActivity(redirectToSavingFileExmplIntent);
	}

}
