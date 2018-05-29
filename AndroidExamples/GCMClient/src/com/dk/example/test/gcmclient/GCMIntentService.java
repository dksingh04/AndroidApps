package com.dk.example.test.gcmclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    static final String DISPLAY_MESSAGE_ACTION =
            "com.dk.example.test.gcmclient.DISPLAY_MESSAGE";
    
    public static final String TAG = "GCM Demo";
    static final String EXTRA_MESSAGE = "message";
    
	public GCMIntentService() {
		super("GCM Intent Service");
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
           
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
            	String msg = extras.getString("Server Notification");
                sendNotification(msg);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadCastReceiver.completeWakefulIntent(intent);
		
	}
	
	 // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent gcmIntentView = new Intent(this, NotificationView.class);
        gcmIntentView.putExtra("Message", msg);
        /*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        		gcmIntentView, 0);*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);
        
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(gcmIntentView);
        PendingIntent resultIntent =
                stackBuilder.getPendingIntent(
                   0,
                   PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
