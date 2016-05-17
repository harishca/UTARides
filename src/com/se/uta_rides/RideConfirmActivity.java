package com.se.uta_rides;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RideConfirmActivity extends BaseActivity implements OnClickListener{

	String emailid_car1="";
	String emailid_stu1="";
	String date1="";
	String time1="";
	String location1="";
	String numberOfSeatsRequired="";
	Button confirm,reject;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	String status="";
	String studentName="";
	private TextView name=null;
	private TextView date=null;
	private TextView time=null;
	private TextView location=null;
	String latitudeSearch="";
	String longitudeSearch="";
	
	ParseApplication parse = new ParseApplication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rideconfirm);
		
		emailid_stu1 = getIntent().getStringExtra("email_id");
		studentName = getIntent().getStringExtra("f_name");
		date1 = getIntent().getStringExtra("date");
		time1 = getIntent().getStringExtra("time");
		location1 = getIntent().getStringExtra("loc");
		numberOfSeatsRequired=getIntent().getStringExtra("numberOfSeatsRequired");
		latitudeSearch=getIntent().getStringExtra("latitudeSearch");
		longitudeSearch=getIntent().getStringExtra("longitudeSearch");
		
		name=(TextView)findViewById(R.id.name);
		date=(TextView)findViewById(R.id.date);
		time=(TextView)findViewById(R.id.time);
		location=(TextView)findViewById(R.id.location);
		
		
		name.setText("Name:"+studentName);
		date.setText("Date:"+date1);
		time.setText("Time:"+time1);
		location.setText("Location:"+location1);
		
		final String DEFAULT="N/A";
		SharedPreferences sharedpreferences= getSharedPreferences("MyData",Context.MODE_PRIVATE);
		emailid_car1 = sharedpreferences.getString("name",DEFAULT);
	
		
		System.out.println("email id of Car owner"+emailid_car1);
		System.out.println("email id of student"+emailid_stu1);
		System.out.println("date recorded"+date1);
		System.out.println("time recorded"+time1);
		System.out.println("number of seats"+numberOfSeatsRequired);
		confirm = (Button)findViewById(R.id.confirmButton);
		reject = (Button)findViewById(R.id.rejectButton);
		
		
		confirm.setOnClickListener(this);
		reject.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.confirmButton:
			UserValidation validates = new UserValidation();
			validates.execute(emailid_car1,emailid_stu1,date1,time1,location1);
			break;
			
		case R.id.rejectButton:
			parse.sendNotificationtoStudent(emailid_stu1,emailid_car1,date1,time1,location1,numberOfSeatsRequired,latitudeSearch,longitudeSearch);
			break;
			
		}
	}
	
	
	private class UserValidation extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String emailid_car = params[0];
			String emailid_stu = params[1];
			String date = params[2];
			String time = params[3];
			String location = params[4];

			String encodedLoc = "";
			try {
				encodedLoc = URLEncoder.encode(location, "UTF-8").replace(
						"+", "%20");
			} catch (UnsupportedEncodingException e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try{
				String params1 = "s_email='" + emailid_stu + "'&&co_email='" + emailid_car
						+ "'&&rdate='" + date + "'&&rtime='" + time +"'&&rloc='" + encodedLoc
						+ "'&&seats="+numberOfSeatsRequired;
				String fullUrl = "http://omega.uta.edu/~sxk7162/update_ride_details.php?"
						+ params1;
				System.out.println("fullurl - " + fullUrl);
				httpClient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/update_ride_details.php?"
								+ params1);
				response = httpClient.execute(httppost);
				System.out.println(response);
				entity = response.getEntity();
				System.out.println("Entity Object" + entity.toString());
				if (entity != null) {
					isr = entity.getContent();
					parse.sendAccepted(emailid_stu1);
					System.out.println("byte - " + isr.available());
				}
			} catch (UnsupportedEncodingException e) {
				Log.e("log_tag",
						" Error in UnsupportedEncodingException - " + e.toString());
			} catch (ClientProtocolException e) {
				Log.e("log_tag",
						" Error in ClientProtocolException - " + e.toString());
			} catch (IOException e) {
				Log.e("log_tag", " Error in IOException - " + e.toString());
			} catch (Exception e) {
				Log.e("log_tag", " Error in Connection" + e.toString());
			}
			
			return null;
		}
		
	}

}
