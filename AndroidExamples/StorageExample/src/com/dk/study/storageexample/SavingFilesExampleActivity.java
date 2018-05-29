package com.dk.study.storageexample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SavingFilesExampleActivity extends Activity {
	
	private String MSG = "com.dk.study.storageexample.SavingFilesExampleActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saving_files_example);
		Intent intentData = getIntent();
		String firstName = intentData.getStringExtra("f_name");
		String lastName = intentData.getStringExtra("l_name");
		File file = new File(getFilesDir(), "contactdata.txt");
		TextView textView = (TextView)findViewById(R.id.savemsgtext);
		
		FileOutputStream outputStream;
		try{
			
			if(!file.exists()){
				file.createNewFile();
			}
			outputStream = openFileOutput("contactdata.txt", Context.MODE_PRIVATE);
			OutputStreamWriter outStreamWriter = new OutputStreamWriter(outputStream);
			outStreamWriter.write(firstName+" "+lastName);
			//outStreamWriter.write(lastName);
			outputStream.close();
			textView.setText(R.string.save_message);
			
		}catch(Exception e){
			e.printStackTrace();
			textView.setText(R.string.save_message_error);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saving_files_example, menu);
		return true;
	}
	
	public void readDataFromFile(View view){
		FileInputStream inputStream;
		//File file = new File(getFilesDir(), "contactdata.txt");
		Log.d(MSG, "readDataFromFile");
		try{
			
			/*if(!file.exists()){
				Log.d(MSG, "File does no Exist");
				throw new FileNotFoundException();
			}*/
			inputStream = openFileInput("contactdata.txt");
			Log.d(MSG, "readDataFromFile1");
			StringBuilder stringBuilder = new StringBuilder();
			if(inputStream!=null){
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				Log.d(MSG, "readDataFromFile2");
				char [] buffer = new char[500];
				inputStreamReader.read(buffer);
				//receiveString = bufferReader.readLine();
				stringBuilder.append(buffer[2]);
				/*while((receiveString = bufferReader.readLine())!=null){
					stringBuilder.append(receiveString);
					Log.d(MSG, receiveString);
					Log.d(MSG, "readDataFromFile2 in while loop");
				}*/
				inputStream.close();
				Log.d(MSG, "readDataFromFile3");
			}

			//inputStream.close();
			TextView textView = (TextView)findViewById(R.id.savemsgtext);
			Log.d(MSG, stringBuilder.toString());
			textView.setText(stringBuilder.toString());
			
		}catch(FileNotFoundException fne){
			Log.d(MSG, "File Not Found!!!");
			fne.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			//textView.setText(R.string.save_message_error);
		}
	}

}
