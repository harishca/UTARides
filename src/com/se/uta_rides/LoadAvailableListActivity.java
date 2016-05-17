package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/*LoadAvailableListActivity - Loads the list of available Car Owners*/
public class LoadAvailableListActivity extends Activity {
	JSONArray jsonArray;
	ListView listView;
	ListAdapter adapter;
	ProgressDialog mProgressDialog;
	HashMap<String, String> map;
	ArrayList<HashMap<String, String>> arrayList;
	// ArrayList<HashMap<String, String>> arrayList1;
	ListView resultView;
	String dateSearch, timeSearch, result, locationSearch,
			encodedLocationSearch, numberOfSeatsRequired, latitudeSearch,
			longitudeSearch;
	String firstName, lastName, phoneNumber, email, startTime, endTime;
	private static final String NAME = "u_name";
	private static final String PHONE_NUMBER = "u_contact";
	private static final String EMAIL = "u_email";
	private static final String START_TIME = "start_time";
	private static final String END_TIME = "end_time";
	// public static final String ID_EXTRA="ID";
	List<NameValuePair> newValuePairs;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_available_list);
		StrictMode.enableDefaults();
		resultView = (ListView) findViewById(R.id.listAvailable);
		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData",
				Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("name", DEFAULT);
		System.out.println("Name++++++++ " + name);

		/* Retrieves the Date and Time values sent from Search Activity */
		dateSearch = getIntent().getStringExtra("dateSearch");
		timeSearch = getIntent().getStringExtra("timeSearch");
		locationSearch = getIntent().getStringExtra("locationSearch");
		latitudeSearch = getIntent().getStringExtra("latitudeSearch");
		longitudeSearch = getIntent().getStringExtra("longitudeSearch");
		numberOfSeatsRequired = getIntent().getStringExtra(
				"numberOfSeatsRequired");

		try {
			encodedLocationSearch = URLEncoder.encode(locationSearch, "UTF-8")
					.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			Log.e("LoadAvailableListActivity - UnsupportedEncodingException",
					e.toString());
		}
		arrayList = new ArrayList<HashMap<String, String>>();

		try {
			System.out.println("time stamp - " + dateSearch + " " + timeSearch);
			newValuePairs = new ArrayList<NameValuePair>(2);
			newValuePairs.add(new BasicNameValuePair("day_id", dateSearch));
			newValuePairs.add(new BasicNameValuePair("time", timeSearch));
			System.out.println("Value" + newValuePairs.toString());

			/* Create a query parameters to be sent to web services */
			String params = "day_id='" + dateSearch + "'&&time='" + timeSearch
					+ "'&&loc='" + encodedLocationSearch + "'&&email='" + name
					+ "'&&lat='" + latitudeSearch + "'&&long='"
					+ longitudeSearch + "'&&seats=" + numberOfSeatsRequired;
			String fullUrl = "http://omega.uta.edu/~sxk7162/get_carowner_details.php?"
					+ params;
			System.out.println("fullurl - " + fullUrl);
			httpClient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://omega.uta.edu/~sxk7162/get_carowner_details.php?"
							+ params);
			response = httpClient.execute(httppost);
			System.out.println(response);
			entity = response.getEntity();
			System.out.println("Entity Object" + entity.toString());
			if (entity != null) {
				isr = entity.getContent();
				System.out.println("byte - " + isr.available());
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("LoadAvailableListActivity - ",
					" Error in UnsupportedEncodingException - " + e.toString());
		} catch (ClientProtocolException e) {
			Log.e("LoadAvailableListActivity - ",
					" Error in ClientProtocolException - " + e.toString());
		} catch (IOException e) {
			Log.e("LoadAvailableListActivity - ", " Error in IOException - "
					+ e.toString());
		} catch (Exception e) {
			Log.e("LoadAvailableListActivity - ",
					" Error in Connection" + e.toString());
		}

		// Convert Response to String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					isr, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			isr.close();

			result = sb.toString();
			System.out.println("result from ISR : " + result);
		} catch (Exception e) {
			Log.e("LoadAvailableListActivity - ", "Error converting result "
					+ e.toString());
		}

		System.out.println("LoadAvailableListActivity - " + result);

		boolean flag = false;

		if (result != null) {
			try {
				jsonArray = new JSONArray(result);
			} catch (JSONException | NullPointerException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				flag = true;
			}
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					firstName = jsonObject.getString(NAME);
					phoneNumber = jsonObject.getString(PHONE_NUMBER);
					email = jsonObject.getString(EMAIL);
					startTime = jsonObject.getString(START_TIME);
					endTime = jsonObject.getString(END_TIME);
					HashMap<String, String> map = new HashMap<String, String>();

					map.put(NAME, firstName);
					map.put(PHONE_NUMBER, phoneNumber);
					map.put(EMAIL, email);
					map.put(START_TIME, startTime);
					map.put(END_TIME, endTime);
					arrayList.add(map);
				}
				/* Retrieve a List View to set the list of available Rides */
				resultView = (ListView) findViewById(R.id.listAvailable);
				ListAdapter adapter = new SimpleAdapter(
						LoadAvailableListActivity.this, arrayList,
						R.layout.activity_median, new String[] { NAME },
						new int[] { R.id.textMedian });
				resultView.setAdapter(adapter);
				resultView.setOnItemClickListener(onListClick);

			} catch (JSONException | NullPointerException e) {
				Log.e("log_tag", "No clue of this - " + e.toString());
				flag = true;
				e.printStackTrace();
			}
		} else if (result == null) {
			Toast.makeText(
					getApplicationContext(),
					"No Rides Available. You can go back and add this search to your wish list",
					Toast.LENGTH_LONG).show();
		}
		if (flag == true) {
			Toast.makeText(
					getApplicationContext(),
					"No Rides Available right now. You can go back and add this search to your wish list",
					Toast.LENGTH_LONG).show();
		}
	}

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(LoadAvailableListActivity.this,
					SelectedUserDetails.class);
			System.out.println(String.valueOf(id));
			System.out.println(arrayList.get((int) id).get("u_name"));

			i.putExtra("firstName", arrayList.get((int) id).get("u_name"));
			i.putExtra("email", arrayList.get((int) id).get("u_email"));
			i.putExtra("phoneNumber", arrayList.get((int) id).get("u_contact"));
			i.putExtra("startTime", arrayList.get((int) id).get("start_time"));
			i.putExtra("endTime", arrayList.get((int) id).get("end_time"));
			i.putExtra("datesearch", dateSearch);
			i.putExtra("timesearch", timeSearch);
			i.putExtra("locsearch", locationSearch);
			i.putExtra("latitudeSearch", latitudeSearch);
			i.putExtra("longitudeSearch", longitudeSearch);
			startActivity(i);
		}
	};
}