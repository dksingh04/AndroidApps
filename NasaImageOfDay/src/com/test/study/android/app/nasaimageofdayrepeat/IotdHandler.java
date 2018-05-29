package com.test.study.android.app.nasaimageofdayrepeat;

/**
 * IotdHandler
 * This handler will collect the Image Of The Day
 * 
 *  @author Geroen Joris - http://www.headfirstandroid.com/
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class IotdHandler extends DefaultHandler {
	private static final String TAG =  IotdHandler.class.getSimpleName();

	private boolean inTitle = false;
	private boolean inDescription = false;
	private boolean inItem = false;
	private boolean inUrl = false;
	private boolean inDate = false;
	private Bitmap image = null;

	private String url = null;
	private String title = null;
	private String description = null;
	private String date = null;

	private IotdHandlerListener listener;
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if (localName.equals("url")) {
			inUrl = true;
		} else {
			inUrl = false;
		}
		if (localName.startsWith("item")) {
			inItem = true;
		} else {
			if (inItem) {
				if (localName.equals("title")) {
					inTitle = true;
				} else {
					inTitle = false;
				}

				if (localName.equals("description")) {
					inDescription = true;
				} else {
					inDescription = false;
				}

				if (localName.equals("pubDate")) {
					inDate = true;
				} else {
					inDate = false;
				}
				if (localName.equals("enclosure")) {
					inUrl =true;
					url = attributes.getValue("url");
				}
			}
		}

	}

	public void characters(char ch[], int start, int length) {
		String chars = (new String(ch).substring(start, start + length));
		if (inUrl) {
			image = getBitmap(url);
		}
		if (inTitle) {
		//	title.append(chars);
		}

		if (inDescription) {
			//description.append(chars);
		}

		if (inDate && date == null) {
			// Example: Tue, 21 Dec 2010 00:00:00 EST
			String rawDate = chars;
			try {
				SimpleDateFormat parseFormat = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss");
				Date sourceDate = parseFormat.parse(rawDate);

				SimpleDateFormat outputFormat = new SimpleDateFormat(
						"EEE, dd MMM yyyy");
				date = outputFormat.format(sourceDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public void processFeed(String rssUrl) throws MalformedURLException {
			
			
			final URL url = new URL(rssUrl);
			
			Thread thread = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {
			        	parseRssFeed(url);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			thread.start();
			
	}
		
	private void parseRssFeed(URL url) {
	try{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
	//	ErrorProcessor errors = new ErrorProcessor(); 
		xr.setContentHandler(this);
	//	xr.setErrorHandler(errors);
		Log.d("parseRssFeed : ",url.getHost()+" : "+url.toString());
		InputStream stream = url.openStream();
		Log.d("Available Stream ", ""+stream.available());
		xr.parse(new InputSource(stream));

	} catch (IOException e) {
		Log.e(TAG, e.toString());
		Log.d("IOException : ",e.getMessage());
	} catch (SAXException e) {
		Log.e(TAG, e.toString());
		Log.d("SAXException : ",e.getMessage());
	} catch (ParserConfigurationException e) {
		Log.e(TAG, e.toString());
		Log.d("ParserConfigurationException : ",e.getMessage());
	}
	}

	public void endElement(String uri, String localName, String qName) {
		if (url != null && title != null && description != null && date != null) {
			if (listener != null) {
				listener.iotdParsed(url, title.toString(),
						description.toString(), date);
				listener = null;
			}
		}
	}

	private Bitmap getBitmap(String url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			return bitmap;
		} catch (IOException ioe) {
			return null;
		}
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		this.title= title;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}
	public IotdHandlerListener getListener() {
		return listener;
	}

	public void setListener(IotdHandlerListener listener) {
		this.listener = listener;
	}

	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
	}

}
