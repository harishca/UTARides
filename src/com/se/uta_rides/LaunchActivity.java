package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*LaunchActivity - Allows user to select "Booking a ride" and "Providing a Ride"*/
public class LaunchActivity extends BaseActivity implements OnClickListener {

	Button student, carOwner, viewRide;
	Intent selectPath;
	String emailId="";
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;
	String result="";
//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		/*final String DEFAULT="N/A";
		 SharedPreferences sharedpreferences= getSharedPreferences("MyData",Context.MODE_PRIVATE);
		emailId=sharedpreferences.getString("name",DEFAULT); */
		
		/*Retrieves Student and Car Owner buttons*/
		student = (Button) findViewById(R.id.launchStudent);
		carOwner = (Button) findViewById(R.id.launchCarOwner);
		viewRide = (Button) findViewById(R.id.viewBookedRides);
		student.setOnClickListener(this);
		carOwner.setOnClickListener(this);
		viewRide.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.launchStudent:
			startActivity(new Intent("com.se.uta_rides.SEARCHACTIVITY"));
			break;

		case R.id.launchCarOwner:
			startActivity(new Intent("com.se.uta_rides.CAROWNERSETAVAILABLEACTIVITY"));
			break;
			
		case R.id.viewBookedRides:
			
			startActivity(new Intent("com.se.uta_rides.VIEWRIDEDETAILS"));
			break;
		}

	}
	

}