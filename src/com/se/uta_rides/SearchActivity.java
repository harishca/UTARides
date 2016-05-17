package com.se.uta_rides;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/*SearchActivity - Allows users to Search for a list of Car Owner, within a specified time and date*/
public class SearchActivity extends BaseActivity implements OnClickListener,
		OnItemSelectedListener {
	Button buttonSearch, buttonDate, buttonTime, buttonMap, buttonWishList;
	// private Spinner favDestDropDownList;
	EditText textDate, textTime, textSeatsRequired, textDestination;
	Intent i;
	Calendar calendar;
	String selectedDate, selectedTime, selectedLocation, selectedSeatsRequired;
	int mYear, mMonth, mDay, tHour, tMinute;
	DatePickerDialog datePick;
	TimePickerDialog timePick;
	String selectedLocationLatitude, selectedLocationLongitude,
			selectedLocationAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		/* Retrieves the Date, Time and Search button */
		buttonDate = (Button) findViewById(R.id.buttonDate);
		buttonTime = (Button) findViewById(R.id.buttonTime);
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		buttonMap = (Button) findViewById(R.id.buttonMap);
		buttonWishList = (Button) findViewById(R.id.buttonWishList);
		textSeatsRequired = (EditText) findViewById(R.id.textSeatsRequired);
		// favDestDropDownList = (Spinner)
		// findViewById(R.id.favDestDropDownList);
		//
		// ArrayAdapter<CharSequence> favDestDropDownListAdapter = ArrayAdapter
		// .createFromResource(this, R.array.favDestDropDownList,
		// android.R.layout.simple_spinner_item);
		// favDestDropDownListAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// favDestDropDownList.setAdapter(favDestDropDownListAdapter);
		// favDestDropDownList.setOnItemSelectedListener(this);
		System.out.println(9);

		/* Retrieves the Date and Time text views */
		textDate = (EditText) findViewById(R.id.textDate);
		textTime = (EditText) findViewById(R.id.textTime);
		textDestination = (EditText) findViewById(R.id.textDestination);

		buttonDate.setOnClickListener(this);
		buttonTime.setOnClickListener(this);
		buttonSearch.setOnClickListener(this);
		buttonMap.setOnClickListener(this);
		buttonWishList.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
					selectedLocationAddress = selectedLocationArray[2].replace("null", " ").trim();
					System.out.println("Got these from Map " + "address "
							+ selectedLocationAddress + "..." + "latitude"
							+ selectedLocationLatitude + "..." + "longitude"
							+ selectedLocationLongitude);
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		selectedSeatsRequired = textSeatsRequired.getText().toString();

		switch (view.getId()) {
		case R.id.buttonMap:
			try {
				selectedLocation = textDestination.getText().toString();
				if (selectedLocation != null && !selectedLocation.equals("")) {
					i = new Intent("com.se.uta_rides.maps.MAPSACTIVITY");
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

		case R.id.buttonDate:
			calendar = Calendar.getInstance();
			mYear = calendar.get(Calendar.YEAR);
			mMonth = calendar.get(Calendar.MONTH);
			mDay = calendar.get(Calendar.DAY_OF_MONTH);

			/* Lets the user pick the date of the ride */
			datePick = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							textDate.setText(dayOfMonth + "-"
									+ (monthOfYear + 1) + "-" + year);
							selectedDate = year + "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth;

						}
					}, mYear, mMonth, mDay);

			datePick.show();

			break;

		case R.id.buttonTime:
			calendar = Calendar.getInstance();
			tHour = calendar.get(Calendar.HOUR_OF_DAY);
			tMinute = calendar.get(Calendar.MINUTE);

			/* Lets the user pick the time of the ride */
			timePick = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							textTime.setText(hourOfDay + ":" + minute);

							selectedTime = hourOfDay + ":" + minute;
						}
					}, tHour, tMinute, false);

			timePick.show();

			break;

		case R.id.buttonSearch:
			String dateSearch = selectedDate;
			String timeSearch = selectedTime;
			String locationSearch = selectedLocationAddress;
			String latitudeSearch = selectedLocationLatitude;
			String longitudeSearch = selectedLocationLongitude;
			String numberOfSeatsRequired = selectedSeatsRequired;
			DateFormat formatter;
			Date selDate = null;

			if (numberOfSeatsRequired == null) {

				Toast.makeText(getApplicationContext(),
						"Please enter Number of seats!", Toast.LENGTH_SHORT)
						.show();
			} else if (locationSearch == null) {
				Toast.makeText(getApplicationContext(),
						"Please enter location!", Toast.LENGTH_SHORT).show();
			} else if (dateSearch == null) {
				Toast.makeText(getApplicationContext(), "Please enter date!",
						Toast.LENGTH_SHORT).show();
			} else if (timeSearch == null) {
				Toast.makeText(getApplicationContext(), "Please enter time!",
						Toast.LENGTH_SHORT).show();
			} else {

				formatter = new SimpleDateFormat("yyyy-MM-dd");
				try {
					selDate = formatter.parse(selectedDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println(sdf.format(date));

				if (selDate.before(date)) {
					Toast.makeText(getApplicationContext(),
							"Please Select a future date", Toast.LENGTH_SHORT)
							.show();
				} else {

					System.out.println("Searching...... " + dateSearch + " "
							+ timeSearch + " " + latitudeSearch + " "
							+ longitudeSearch + " " + locationSearch + " "
							+ numberOfSeatsRequired);
					i = new Intent(SearchActivity.this,
							LoadAvailableListActivity.class);
					i.putExtra("dateSearch", dateSearch);
					i.putExtra("timeSearch", timeSearch);
					i.putExtra("latitudeSearch", latitudeSearch);
					i.putExtra("longitudeSearch", longitudeSearch);
					i.putExtra("locationSearch", locationSearch);
					i.putExtra("numberOfSeatsRequired", numberOfSeatsRequired);
					startActivity(i);
				}
			}
			break;

		case R.id.buttonWishList:
			SharedPreferences userDetails = getSharedPreferences("MyData",
					Context.MODE_PRIVATE);
			String userName = userDetails.getString("name", "null");

			new SendData().execute(userName, selectedTime, selectedDate,
					selectedSeatsRequired, selectedLocationLatitude,
					selectedLocationLongitude, selectedLocationAddress);

			break;
		}
	}

	public String functionDate() {
		System.out.println("selected date " + selectedDate);
		return selectedDate;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
		Toast.makeText(
				parent.getContext(),
				"Location selected : "
						+ parent.getItemAtPosition(position).toString(),
				Toast.LENGTH_SHORT).show();
		selectedLocation = parent.getItemAtPosition(position).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	private class SendData extends AsyncTask<String, String, String> {
		HttpClient httpClient;
		HttpPost httpPost;

		@Override
		protected String doInBackground(String... params) {
			try {
				String email = params[0];
				String time = params[1];
				String date = params[2];
				String seats = params[3];
				String latitude = params[4];
				String longitude = params[5];
				String loc = params[6];
				String encodedLoc = URLEncoder.encode(loc, "UTF-8").replace(
						"+", "%20");
				String totimingsPHP = "email='" + email + "'&&" + "time='"
						+ time + "'&&" + "date='" + date + "'&&" + "seats='"
						+ seats + "'&&" + "loc='" + encodedLoc + "'";
				System.out.println(totimingsPHP);
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/create_wishlist.php?"
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
}