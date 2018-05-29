package com.test.study.android.app.nasaimageofdayrepeat;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class NasaImageOfDay extends Activity {

	private String url = "http://www.nasa.gov/rss/image_of_the_day.rss";
	private static final String TAG = RSSReaderTask.class.getSimpleName();
	private Bitmap image;
	private Handler _handler;
	RSSReaderTask rssReadTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nasa_image_of_day);
		_handler = new Handler();
		processRssFeed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nasa_image_of_day, menu);
		return true;
	}
	
	public void onRefresh(View view){
		Log.d(TAG, view.getContext().getPackageName());
		processRssFeed();
	}
	
	public void onSetWallPaper(View view){
		Log.d("onSetWallPaper method", view.getContext().getPackageName());
		Thread th = new Thread(){
			public void run(){
				WallpaperManager wallPaperManager = WallpaperManager.getInstance(NasaImageOfDay.this);
				try{
					wallPaperManager.setBitmap(rssReadTask.get().getImage());
					_handler.post( new Runnable(){
						public void run(){
							Toast.makeText(NasaImageOfDay.this, "Wallpaper set", Toast.LENGTH_SHORT).show();
						}
					}
					);
					
				}catch(Exception e){
					e.printStackTrace();
					_handler.post( new Runnable(){
						public void run(){
							Toast.makeText(NasaImageOfDay.this, "Error in Wallpaper set", Toast.LENGTH_SHORT).show();
						}
					}
					);
				}
			}
		};
		th.start();
		
	}
	
	private void processRssFeed() {
		rssReadTask = new RSSReaderTask(this);	
		rssReadTask.execute(url);
	
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
