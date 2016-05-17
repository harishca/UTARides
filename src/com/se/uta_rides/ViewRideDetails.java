package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRideDetails extends Activity {

	TextView carOwnerName, rideTakerName, date, time, location, numberOfSeats;
	JSONArray jsonArray;
	// RideData rideData = new RideData();
	String emailId = "";
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	ListAdapter adapter;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;
	String result = "";
	ArrayList<HashMap<String, String>> arrayList;
	ListView resultView;
	String carOwnerEmail, rideTakerEmail, rideDate, rideTime, rideLoc,
			seatsBooked;
	private static final String rideGivenTo = "ride_taker_email";
	private static final String rideGivenBy = "ride_giver_email";
	private static final String rideGivenDate = "ride_date";
	private static final String rideGivenTime = "ride_time";
	private static final String rideLocation = "ride_location";
	private static final String numberOfSeatsFilled = "seats_booked";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData",
				Context.MODE_PRIVATE);
		emailId = sharedpreferences.getString("name", DEFAULT);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booked_rides);
		resultView = (ListView) findViewById(R.id.bookedList);

		// carOwnerName = (TextView) findViewById(R.id.carOwnerName);
		// rideTakerName = (TextView) findViewById(R.id.rideTakerName);
		// numberOfSeats = (TextView) findViewById(R.id.numberOfSeats);
		// date = (TextView) findViewById(R.id.date);
		// time = (TextView) findViewById(R.id.time);
		// location = (TextView) findViewById(R.id.location);
		arrayList = new ArrayList<HashMap<String, String>>();
		ViewDetails viewData = new ViewDetails();
		viewData.execute(emailId);
	}

	private class ViewDetails extends
			AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			try {
				String email = params[0];

				String fullUrl = "http://omega.uta.edu/~sxk7162/view_ride_details.php?email='"
						+ email + "'";
				System.out.println("fullurl - " + fullUrl);
				httpClient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/view_ride_details.php?email='"
								+ email + "'");
				response = httpClient.execute(httppost);
				System.out.println(response);
				entity = response.getEntity();
				System.out.println("Entity Object" + entity.toString());
				if (entity != null) {
					isr = entity.getContent();
					System.out.println("byte - " + isr.available());
				}
			} catch (UnsupportedEncodingException e) {
				Log.e("ViewRideDetails - ",
						" Error in UnsupportedEncodingException - "
								+ e.toString());
			} catch (ClientProtocolException e) {
				Log.e("ViewRideDetails - ",
						" Error in ClientProtocolException - " + e.toString());
			} catch (IOException e) {
				Log.e("ViewRideDetails - ",
						" Error in IOException - " + e.toString());
			} catch (Exception e) {
				Log.e("ViewRideDetails - ",
						" Error in Connection" + e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(isr, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				isr.close();

				result = sb.toString();
				System.out.println("result from ISR : " + result);
			} catch (Exception e) {
				Log.e("ViewRideDetails",
						"Error converting result " + e.toString());
				// Toast.makeText(getBaseContext(), "No Rides Available",
				// Toast.LENGTH_LONG).show();
			}

			System.out.println("ViewRideDetails - " + result);

			try {
				if (result != null) {
					try {
						jsonArray = new JSONArray(result);
					} catch (JSONException | NullPointerException e) {
						Log.e("ViewRideDetails - ",
								"Error parsing data " + e.toString());
						// Toast.makeText(getBaseContext(),
						// "No Rides Available",
						// Toast.LENGTH_LONG).show();
					}
					try {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							System.out.println("reading from json file");
							carOwnerEmail = jsonObject.getString(rideGivenBy);
							rideTakerEmail = jsonObject.getString(rideGivenTo);
							rideDate = jsonObject.getString(rideGivenDate);
							rideTime = jsonObject.getString(rideGivenTime);
							rideLoc = jsonObject.getString(rideLocation);
							seatsBooked = jsonObject
									.getString(numberOfSeatsFilled);
							HashMap<String, String> map = new HashMap<String, String>();

							String carOwnerEmail1 = "Ride Provider : "
									+ carOwnerEmail;
							String rideTakerEmail1 = "Ride Receiver : "
									+ rideTakerEmail;
							String rideDate1 = "Ride Date : " + rideDate;
							String rideTime1 = "Ride Time : " + rideTime;
							String rideLoc1 = "Ride Location : " + rideLoc;
							String seatsBooked1 = "Seats Booked : "
									+ seatsBooked;

							System.out.println("Inserting it into hash map");

							map.put(rideGivenBy, carOwnerEmail1);
							map.put(rideGivenTo, rideTakerEmail1);
							map.put(rideGivenDate, rideDate1);
							map.put(rideGivenTime, rideTime1);
							map.put(rideLocation, rideLoc1);
							map.put(numberOfSeatsFilled, seatsBooked1);
							System.out.println("inserted into map");
							for (String key : map.keySet()) {
								System.out.println("Key : " + key + "Value : "
										+ map.get(key));
							}
							arrayList.add(map);
							System.out.println("after adding into map");
							for (int k = 0; k < arrayList.size(); k++) {
								System.out.println("Array List : "
										+ arrayList.get(k));
							}
						}
					} catch (JSONException | NullPointerException e) {
						Log.e("ViewRideDetailsActivity - Error", e.getMessage());
						e.printStackTrace();
						// Toast.makeText(getBaseContext(),
						// "No Rides Available",
						// Toast.LENGTH_LONG).show();
					}
				} else if (result == null) {
					Log.e("ViewRideDetailsActivity - ", "No rides");
					// Toast.makeText(getBaseContext(), "No Rides Available",
					// Toast.LENGTH_LONG).show();
				}
			} catch (NullPointerException e) {
				Log.e("ViewRideDetailsActivity - ", "No rides");
				// Toast.makeText(getBaseContext(), "No Rides Available",
				// Toast.LENGTH_LONG).show();
			}
			return arrayList;
		}

		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);

			try {
				System.out.println("Onpostexec");
				if (result.size() > 0) {
					for (int k = 0; k < result.size(); k++) {
						System.out.println("Array List : " + result.get(k));
					}
					// Retrieve a List View to set the list of available Rides
					resultView = (ListView) findViewById(R.id.bookedList);
					ListAdapter adapter = new SimpleAdapter(
							ViewRideDetails.this, result,
							R.layout.activity_view_rides, new String[] {
									rideGivenBy, rideGivenTo, rideGivenDate,
									rideGivenTime, rideLocation,
									numberOfSeatsFilled }, new int[] {
									R.id.carOwnerName, R.id.rideTakerName,
									R.id.date, R.id.time, R.id.location,
									R.id.numberOfSeats });
					resultView.setAdapter(adapter);
					System.out.println("after array display");
				} else {
					Toast.makeText(
							getApplicationContext(),
							"You have not booked any rides. Please press back to return to previous screen",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"You have not booked any rides. Please press back to return to previous screen",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
