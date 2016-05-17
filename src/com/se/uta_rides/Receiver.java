package com.se.uta_rides;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.se.uta_rides.RideConfirmActivity;
import com.se.uta_rides.SplashActivity;

public class Receiver extends ParsePushBroadcastReceiver {
	
	
	/*public void onPushReceive(Context context, Intent intent) {
			final String TAG = "MyNotificationsReceiver";
		
		    try {
		      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

		      String notificationText = json.getString("alert");
		      System.out.println("notificationText = "+notificationText);
		    } catch (JSONException e) {
		      Log.d(TAG, "JSONException: " + e.getMessage());
		    }
	}*/
	
	
	@Override
	public void onPushOpen(Context context, Intent intent) {
		final String TAG = "MyNotificationsReceiver";
		String alert = "";
		String emailid_stu="";
		String time = "";
		String date = "";
		String loc = "";
		String concatenatedString = "";
		String numberOfSeatsRequired = "";
		String latitudeSearch="";
		String longitudeSearch="";
	    try {
	      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
	      //JSONObject json1 = new JSONObject(intent.getExtras().getString("com.parse.Target"));
	      alert = json.getString("alert");
	      emailid_stu = json.getString("emailid_stu");
	      concatenatedString=emailid_stu.substring(0, emailid_stu.indexOf('@'));
	      System.out.println(emailid_stu.substring(0, emailid_stu.indexOf('@')));
	      time = json.getString("time");
	      date = json.getString("date");
	      loc = json.getString("location");
	      latitudeSearch = json.getString("latitudeSearch");
	      longitudeSearch = json.getString("longitudeSearch");
	      numberOfSeatsRequired=json.getString("numberofseatsrequired");
	      
	      System.out.println("receiver class params main"+time+date+loc+numberOfSeatsRequired);
	      System.out.println("notificationText = "+alert);
	      System.out.println("email id = "+emailid_stu);
	    } catch (JSONException e) {
	      Log.d(TAG, "JSONException: " + e.getMessage());
	    }
	    
	    
	    
	    if(alert.startsWith("Request")){
		
	    //Log.e("Push", "Clicked");
	    Intent i = new Intent(context, RideConfirmActivity.class);
	    i.putExtras(intent.getExtras());
	    i.putExtra("email_id", emailid_stu);
	    i.putExtra("time", time);
	    i.putExtra("date", date);
	    i.putExtra("f_name", concatenatedString);
	    i.putExtra("loc", loc);
	    i.putExtra("numberOfSeatsRequired", numberOfSeatsRequired);
	    i.putExtra("latitudeSearch", latitudeSearch);
	    i.putExtra("longitudeSearch", longitudeSearch);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    System.out.println("receiver class params request"+time+date+loc+numberOfSeatsRequired);
	    context.startActivity(i);
	    }else if (alert.startsWith("Rejected")){
	    	 Intent i = new Intent(context, LoadAvailableListActivity.class);
	 	    i.putExtras(intent.getExtras());
	 	    i.putExtra("timeSearch", time);
	 	    System.out.println("receiver class params rejected"+time+date+loc+numberOfSeatsRequired);
		    i.putExtra("dateSearch", date);
		    i.putExtra("locationSearch", loc);
		    i.putExtra("numberOfSeatsRequired", numberOfSeatsRequired);
		    i.putExtra("latitudeSearch", latitudeSearch);
		    i.putExtra("longitudeSearch",longitudeSearch);
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    context.startActivity(i);
	    }
	}
}
