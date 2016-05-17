package com.se.uta_rides;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.se.uta_rides.CarOwnersDetailsActivity;

public class ParseApplication extends Application{
	
	
	Button send;
	JSONObject data = new JSONObject();
	@SuppressWarnings("deprecation")
	public void onCreate(){
		super.onCreate();
		Parse.initialize(this, Keys.applicationID , Keys.clientKey );
		PushService.setDefaultPushCallback(this, CarOwnersDetailsActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public void sendNotificationtoCarowner(String email,String date,String time,String loc,String emailid_stu,String numberofseatsrequired,String latitudeSearch,String longitudeSearch){
		
		ParseQuery pQuery = ParseInstallation.getQuery();
		pQuery.whereEqualTo("User_id", email);
		System.out.println("query executed");
		try {
			data.put("emailid_stu", emailid_stu);
			data.put("alert", "Request for ride by "+emailid_stu);
			data.put("time", time);
			data.put("date", date);
			data.put("location", loc);
			data.put("numberofseatsrequired",numberofseatsrequired);
			data.put("latitudeSearch",latitudeSearch);
			data.put("longitudeSearch", longitudeSearch);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParsePush parsepush = new ParsePush();
		parsepush.setQuery(pQuery);
		parsepush.setData(data);
		parsepush.sendInBackground();
		System.out.println("Message sent");
		System.out.println(data.toString());
	}
	
	public void sendNotificationtoStudent(String emailid_stu,String emailid_car,String date,String time,String loc,String numberofseatsrequired,String latitudeSearch,String longitudeSearch){
		ParseQuery pQuery = ParseInstallation.getQuery();
		pQuery.whereEqualTo("User_id", emailid_stu);
		System.out.println("query executed");
		System.out.println("query executed");
		try {
			data.put("emailid_stu", emailid_stu);
			data.put("alert", "Rejected ride by "+emailid_car+" please select other ride");
			data.put("time", time);
			data.put("date", date);
			data.put("location", loc);
			data.put("numberofseatsrequired",numberofseatsrequired);
			data.put("latitudeSearch",latitudeSearch);
			data.put("longitudeSearch", longitudeSearch);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParsePush parsepush = new ParsePush();
		parsepush.setQuery(pQuery);
		parsepush.setData(data);
		//parsepush.setMessage("Rejected");
		parsepush.sendInBackground();
	}
	
	public void sendAccepted(String emailid){
		ParseQuery pQuery = ParseInstallation.getQuery();
		pQuery.whereEqualTo("User_id", emailid);
		System.out.println("query executed");
		ParsePush parsepush = new ParsePush();
		parsepush.setQuery(pQuery);
		//parsepush.setData(data);//to be used while handling accepted.
		parsepush.setMessage("accepted");
		parsepush.sendInBackground();
	}

}
