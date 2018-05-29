package com.dk.example.test.gcmclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterActivity extends Activity {
	private GoogleCloudMessaging gcm;
	private String regId;
	private String userId;
	private String userName;
	private Handler _handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_handler = new Handler();
		String regId = getRegistrationId();
		if (regId != null) {
			TextView textView = new TextView(this);
			//textView.setTextSize(50);
			textView.setText(R.string.already_registered_msg);
			setContentView(textView);
			// The code is for time being to test the server
			Map<String, String> params = new HashMap<String, String>();
			params.put("RegId", regId);
			params.put("UserId", userId);
			params.put("UserName", userName);
			registerToServer(AppConfig.REGISTER_SERVER_URL, params);
		} else {
			setContentView(R.layout.activity_register);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void onRegister(View view) {
		Log.d("onRegister.", "Called on Register");
		EditText userIdEditText = (EditText) findViewById(R.id.userIdText);
		EditText userNameEditText = (EditText) findViewById(R.id.userNameText);

		userId = userIdEditText.getText().toString();
		userName = userNameEditText.getText().toString();

		registerToGCMServer();

	}

	// Send Registration Id to Server
	private void registerToServer(final String endppoint, final Map<String, String> params) {
		    final URL url;
			try {
				url = new URL(endppoint);
			} catch (MalformedURLException me) {
				throw new IllegalArgumentException("invalid url: " + endppoint);
			}
		 new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... parameters) {
				
		
				StringBuilder paramBuilder = new StringBuilder();
				Iterator<Entry<String, String>> paramIterator = params.entrySet()
						.iterator();
				while (paramIterator.hasNext()) {
					Entry<String, String> param = paramIterator.next();
					paramBuilder.append(param.getKey()).append("=")
							.append(param.getValue());
					if (paramIterator.hasNext()) {
						paramBuilder.append('&');
					}
				}
		
				String body = paramBuilder.toString();
				Log.v(AppConfig.TAG, "Posting Body '" + body + "' to '" + url);
				byte[] bytes = body.getBytes();
		
				HttpURLConnection conn = null;
		
				try {
					Log.e("URL", "> " + url);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setFixedLengthStreamingMode(bytes.length);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded;charset=UTF-8");
					// post the request
					OutputStream out = conn.getOutputStream();
					out.write(bytes);
					out.close();
					// handle the response
					int status = conn.getResponseCode();
					Log.i("Status: ", ""+status);
					if (status != 200) {
						displayMessageOnScreen("Post failed with error code " + status);
						throw new IOException("Post failed with error code " + status);
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (conn != null)
						conn.disconnect();
				}
				return null;
			}
			 
		 }.execute(null,null,null);
			
				
		

	}

	private void registerToGCMServer() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String msg;
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}

					regId = gcm.register(AppConfig.GCM_SENDER_ID);

					msg = "Device registered, registration ID=" + regId;

					Log.i("GCM", msg);
					displayMessageOnScreen("Registered Successfully!!");

				} catch (IOException ex) {
					final String errorMsg = "Error :" + ex.getMessage();
					Log.i("GCM", errorMsg);
					displayMessageOnScreen(errorMsg);

				}
				return regId;

			}

			@Override
			protected void onPostExecute(String regId) {
				saveRegIdToDevice(regId);
				Map<String, String> params = new HashMap<String, String>();
				params.put("REG_ID", regId);
				params.put("UserId", userId);
				params.put("USER_NMEUserName", userName);
				registerToServer(AppConfig.REGISTER_SERVER_URL, params);
			}
		}.execute(null, null, null);
	}

	private void saveRegIdToDevice(String regId) {
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(
				getString(R.string.regidpref_file), Context.MODE_PRIVATE);
		SharedPreferences.Editor regIdEditor = sharedPref.edit();

		regIdEditor.putString(getString(R.string.regId_key), regId);
		regIdEditor.commit();
	}

	private String getRegistrationId() {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.regidpref_file), Context.MODE_PRIVATE);
		String regId = sharedPref
				.getString(getString(R.string.regId_key), null);
		return regId;
	}

	private void displayMessageOnScreen(final String msg) {
		_handler.post(new Runnable() {
			public void run() {
				Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}
