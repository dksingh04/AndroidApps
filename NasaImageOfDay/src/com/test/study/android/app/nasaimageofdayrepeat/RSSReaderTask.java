package com.test.study.android.app.nasaimageofdayrepeat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSReaderTask extends AsyncTask<String, Void, IotdHandler> {
	private static final String TAG = RSSReaderTask.class.getSimpleName();
	private Activity activity;
	private ProgressDialog dialog;
	public RSSReaderTask(final Activity activity){
		this.activity = activity;
		
	}
	@Override
	protected IotdHandler doInBackground(String... urls) {
		
		try {
			Log.d("doInBackground Task", "Start Reading the Feed");
			
			//dialog.show();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder =  builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(urls[0]);
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/rss/channel/item[1]/*";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            IotdHandler iotdHandler = new IotdHandler(); 
            for (int i = 0; i < nodeList.getLength(); i++) {
               String nodeName = nodeList.item(i).getNodeName();
               if(nodeList.item(i).hasChildNodes()){
            	   if(nodeName.equals("title")){
            		   Log.d(nodeName+": ", nodeList.item(i).getFirstChild().getNodeValue());
            		   iotdHandler.setTitle(nodeList.item(i).getFirstChild().getNodeValue());
            	   }else if(nodeName.equals("description")){
            		   Log.d(nodeName+": ", nodeList.item(i).getFirstChild().getNodeValue());
            		   iotdHandler.setDescription(nodeList.item(i).getFirstChild().getNodeValue()); 
            	   }else if(nodeName.equals("pubDate")){
            		   Log.d(nodeName+": ", nodeList.item(i).getFirstChild().getNodeValue());
            		   iotdHandler.setDate(nodeList.item(i).getFirstChild().getNodeValue()); 
            	   }
               }
               if(nodeList.item(i).hasAttributes()){
            	   if(nodeList.item(i).getNodeName().equals("enclosure")){
            		   Log.d(nodeName+": ",nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
            		   iotdHandler.setUrl(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
            		   iotdHandler.setImage(getBitmap(iotdHandler.getUrl()));
            	   }
               }
            }
			return iotdHandler;
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			Log.d("IOException : ",e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e(TAG, e.toString());
			Log.d("SAXException : ",e.getMessage());
			return null;
		} catch (ParserConfigurationException e) {
			Log.e(TAG, e.toString());
			Log.d("ParserConfigurationException : ",e.getMessage());
			return null;
		} catch (XPathExpressionException e) {
			Log.d("XPathExpressionException : ",e.getMessage());
			return null;
		} 
		
	}
	@Override
	protected void onPreExecute(){
		Log.d("onPreExecute", "onPreExecute");
		if(dialog==null) {
			dialog = new ProgressDialog(activity).show(activity, "Loading", "Loading the Image of the Day");
		}
	}
	protected void onPostExecute(IotdHandler iotdHandler) {
			/*Log.d("Title", iotdHandler.getTitle());
			Log.d("Date", iotdHandler.getDate());
			Log.d("Description", iotdHandler.getDescription());
			*/
			try {
				resetDisplay(iotdHandler.getTitle(), iotdHandler.getDate(), iotdHandler.getImage(), iotdHandler.getDescription());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(dialog!=null){
				dialog.dismiss();
			}
    }
	
	private void resetDisplay(String title, String date,
			Bitmap image, String description) throws MalformedURLException, IOException{
			
			TextView imgTitleView = (TextView)activity.findViewById(R.id.imageTitle);
			imgTitleView.setText(title);
			
			TextView imgDateView = (TextView)activity.findViewById(R.id.imagedate);
			imgDateView.setText(date);
			
			final ImageView imgView = (ImageView)activity.findViewById(R.id.imageDisplay);
			
			imgView.setImageBitmap(image);
			
			TextView imgDescView = (TextView)activity.findViewById(R.id.imageDescription);
			imgDescView.setText(description);
			
			
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
}
