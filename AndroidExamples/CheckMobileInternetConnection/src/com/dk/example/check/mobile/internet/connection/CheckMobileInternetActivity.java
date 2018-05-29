package com.dk.example.check.mobile.internet.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CheckMobileInternetActivity extends Activity {
	 MobileInternetConnectionDetector cd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_mobile_internet);
		Button buttonSts = (Button)findViewById(R.id.btn_check);
		cd = new MobileInternetConnectionDetector(getApplicationContext());
		buttonSts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isConnected;
				isConnected = cd.checkInternetConnectivity();
				if(isConnected){
					showAlertDialog(CheckMobileInternetActivity.this, "Mobile Internet", "Your Mobile has Internet", true);
				}else{
					showAlertDialog(CheckMobileInternetActivity.this, "Mobile Internet", "Your Mobile do not have Internet", true);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_mobile_internet, menu);
		return true;
	}
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
}
