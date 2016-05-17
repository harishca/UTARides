package com.se.uta_rides;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

/*CarOwnerSetAvailbleActivity - Allows Car Owners to set their availability times*/
public class CarOwnerSetAvailbleActivity extends BaseActivity implements
		OnClickListener, OnItemSelectedListener {
	private Spinner dayDropDownList, favSpotDropDownList;
	private Button buttonSave, buttonUpdate, buttonStartTime, buttonEndTime,
			buttonMapSetAvailable;
	private EditText textStartTime, textEndTime, textNumberOfSeats,
			textDestinationSetAvailable;
	Calendar calendar;
	String selectedDate, selectedTime, selectedNumberOfSeats;
	String selectedStartTime, selectedEndTime, selectedLocation;
	int mYear, mMonth, mDay, tHour, tMinute;
	TimePickerDialog timePick;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;
	String result, status1;
	JSONArray jsonArray;
	String selectedLocationLatitude, selectedLocationLongitude,
			selectedLocationAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carowner_set_available);

		dayDropDownList = (Spinner) findViewById(R.id.dayDropDownList);
		// favSpotDropDownList = (Spinner)
		// findViewById(R.id.favSpotDropDownList);
		buttonStartTime = (Button) findViewById(R.id.buttonStartTime);
		buttonEndTime = (Button) findViewById(R.id.buttonEndTime);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
		buttonMapSetAvailable = (Button) findViewById(R.id.buttonMapSetAvailable);
		textStartTime = (EditText) findViewById(R.id.textStartTime);
		textEndTime = (EditText) findViewById(R.id.textEndTime);
		textNumberOfSeats = (EditText) findViewById(R.id.textNumberOfSeats);
		textDestinationSetAvailable = (EditText) findViewById(R.id.textDestinationSetAvailable);

		selectedNumberOfSeats = textNumberOfSeats.getText().toString();
		System.out.println("number of seats=" + selectedNumberOfSeats);

		buttonSave.setOnClickListener(this);
		buttonStartTime.setOnClickListener(this);
		buttonEndTime.setOnClickListener(this);
		buttonUpdate.setOnClickListener(this);
		buttonMapSetAvailable.setOnClickListener(this);

		// dayDropDownList.setOnItemSelectedListener(this);

		System.out.println(8);

		System.out.println(9);
		ArrayAdapter<CharSequence> dayDropDownListAdapter = ArrayAdapter
				.createFromResource(this, R.array.dayDropDownList,
						android.R.layout.simple_spinner_item);
		dayDropDownListAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dayDropDownList.setAdapter(dayDropDownListAdapter);
		dayDropDownList.setOnItemSelectedListener(this);

		// ArrayAdapter<CharSequence> favSpotDropDownListAdapter = ArrayAdapter
		// .createFromResource(this, R.array.favSpotDropDownList,
		// android.R.layout.simple_spinner_item);
		// favSpotDropDownListAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// favSpotDropDownList.setAdapter(favSpotDropDownListAdapter);
		// favSpotDropDownList.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		selectedNumberOfSeats = textNumberOfSeats.getText().toString();
		System.out.println("number of seats=" + selectedNumberOfSeats);

		switch (v.getId()) {
		case R.id.buttonMapSetAvailable:
			try {
				selectedLocation = textDestinationSetAvailable.getText()
						.toString();
				if (selectedLocation != null && !selectedLocation.equals("")) {
					Intent i = new Intent("com.se.uta_rides.maps.MAPSACTIVITY");
					i.putExtra("sendLocation", selectedLocation);
					startActivityForResult(i, 10);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter a Location to search",
							Toast.LENGTH_LONG);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Please enter a Location to search", Toast.LENGTH_LONG);
			}

			break;

		

		case R.id.buttonStartTime:
			calendar = Calendar.getInstance();
			tHour = calendar.get(Calendar.HOUR_OF_DAY);
			tMinute = calendar.get(Calendar.MINUTE);

			/* Lets the user pick the time of the ride */
			timePick = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							System.out.println("... " + hourOfDay);
							System.out.println(".... " + minute);
							textStartTime.setText(hourOfDay + ":" + minute);

							selectedStartTime = hourOfDay + ":" + minute;
						}
					}, tHour, tMinute, false);

			timePick.show();

			break;

		case R.id.buttonEndTime:
			calendar = Calendar.getInstance();
			tHour = calendar.get(Calendar.HOUR_OF_DAY);
			tMinute = calendar.get(Calendar.MINUTE);

			/* Lets the user pick the time of the ride */
			timePick = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							System.out.println("... " + hourOfDay);
							System.out.println(".... " + minute);
							textEndTime.setText(hourOfDay + ":" + minute);

							selectedEndTime = hourOfDay + ":" + minute;
						}
					}, tHour, tMinute, false);

			timePick.show();

			break;
		case R.id.buttonSave:
			System.out.println("Entered button save");
			System.out.println("selectedStartTime"+selectedStartTime);
			if(selectedStartTime == null){
				Toast.makeText(getApplicationContext(),
						"Enter values in Start time field!",
						Toast.LENGTH_SHORT).show();				
			}
			else if(selectedLocationAddress== null){
				Toast.makeText(getApplicationContext(),
						"Please select location from map!",
						Toast.LENGTH_SHORT).show();
			}
			else if(selectedEndTime==null){
				Toast.makeText(getApplicationContext(),
						"Please select end time!",
						Toast.LENGTH_SHORT).show();
			}
			else if (selectedNumberOfSeats==null) {
				Toast.makeText(getApplicationContext(),
						"Enter values in number of seats field",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						CarOwnerSetAvailbleActivity.this,
						"You selected : "
								+ "\n"
								+ "Day : "
								+ String.valueOf(dayDropDownList
										.getSelectedItem()) + "\n"
								+ "Location : "
						// +
						// String.valueOf(favSpotDropDownList.getSelectedItem())
						, Toast.LENGTH_SHORT).show();

				SharedPreferences userDetails = getSharedPreferences("MyData",
						Context.MODE_PRIVATE);
				String userName = userDetails.getString("name", "null");

				new SendData().execute(userName,
						String.valueOf(dayDropDownList.getSelectedItem()),
						selectedStartTime, selectedEndTime,
						selectedNumberOfSeats, selectedLocationLatitude,
						selectedLocationLongitude, selectedLocationAddress);
			}
			break;
			
		case R.id.buttonUpdate:
			
			System.out.println("update");
			if(selectedStartTime.isEmpty()){
				Toast.makeText(getApplicationContext(),
						"Please select Start time!",
						Toast.LENGTH_SHORT).show();
			}
			else if(selectedLocationAddress.isEmpty()){
				Toast.makeText(getApplicationContext(),
						"Please select location from map!",
						Toast.LENGTH_SHORT).show();
			}
			else if(selectedEndTime.isEmpty()){
				Toast.makeText(getApplicationContext(),
						"Please select end time!",
						Toast.LENGTH_SHORT).show();
			}
			else if (selectedNumberOfSeats.isEmpty()) {
				Toast.makeText(getApplicationContext(),
						"Enter values in number of seats field!",
						Toast.LENGTH_SHORT).show();
			} else {

				UpdateData update = new UpdateData();

				SharedPreferences userDetails = getSharedPreferences("MyData",
						Context.MODE_PRIVATE);
				String userName = userDetails.getString("name", "null");

				AsyncTask<String, String, String> checkTimings = update
						.execute(userName, String.valueOf(dayDropDownList
								.getSelectedItem()), selectedStartTime,
								selectedEndTime, selectedNumberOfSeats,
								selectedLocationLatitude,
								selectedLocationLongitude,
								selectedLocationAddress);
				try {
					if (checkTimings.get() != "") {
						System.out.println("timings not set set it first");
						Toast.makeText(CarOwnerSetAvailbleActivity.this,
								"Please save the timings before update", 2000)
								.show();
					} else {
						Toast.makeText(
								CarOwnerSetAvailbleActivity.this,
								"You selected : "
										+ "\n"
										+ "Day : "
										+ String.valueOf(dayDropDownList
												.getSelectedItem()) + "\n"
										+ "Location : "
								// + String.valueOf(favSpotDropDownList
								// .getSelectedItem())
								, Toast.LENGTH_SHORT).show();
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 10) {
			if (data.hasExtra("returnLocation")) {
				String returnLocation = data.getExtras().getString(
						"returnLocation");
				if (returnLocation != null && returnLocation.length() > 0) {
					Toast.makeText(getApplicationContext(), returnLocation,
							Toast.LENGTH_SHORT).show();
					System.out.println("myreturn " + returnLocation);
					String[] selectedLocationArray = returnLocation.split(":",
							3);
					for (int z = 0; z < selectedLocationArray.length; z++) {
						System.out.println(selectedLocationArray[z]);
					}
					selectedLocationLatitude = selectedLocationArray[0];
					selectedLocationLongitude = selectedLocationArray[1];
					selectedLocationAddress = selectedLocationArray[2];
					System.out.println("Got these from Map " + "address "
							+ selectedLocationAddress + "..." + "latitude"
							+ selectedLocationLatitude + "..." + "longitude"
							+ selectedLocationLongitude);
				}
			}
		}
	}

	private class SendData extends AsyncTask<String, String, String> {
		HttpClient httpClient;
		HttpPost httpPost;

		@Override
		protected String doInBackground(String... params) {
			try {
				String email = params[0];
				String day_id = params[1];
				String st = params[2];
				String et = params[3];
				String seats = params[4];
				String latitude = params[5];
				String longitude = params[6];
				String loc = params[7];
				String encodedLoc = URLEncoder.encode(loc, "UTF-8").replace(
						"+", "%20");
				String totimingsPHP = "email='" + email + "'&&" + "day_id='"
						+ day_id + "'&&" + "loc='" + encodedLoc + "'&&"
						+ "st='" + st + "'&&" + "et='" + et + "'&&"
						+ "lat='" + latitude + "'&&" + "long='"
						+ longitude + "'&&" + "seats=" + seats;
				System.out.println(totimingsPHP);
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/enter_timings.php?"
								+ totimingsPHP);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			System.out.println("wooooooooo hooo");

			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
			} catch (UnsupportedEncodingException e) {
				Log.e("CarOwnerSetAvailableActivity URL Encode - ",
						e.toString());
			} catch (IllegalArgumentException e) {
				Log.e("CarOwnerSetAvailableActivity Illegal Args - ",
						e.toString());
			} catch (HttpResponseException e) {
				Log.e("CarOwnerSetAvailableActivity Response - ", e.toString());
			} catch (ClientProtocolException e) {
				Log.e("CarOwnerSetAvailableActivity Protocol - ", e.toString());
			} catch (HttpHostConnectException e) {
				Log.e("CarOwnerSetAvailableActivity Connection - ",
						e.toString());
			} catch (IOException e) {
				Log.e("CarOwnerSetAvailableActivity IO - ", e.toString());
			}

			return null;
		}
	}

	private class UpdateData extends AsyncTask<String, String, String> {
		HttpClient httpClient;
		HttpPost httpPost;

		@Override
		protected String doInBackground(String... params) {
			try {
				String email = params[0];
				String day_id = params[1];
				String st = params[2];
				String et = params[3];
				String seats = params[4];
				String latitude = params[5];
				String longitude = params[6];
				String loc = params[7];
				String encodedLoc = URLEncoder.encode(loc, "UTF-8").replace(
						"+", "%20");
				String totimingsPHP = "email='" + email + "'&&" + "day_id='"
						+ day_id + "'&&" + "loc='" + encodedLoc + "'&&"
						+ "st='" + st + "'&&" + "et='" + et + "'&&"
						+ "lat='" + latitude + "'&&" + "long='"
						+ longitude + "'&&" + "seats=" + seats;
				System.out.println(totimingsPHP);
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/update_timings_check.php?"
								+ totimingsPHP);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			System.out.println("weeeeee heeee");

			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
				entity = httpResponse.getEntity();
				if (entity != null) {
					isr = entity.getContent();
					System.out.println("byte - " + isr.available());
				}
			} catch (UnsupportedEncodingException e) {
				Log.e("CarOwnerSetAvailableActivity URL Encode - ",
						e.toString());
			} catch (IllegalArgumentException e) {
				Log.e("CarOwnerSetAvailableActivity Illegal Args - ",
						e.toString());
			} catch (HttpResponseException e) {
				Log.e("CarOwnerSetAvailableActivity Response - ", e.toString());
			} catch (ClientProtocolException e) {
				Log.e("CarOwnerSetAvailableActivity Protocol - ", e.toString());
			} catch (HttpHostConnectException e) {
				Log.e("CarOwnerSetAvailableActivity Connection - ",
						e.toString());
			} catch (IOException e) {
				Log.e("CarOwnerSetAvailableActivity IO - ", e.toString());
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
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			System.out.println("CarOwnerUpdateAvailability - " + result);

			return result;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
