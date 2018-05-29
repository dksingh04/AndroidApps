package com.test.study.android.app.nasaimageofdayrepeat;

import android.graphics.Bitmap;

public class RSSFeed {
	private Bitmap image = null;

	private String url = null;
	private StringBuffer title = new StringBuffer();
	private StringBuffer description = new StringBuffer();
	private String date = null;
	
	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title.toString();
	}

	public String getDescription() {
		return description.toString();
	}

	public String getDate() {
		return date;
	}
	public Bitmap getImage() {
		return image;
	}
}

